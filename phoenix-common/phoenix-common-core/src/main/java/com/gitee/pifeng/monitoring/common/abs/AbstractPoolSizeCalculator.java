package com.gitee.pifeng.monitoring.common.abs;

import cn.hutool.core.lang.Console;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;

/**
 * A class that calculates the optimal thread pool boundaries. It takes the desired target utilization and the desired
 * work queue memory consumption as input and retuns thread count and work queue capacity.
 *
 * @author Niklas Schlimm
 */
public abstract class AbstractPoolSizeCalculator {

    /**
     * The sample queue size to calculate the size of a single {@link Runnable} element.
     */
    private final int SAMPLE_QUEUE_SIZE = 1000;

    /**
     * Accuracy of test run. It must finish within 20ms of the testTime otherwise we retry the test. This could be
     * configurable.
     */
    private final int EPSYLON = 20;

    /**
     * Control variable for the CPU time investigation.
     */
    private volatile boolean expired;

    /**
     * Time (millis) of the test run in the CPU time calculation.
     */
    private final long testtime = 3000;

    /**
     * Calculates the boundaries of a thread pool for a given {@link Runnable}.
     *
     * @param targetUtilization    the desired utilization of the CPUs (0 &lt;= targetUtilization &lt;= 1)
     * @param targetQueueSizeBytes the desired maximum work queue size of the thread pool (bytes)
     */
    public void calculateBoundaries(BigDecimal targetUtilization, BigDecimal targetQueueSizeBytes) {
        calculateOptimalCapacity(targetQueueSizeBytes);
        Runnable task = creatTask();
        start(task);
        // warm up phase
        start(task);
        long cputime = getCurrentThreadCPUTime();
        // test intervall
        start(task);
        cputime = getCurrentThreadCPUTime() - cputime;
        long waittime = (testtime * 1000000) - cputime;
        calculateOptimalThreadCount(cputime, waittime, targetUtilization);
    }

    private void calculateOptimalCapacity(BigDecimal targetQueueSizeBytes) {
        long mem = calculateMemoryUsage();
        BigDecimal queueCapacity = targetQueueSizeBytes.divide(new BigDecimal(mem), RoundingMode.HALF_UP);
        Console.log("Target queue memory usage (bytes): " + targetQueueSizeBytes);
        Console.log("createTask() produced " + creatTask().getClass().getName() + " which took " + mem
                + " bytes in a queue");
        Console.log("Formula: " + targetQueueSizeBytes + " / " + mem);
        Console.log("* Recommended queue capacity (bytes): " + queueCapacity);
    }

    /**
     * Brian Goetz' optimal thread count formula, see 'Java Concurrency in Practice' (chapter 8.2)
     *
     * @param cpu               cpu time consumed by considered task
     * @param wait              wait time of considered task
     * @param targetUtilization target utilization of the system
     */
    private void calculateOptimalThreadCount(long cpu, long wait, BigDecimal targetUtilization) {
        BigDecimal waitTime = new BigDecimal(wait);
        BigDecimal computeTime = new BigDecimal(cpu);
        BigDecimal numberOfCPU = new BigDecimal(Runtime.getRuntime().availableProcessors());
        BigDecimal optimalthreadcount = numberOfCPU.multiply(targetUtilization).multiply(
                new BigDecimal(1).add(waitTime.divide(computeTime, RoundingMode.HALF_UP)));
        Console.log("Number of CPU: " + numberOfCPU);
        Console.log("Target utilization: " + targetUtilization);
        Console.log("Elapsed time (nanos): " + (testtime * 1000000));
        Console.log("Compute time (nanos): " + cpu);
        Console.log("Wait time (nanos): " + wait);
        Console.log("Formula: " + numberOfCPU + " * " + targetUtilization + " * (1 + " + waitTime + " / "
                + computeTime + ")");
        Console.log("* Optimal thread count: " + optimalthreadcount);
    }

    /**
     * Runs the {@link Runnable} over a period defined in {@link #testtime}. Based on Heinz Kabbutz' ideas
     * (http://www.javaspecialists.eu/archive/Issue124.html).
     *
     * @param task the runnable under investigation
     */
    public void start(Runnable task) {
        long start = 0;
        int runs = 0;
        do {
            if (++runs > 5) {
                throw new IllegalStateException("Test not accurate");
            }
            expired = false;
            start = System.currentTimeMillis();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    expired = true;
                }
            }, testtime);
            while (!expired) {
                task.run();
            }
            start = System.currentTimeMillis() - start;
            timer.cancel();
        } while (Math.abs(start - testtime) > EPSYLON);
        collectGarbage(3);
    }

    private void collectGarbage(int times) {
        for (int i = 0; i < times; i++) {
            System.gc();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Calculates the memory usage of a single element in a work queue. Based on Heinz Kabbutz' ideas
     * (http://www.javaspecialists.eu/archive/Issue029.html).
     *
     * @return memory usage of a single {@link Runnable} element in the thread pools work queue
     */
    public long calculateMemoryUsage() {
        BlockingQueue<Runnable> queue = createWorkQueue();
        for (int i = 0; i < SAMPLE_QUEUE_SIZE; i++) {
            queue.add(creatTask());
        }
        long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        queue = null;
        collectGarbage(15);
        mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        queue = createWorkQueue();
        for (int i = 0; i < SAMPLE_QUEUE_SIZE; i++) {
            queue.add(creatTask());
        }
        collectGarbage(15);
        mem1 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        return (mem1 - mem0) / SAMPLE_QUEUE_SIZE;
    }

    /**
     * Create your runnable task here.
     *
     * @return an instance of your runnable task under investigation
     */
    public abstract Runnable creatTask();

    /**
     * Return an instance of the queue used in the thread pool.
     *
     * @return queue instance
     */
    public abstract BlockingQueue<Runnable> createWorkQueue();

    /**
     * Calculate current cpu time. Various frameworks may be used here, depending on the operating system in use. (e.g.
     * http://www.hyperic.com/products/sigar). The more accurate the CPU time measurement, the more accurate the results
     * for thread count boundaries.
     *
     * @return current cpu time of current thread
     */
    public abstract long getCurrentThreadCPUTime();

}