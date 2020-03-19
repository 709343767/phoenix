package com.transfar.server.business.server.core;

import com.transfar.common.constant.AlarmLevelEnums;
import com.transfar.common.domain.Alarm;
import com.transfar.common.dto.AlarmPackage;
import com.transfar.common.util.NetUtils;
import com.transfar.server.business.server.domain.Instance;
import com.transfar.server.business.server.service.IAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 在容器启动后，定时扫描应用实例池中的所有应用，实时更新应用实例状态
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/12 12:07
 */
@Slf4j
@Component
public class InstanceCommandLineRunner implements CommandLineRunner {

	/**
	 * 应用实例池
	 */
	@Autowired
	private InstancePool instancePool;

	/**
	 * 告警服务接口
	 */
	@Autowired
	private IAlarmService alarmService;

	@Override
	public void run(String... args) {
		// 重新开启线程，让他单独去做我们想要做的操作，此时CommandLineRunner执行的操作和主线程是相互独立的，抛出异常并不会影响到主线程
		Thread thread = new Thread(() -> {
			final ScheduledExecutorService seService = Executors.newScheduledThreadPool(5, new ThreadFactory() {
				AtomicInteger atomic = new AtomicInteger();

				@Override
				public Thread newThread(Runnable r) {
					return new Thread(r, "monitoring-instance-pool-thread-" + this.atomic.getAndIncrement());
				}
			});
			seService.scheduleAtFixedRate(() -> {
				// 循环所有应用实例
				for (Map.Entry<String, Instance> entry : this.instancePool.entrySet()) {
					String key = entry.getKey();
					Instance instance = entry.getValue();
					// 允许的误差时间
					int thresholdSecond = instance.getThresholdSecond();
					// 最后一次通过心跳包更新的时间
					Date dateTime = instance.getDateTime();
					// 是否已经发过离线告警信息
					boolean isLineAlarm = instance.isLineAlarm();
					// 是否已经发过断网告警信息
					boolean isConnectAlarm = instance.isConnectAlarm();
					// 判决时间
					DateTime judgeDateTime = new DateTime(dateTime).plusSeconds(thresholdSecond);
					if (judgeDateTime.isBeforeNow()) {
						// 已经断网 或者 已经离线
						if ((!instance.isOnConnect()) || (!instance.isOnline())) {
							continue;
						}
						// 判断网络是不是断了
						boolean ping = NetUtils.ping(instance.getIp());
						// 网络不通
						if (!ping) {
							// 断网
							instance.setOnConnect(false);
							instancePool.replace(key, instance);
							// 没发送断网告警
							if (!isConnectAlarm) {
								// 发送断网告警信息
								this.sendAlarmInfo("网络中断", AlarmLevelEnums.WARN, instance);
								instance.setConnectAlarm(true);
								instancePool.replace(key, instance);
							}
						} else {
							// 离线
							instance.setOnline(false);
							instancePool.replace(key, instance);
							// 没发送离线告警
							if (!isLineAlarm) {
								// 发送离线告警信息
								this.sendAlarmInfo("应用程序离线", AlarmLevelEnums.WARN, instance);
								instance.setLineAlarm(true);
								instancePool.replace(key, instance);
							}
						}
					} else {
						// 收到心跳包
						if (isConnectAlarm) {
							// 发送来网通知信息
							this.sendAlarmInfo("网络恢复", AlarmLevelEnums.INFO, instance);
							instance.setConnectAlarm(false);
							instancePool.replace(key, instance);
						}
						if (isLineAlarm) {
							// 发送在线通知信息
							this.sendAlarmInfo("应用程序在线", AlarmLevelEnums.INFO, instance);
							instance.setLineAlarm(false);
							instancePool.replace(key, instance);
						}
					}
				}
				// 打印当前应用池中的所有应用
				log.info("当前应用实例池大小：{}，详细信息：{}", this.instancePool.size(), this.instancePool.toJsonString());
			}, 30, 30, TimeUnit.SECONDS);
		});
		// 设置守护线程
		thread.setDaemon(true);
		// 开始执行分进程
		thread.start();
	}

	/**
	 * <p>
	 * 发送告警信息
	 * </p>
	 *
	 * @param title           告警标题
	 * @param alarmLevelEnums 告警级别
	 * @param instance        应用实例详情
	 * @author 皮锋
	 * @custom.date 2020/3/13 11:20
	 */
	private synchronized void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, Instance instance) {
		new Thread(() -> {
			String msg = "应用ID：" + instance.getInstanceId() + "，应用名称：" + instance.getInstanceName() + "，应用端点："
					+ instance.getEndpoint() + "，IP地址：" + instance.getIp();
			Alarm alarm = Alarm.builder().title(title)//
					.msg(msg)//
					.alarmLevel(alarmLevelEnums)//
					.build();
			AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
			alarmService.dealAlarmPackage(alarmPackage);
		}).start();
	}
}
