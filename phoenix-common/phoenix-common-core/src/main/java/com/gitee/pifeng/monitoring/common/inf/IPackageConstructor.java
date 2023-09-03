package com.gitee.pifeng.monitoring.common.inf;

import com.alibaba.fastjson.JSONObject;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.domain.Jvm;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.domain.Server;
import com.gitee.pifeng.monitoring.common.dto.*;
import com.gitee.pifeng.monitoring.common.exception.NetException;

/**
 * <p>
 * 包构造器接口
 * </p>
 *
 * <strong>使用“抽象工厂设计模式”的思想去构建数据包，不同的监控端点实现不同的包构造器去构建符合自己需求的数据包，
 * 使用者无需关心数据包是如何构建的，只需通过包构建器直接获取构建好的数据包即可。</strong>
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
     * @param server 服务器信息
     * @return {@link ServerPackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午4:51:51
     */
    ServerPackage structureServerPackage(Server server) throws NetException;

    /**
     * <p>
     * 构建Java虚拟机信息包
     * </p>
     *
     * @param jvm Java虚拟机信息
     * @return {@link JvmPackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/8/14 21:28
     */
    JvmPackage structureJvmPackage(Jvm jvm) throws NetException;

    /**
     * <p>
     * 构建请求基础响应包
     * </p>
     *
     * @param result 返回结果
     * @return {@link BaseResponsePackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020年3月11日 上午9:52:48
     */
    BaseResponsePackage structureBaseResponsePackage(Result result) throws NetException;

    /**
     * <p>
     * 构建基础请求包
     * </p>
     *
     * @param extraMsg 附加信息
     * @return {@link BaseRequestPackage}
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2021/4/5 12:45
     */
    BaseRequestPackage structureBaseRequestPackage(JSONObject extraMsg) throws NetException;

    /**
     * <p>
     * 构建下线信息包
     * </p>
     *
     * @return {@link OfflinePackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2023/5/30 13:35
     */
    OfflinePackage structureOfflinePackage() throws NetException;

}
