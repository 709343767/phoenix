package com.transfar.inf;

import org.hyperic.sigar.SigarException;

import com.transfar.domain.Alarm;
import com.transfar.dto.AlarmPackage;
import com.transfar.dto.BaseResponsePackage;
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
     * @param alarm 告警信息
     * @return AlarmPackage
     * @author 皮锋
     * @custom.date 2020年3月7日 下午3:02:46
     */
    AlarmPackage structureAlarmPackage(Alarm alarm);

    /**
     * <p>
     * 构建心跳数据包
     * </p>
     *
     * @return HeartbeatPackage
     * @author 皮锋
     * @custom.date 2020年3月7日 下午3:54:30
     */
    HeartbeatPackage structureHeartbeatPackage();

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
    ServerPackage structureServerPackage() throws SigarException;

    /**
     * <p>
     * 构建请求成功的基础响应包
     * </p>
     *
     * @return BaseResponsePackage
     * @author 皮锋
     * @custom.date 2020年3月11日 上午9:52:37
     */
    BaseResponsePackage structureBaseResponsePackageBySuccess();

    /**
     * <p>
     * 构建请求失败的基础响应包
     * </p>
     *
     * @return BaseResponsePackage
     * @author 皮锋
     * @custom.date 2020年3月11日 上午9:52:48
     */
    BaseResponsePackage structureBaseResponsePackageByFail();

}