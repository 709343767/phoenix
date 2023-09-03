package com.gitee.pifeng.monitoring.agent.core;

import com.gitee.pifeng.monitoring.agent.business.server.service.*;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.*;
import com.gitee.pifeng.monitoring.common.web.core.Invoker;
import com.gitee.pifeng.monitoring.common.web.core.InvokerHolder;

/**
 * <p>
 * 方法执行助手
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 上午10:59:29
 */
public class MethodExecuteHandler {

    /**
     * 代理端包构造器
     */
    private static final AgentPackageConstructor AGENT_PACKAGE_CONSTRUCTOR = AgentPackageConstructor.getInstance();

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/27 13:26
     */
    private MethodExecuteHandler() {
    }

    /**
     * <p>
     * 向服务端发送心跳包
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月5日 上午11:01:46
     */
    public static BaseResponsePackage sendHeartbeatPackage2Server(HeartbeatPackage heartbeatPackage) {
        // 通过命令执行器管理器，获取指定的命令执行器
        Invoker invoker = InvokerHolder.getInvoker(IHeartbeatService.class, "sendHeartbeatPackage");
        // 执行命令，返回执行结果
        return execute(invoker, heartbeatPackage);
    }

    /**
     * <p>
     * 向服务端发送告警包
     * </p>
     *
     * @param alarmPackage 告警包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月6日 下午3:23:56
     */
    public static BaseResponsePackage sendAlarmPackage2Server(AlarmPackage alarmPackage) {
        // 通过命令执行器管理器，获取指定的命令执行器
        Invoker invoker = InvokerHolder.getInvoker(IAlarmService.class, "sendAlarmPackage");
        // 执行命令，返回执行结果
        return execute(invoker, alarmPackage);
    }

    /**
     * <p>
     * 向服务端发送服务器信息包
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月7日 下午5:18:32
     */
    public static BaseResponsePackage sendServerPackage2Server(ServerPackage serverPackage) {
        // 通过命令执行器管理器，获取指定的命令执行器
        Invoker invoker = InvokerHolder.getInvoker(IServerService.class, "sendServerPackage");
        // 执行命令，返回执行结果
        return execute(invoker, serverPackage);
    }

    /**
     * <p>
     * 向服务端发送Java虚拟机信息包
     * </p>
     *
     * @param jvmPackage Java虚拟机信息包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020/8/15 22:09
     */
    public static BaseResponsePackage sendJvmPackage2Server(JvmPackage jvmPackage) {
        // 通过命令执行器管理器，获取指定的命令执行器
        Invoker invoker = InvokerHolder.getInvoker(IJvmService.class, "sendJvmPackage");
        // 执行命令，返回执行结果
        return execute(invoker, jvmPackage);
    }

    /**
     * <p>
     * 向服务端发送下线信息包
     * </p>
     *
     * @param offlinePackage 下线信息包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2023/7/23 9:54
     */
    public static BaseResponsePackage sendOfflinePackage2Server(OfflinePackage offlinePackage) {
        // 通过命令执行器管理器，获取指定的命令执行器
        Invoker invoker = InvokerHolder.getInvoker(IOfflineService.class, "sendOfflinePackage");
        // 执行命令，返回执行结果
        return execute(invoker, offlinePackage);
    }

    /**
     * <p>
     * 向服务端发送基础请求包
     * </p>
     *
     * @param baseRequestPackage 基础请求包
     * @param url                URL路径
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2021/4/5 14:44
     */
    public static BaseResponsePackage sendBaseRequestPackage2Server(BaseRequestPackage baseRequestPackage, String url) {
        // 通过命令执行器管理器，获取指定的命令执行器
        Invoker invoker = InvokerHolder.getInvoker(IBaseRequestPackageService.class, "sendBaseRequestPackage");
        // 执行命令，返回执行结果
        return execute(invoker, baseRequestPackage, url);
    }

    /**
     * <p>
     * 执行方法，获取返回结果
     * </p>
     *
     * @param invoker 命令执行器-{@link Invoker}
     * @param objects 数据
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020/3/11 22:27
     */
    private static BaseResponsePackage execute(Invoker invoker, Object... objects) {
        // 执行命令，返回执行结果
        BaseResponsePackage responsePackage;
        try {
            assert invoker != null;
            Object object = invoker.invoke(objects);
            responsePackage = (BaseResponsePackage) object;
        } catch (Exception e) {
            Result result = Result.builder().isSuccess(false).msg(e.getMessage()).build();
            responsePackage = AGENT_PACKAGE_CONSTRUCTOR.structureBaseResponsePackage(result);
        }
        return responsePackage;
    }

}
