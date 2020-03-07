package com.transfar;

import com.transfar.constant.AlarmLevelEnum;
import com.transfar.domain.Alarm;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 测试监控客户端
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午7:43:38
 */
@Slf4j
public class MonitorTest {

	public static void main(String[] args) throws InterruptedException {
		Monitor.start();
		// 发送告警
		while (true) {
			Alarm alarm = new Alarm();
			alarm.setAlarmLevel(AlarmLevelEnum.WARN);
			alarm.setMsg("测试");
			boolean result = Monitor.sendAlarm(alarm);
			log.info("发送告警：{}", result);
			Thread.sleep(60000);
		}

	}

}
