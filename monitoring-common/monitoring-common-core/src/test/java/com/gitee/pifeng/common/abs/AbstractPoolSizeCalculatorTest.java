package com.gitee.pifeng.common.abs;

import org.junit.Test;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * <p>
 * 测试{@link AbstractPoolSizeCalculator}
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/24 22:52
 */
public class AbstractPoolSizeCalculatorTest extends AbstractPoolSizeCalculator {

    @Test
    public void test() {
        AbstractPoolSizeCalculatorTest calculator = new AbstractPoolSizeCalculatorTest();
        calculator.calculateBoundaries(new BigDecimal("1.0"), new BigDecimal(100000));
    }

    @Override
    public long getCurrentThreadCPUTime() {
        return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }

    @Override
    public Runnable creatTask() {
        return () -> {
        };
    }

    @Override
    public BlockingQueue<Runnable> createWorkQueue() {
        return new LinkedBlockingQueue<>();
    }
}
