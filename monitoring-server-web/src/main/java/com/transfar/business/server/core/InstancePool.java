package com.transfar.business.server.core;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.transfar.business.server.domain.Instance;

/**
 * <p>
 * 应用池，维护哪些应用在线，哪些应用离线
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月9日 上午10:53:52
 */
public class InstancePool {

	private ConcurrentHashMap<String, Instance> instanceMap = new ConcurrentHashMap<>();

//}

//class InstanceStatusTaskScheduler{

	public void isOnline() {
		final ScheduledExecutorService seService = Executors.newScheduledThreadPool(this.instanceMap.size(), new ThreadFactory() {
			AtomicInteger atomic = new AtomicInteger();

			@Override
			public Thread newThread(Runnable r) {
				return new Thread(r, "monitoring-heartbeat-pool-thread-" + this.atomic.getAndIncrement());
			}
		});
		// 心跳频率
		// long rate =
		// ConfigLoader.monitoringProperties.getHeartbeatProperties().getRate();
		// seService.scheduleAtFixedRate(new InstanceStatusTask(), 30, rate,
		// TimeUnit.SECONDS);
	}

	class InstanceStatusTask implements Runnable {

		@Override
		public void run() {

		}
	}
}
