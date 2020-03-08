package com.transfar.inf;

import org.hyperic.sigar.SigarException;

import com.transfar.domain.Alarm;
import com.transfar.dto.AlarmPackage;
import com.transfar.dto.HeartbeatPackage;
import com.transfar.dto.ServerPackage;

/**
 * <p>
 * 包构造器接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月8日 下午12:25:56
 */
public interface IPackageConstructor {

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
	AlarmPackage structureAlarmPackage(Alarm alarm);

	/**
	 * <p>
	 * 构建心跳数据包
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月7日 下午3:54:30
	 * @return HeartbeatPackage
	 */
	HeartbeatPackage structureHeartbeatPackage();

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
	ServerPackage structureServerPackage() throws SigarException;

}
