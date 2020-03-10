package com.transfar.core;

import java.nio.charset.Charset;
import java.util.Date;

import org.hyperic.sigar.SigarException;

import com.google.common.base.Charsets;
import com.transfar.constant.EndpointTypeConstants;
import com.transfar.domain.Alarm;
import com.transfar.domain.server.ServerDomain;
import com.transfar.dto.AlarmPackage;
import com.transfar.dto.HeartbeatPackage;
import com.transfar.dto.ServerPackage;
import com.transfar.inf.IPackageConstructor;
import com.transfar.util.InstanceUtils;
import com.transfar.util.SigarUtils;
import com.transfar.util.StrUtils;

/**
 * <p>
 * 包构造器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午2:52:50
 */
public class PackageConstructor implements IPackageConstructor {

	/**
	 * <p>
	 * 构造告警数据包
	 * </p>
	 *
	 * @param alarm 告警信息
	 * @return AlarmPackage
	 * @author 皮锋
	 * @custom.date 2020年3月7日 下午3:02:46
	 */
	@Override
	public AlarmPackage structureAlarmPackage(Alarm alarm) {
		AlarmPackage alarmPackage = new AlarmPackage();
		alarmPackage.setId(StrUtils.getUUID());
		alarmPackage.setAlarmTime(new Date());
		alarmPackage.setEndpoint(EndpointTypeConstants.CLIENT);
		alarmPackage.setInstanceId(InstanceUtils.getInstanceId());
		alarmPackage.setInstanceName(InstanceUtils.getInstanceName());
		alarmPackage.setLevel(alarm.getAlarmLevel().name());
		alarmPackage.setTest(alarm.isTest());
		Charset charset = alarm.getCharset();
		// 设置了字符集
		if (null != charset) {
			alarmPackage.setMsg(new String(alarm.getMsg().getBytes(Charsets.UTF_8), Charsets.UTF_8));
		} else {
			alarmPackage.setMsg(alarm.getMsg());
		}
		return alarmPackage;
	}

	/**
	 * <p>
	 * 构建心跳数据包
	 * </p>
	 *
	 * @return HeartbeatPackage
	 * @author 皮锋
	 * @custom.date 2020年3月7日 下午3:54:30
	 */
	@Override
	public HeartbeatPackage structureHeartbeatPackage() {
		HeartbeatPackage heartbeatPackage = new HeartbeatPackage();
		heartbeatPackage.setId(StrUtils.getUUID());
		heartbeatPackage.setEndpoint(EndpointTypeConstants.CLIENT);
		heartbeatPackage.setInstanceId(InstanceUtils.getInstanceId());
		heartbeatPackage.setInstanceName(InstanceUtils.getInstanceName());
		heartbeatPackage.setDateTime(new Date());
		heartbeatPackage.setRate(ConfigLoader.monitoringProperties.getHeartbeatProperties().getRate());
		return heartbeatPackage;
	}

	/**
	 * <p>
	 * 构建服务器数据包
	 * </p>
	 *
	 * @return ServerPackage
	 * @throws SigarException Sigar异常
	 * @author 皮锋
	 * @custom.date 2020年3月7日 下午4:51:51
	 */
	@Override
	public ServerPackage structureServerPackage() throws SigarException {
		ServerPackage serverPackage = new ServerPackage();
		serverPackage.setId(StrUtils.getUUID());
		serverPackage.setDateTime(new Date());
		serverPackage.setEndpoint(EndpointTypeConstants.CLIENT);
		serverPackage.setInstanceId(InstanceUtils.getInstanceId());
		serverPackage.setInstanceName(InstanceUtils.getInstanceName());
		ServerDomain serverDomain = SigarUtils.getServerInfo();
		serverPackage.setServerDomain(serverDomain);
		return serverPackage;
	}

}
