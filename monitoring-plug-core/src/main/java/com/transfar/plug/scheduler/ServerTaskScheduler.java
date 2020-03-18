package com.transfar.plug.scheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.transfar.plug.core.ConfigLoader;
import com.transfar.plug.task.ServerTask;

/**
 * <p>
 * 发送服务器信息任务调度器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午4:42:19
 */
public class ServerTaskScheduler {

	/**
	 * <p>
	 * 开始定时发送服务器信息包
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月7日 下午4:43:35
	 */
	public static void run() {
		// 是否发送服务器信息
		boolean serverInfoEnable = ConfigLoader.monitoringProperties.getMonitoringServerInfoProperties().isEnable();
		if (serverInfoEnable) {
			// 重新开启线程，让他单独去做我们想要做的操作，抛出异常并不会影响到主线程
			// Thread thread = new Thread(() -> {
			// AtomicInteger atomic = new AtomicInteger();
			final ScheduledExecutorService seService = Executors.newScheduledThreadPool(5, new ThreadFactory() {
				AtomicInteger atomic = new AtomicInteger();

				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "monitoring-server-pool-thread-" + this.atomic.getAndIncrement());
				}
			});
			// 发送服务器信息的频率
			long rate = ConfigLoader.monitoringProperties.getMonitoringServerInfoProperties().getRate();
			seService.scheduleAtFixedRate(new ServerTask(), 30, rate, TimeUnit.SECONDS);
			// });
			// 设置线程名
			// thread.setName("monitoring-server-thread");
			// 开始执行分进程
			// thread.start();
		}
	}
}
