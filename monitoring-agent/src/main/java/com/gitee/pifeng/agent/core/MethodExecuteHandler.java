package com.gitee.pifeng.agent.core;

import com.gitee.pifeng.common.domain.Result;
import com.gitee.pifeng.common.dto.*;

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
        Invoker invoker = InvokerHolder.getInvoker(com.gitee.pifeng.agent.business.server.service.IHeartbeatService.class,
                "sendHeartbeatPackage");
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
        Invoker invoker = InvokerHolder.getInvoker(com.gitee.pifeng.agent.business.server.service.IAlarmService.class,
                "sendAlarmPackage");
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
        Invoker invoker = InvokerHolder.getInvoker(com.gitee.pifeng.agent.business.server.service.IServerService.class,
                "sendServerPackage");
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
        Invoker invoker = InvokerHolder.getInvoker(com.gitee.pifeng.agent.business.server.service.IJvmService.class,
                "sendJvmPackage");
        // 执行命令，返回执行结果
        return execute(invoker, jvmPackage);
    }

    /**
     * <p>
     * 执行方法，获取返回结果
     * </p>
     *
     * @param invoker 命令执行器-{@link Invoker}
     * @param pkg     传输包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020/3/11 22:27
     */
    private static BaseResponsePackage execute(Invoker invoker, Object pkg) {
        // 执行命令，返回执行结果
        BaseResponsePackage responsePackage;
        try {
            assert invoker != null;
            Object object = invoker.invoke(pkg);
            responsePackage = (BaseResponsePackage) object;
        } catch (Exception e) {
            Result result = Result.builder().isSuccess(false).msg(e.getMessage()).build();
            responsePackage = new PackageConstructor().structureBaseResponsePackage(result);
        }
        return responsePackage;
    }

}
