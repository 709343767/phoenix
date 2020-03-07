package com.transfar.core;

import java.util.Date;

import org.hyperic.sigar.SigarException;

import com.transfar.constant.EndpointTypeConstant;
import com.transfar.domain.Alarm;
import com.transfar.domain.server.ServerDomain;
import com.transfar.dto.AlarmPackage;
import com.transfar.dto.HeartbeatPackage;
import com.transfar.dto.ServerPackage;
import com.transfar.util.InstanceUtils;
import com.transfar.util.SigarUtils;

/**
 * <p>
 * 包构造器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午2:52:50
 */
public class PackageConstructor {

	/**
	 * <p>
	 * 构造告警数据包
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月7日 下午3:02:46
	 * @param alarm 告警信息
	 * @return AlarmPackage
	 */
	public static AlarmPackage structureAlarmPackage(Alarm alarm) {
		AlarmPackage alarmPackage = new AlarmPackage();
		alarmPackage.setAlarmTime(new Date());
		alarmPackage.setEndpoint(EndpointTypeConstant.CLIENT);
		alarmPackage.setInstanceId(InstanceUtils.getInstanceId());
		alarmPackage.setInstanceName(InstanceUtils.getInstanceName());
		alarmPackage.setLevel(alarm.getAlarmLevel().name());
		alarmPackage.setMsg(alarm.getMsg());
		return alarmPackage;
	}

	/**
	 * <p>
	 * 构建心跳数据包
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月7日 下午3:54:30
	 * @return HeartbeatPackage
	 */
	public static HeartbeatPackage structureHeartbeatPackage() {
		HeartbeatPackage heartbeatPackage = new HeartbeatPackage();
		heartbeatPackage.setEndpoint(EndpointTypeConstant.CLIENT);
		heartbeatPackage.setInstanceId(InstanceUtils.getInstanceId());
		heartbeatPackage.setInstanceName(InstanceUtils.getInstanceName());
		heartbeatPackage.setDateTime(new Date());
		return heartbeatPackage;
	}

	/**
	 * <p>
	 * 构建服务器数据包
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月7日 下午4:51:51
	 * @return ServerPackage
	 * @throws SigarException Sigar异常
	 */
	public static ServerPackage structureServerPackage() throws SigarException {
		ServerPackage serverPackage = new ServerPackage();
		serverPackage.setDateTime(new Date());
		serverPackage.setEndpoint(EndpointTypeConstant.CLIENT);
		serverPackage.setInstanceId(InstanceUtils.getInstanceId());
		serverPackage.setInstanceName(InstanceUtils.getInstanceName());
		ServerDomain serverDomain = SigarUtils.getServerInfo();
		serverPackage.setServerDomain(serverDomain);
		return serverPackage;
	}

}
