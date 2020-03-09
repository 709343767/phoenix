package com.transfar.business.server.core;

import java.util.Date;

import org.hyperic.sigar.SigarException;

import com.transfar.constant.EndpointTypeConstants;
import com.transfar.domain.Alarm;
import com.transfar.dto.AlarmPackage;
import com.transfar.dto.HeartbeatPackage;
import com.transfar.dto.ServerPackage;
import com.transfar.inf.IPackageConstructor;
import com.transfar.util.InstanceUtils;
import com.transfar.util.StrUtils;

/**
 * <p>
 * 包构造器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月8日 下午3:31:11
 */
public class PackageConstructor implements IPackageConstructor {

	@Override
	public AlarmPackage structureAlarmPackage(Alarm alarm) {
		return null;
	}

	/**
	 * <p>
	 * 构建响应心跳数据包
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月7日 下午3:54:30
	 * @return HeartbeatPackage
	 */
	@Override
	public HeartbeatPackage structureHeartbeatPackage() {
		HeartbeatPackage heartbeatPackage = new HeartbeatPackage();
		heartbeatPackage.setId(StrUtils.getUUID());
		heartbeatPackage.setEndpoint(EndpointTypeConstants.SERVER);
		heartbeatPackage.setInstanceId(InstanceUtils.getInstanceId());
		heartbeatPackage.setInstanceName(InstanceUtils.getInstanceName());
		heartbeatPackage.setDateTime(new Date());
		heartbeatPackage.setResult(true);
		return heartbeatPackage;
	}

	@Override
	public ServerPackage structureServerPackage() throws SigarException {
		return null;
	}

}
