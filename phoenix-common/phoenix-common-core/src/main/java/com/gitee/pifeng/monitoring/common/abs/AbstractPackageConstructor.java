package com.gitee.pifeng.monitoring.common.abs;

import com.alibaba.fastjson.JSONObject;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.domain.Jvm;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.domain.Server;
import com.gitee.pifeng.monitoring.common.dto.*;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.inf.IPackageConstructor;

/**
 * <p>
 * 包构造器抽象类，提供对包构造器接口方法的默认实现。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/30 14:23
 */
public class AbstractPackageConstructor implements IPackageConstructor {

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
    @Override
    public AlarmPackage structureAlarmPackage(Alarm alarm) throws NetException {
        return null;
    }

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
    @Override
    public HeartbeatPackage structureHeartbeatPackage() throws NetException {
        return null;
    }

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
    @Override
    public ServerPackage structureServerPackage(Server server) throws NetException {
        return null;
    }

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
    @Override
    public JvmPackage structureJvmPackage(Jvm jvm) throws NetException {
        return null;
    }

    /**
     * <p>
     * 构建基础响应包
     * </p>
     *
     * @param result 返回结果
     * @return {@link BaseResponsePackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020年3月11日 上午9:52:48
     */
    @Override
    public BaseResponsePackage structureBaseResponsePackage(Result result) throws NetException {
        return null;
    }

    /**
     * <p>
     * 构建基础请求包
     * </p>
     *
     * @param extraMsg 附加信息
     * @return {@link BaseRequestPackage}
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2021/4/5 12:45
     */
    @Override
    public BaseRequestPackage structureBaseRequestPackage(JSONObject extraMsg) throws NetException {
        return null;
    }

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
    @Override
    public OfflinePackage structureOfflinePackage() throws NetException {
        return null;
    }

}
