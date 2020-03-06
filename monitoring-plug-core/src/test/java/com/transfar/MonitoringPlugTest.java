package com.transfar;

import java.util.Date;

import com.transfar.constant.AlarmLevelConstant;
import com.transfar.constant.EndpointTypeConstant;
import com.transfar.core.MonitoringPlug;
import com.transfar.dto.AlarmPackage;

/**
 * <p>
 * 测试监控客户端
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午7:43:38
 */
public class MonitoringPlugTest {

	public static void main(String[] args) throws InterruptedException {
		MonitoringPlug.start();
		// 发送告警
		while (true) {
			AlarmPackage alarmPackage = new AlarmPackage();
			alarmPackage.setAlarmTime(new Date());
			alarmPackage.setEndpoint(EndpointTypeConstant.CLIENT);
			alarmPackage.setLevel(AlarmLevelConstant.WARN);
			alarmPackage.setMsg("测试");
			boolean result = MonitoringPlug.sendAlarm(alarmPackage);
			System.out.println("发送告警：" + result);
			Thread.sleep(60000);
		}

	}

}
