package com.gitee.pifeng.common.inf;

import com.gitee.pifeng.common.domain.Alarm;
import com.gitee.pifeng.common.domain.Result;
import com.gitee.pifeng.common.dto.*;
import com.gitee.pifeng.common.exception.NetException;
import org.hyperic.sigar.SigarException;

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
     * @return {@link AlarmPackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午3:02:46
     */
    AlarmPackage structureAlarmPackage(Alarm alarm) throws NetException;

    /**
     * <p>
     * 构建心跳数据包
     * </p>
     *
     * @return {@link HeartbeatPackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午3:54:30
     */
    HeartbeatPackage structureHeartbeatPackage() throws NetException;

    /**
     * <p>
     * 构建服务器数据包
     * </p>
     *
     * @return {@link ServerPackage}
     * @throws SigarException Sigar异常
     * @throws NetException   获取网络信息异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午4:51:51
     */
    ServerPackage structureServerPackage() throws SigarException, NetException;

    /**
     * <p>
     * 构建Java虚拟机信息包
     * </p>
     *
     * @return {@link JvmPackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/8/14 21:28
     */
    JvmPackage structureJvmPackage() throws NetException;

    /**
     * <p>
     * 构建请求基础响应包
     * </p>
     *
     * @param result 返回结果
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月11日 上午9:52:48
     */
    BaseResponsePackage structureBaseResponsePackage(Result result);

}
