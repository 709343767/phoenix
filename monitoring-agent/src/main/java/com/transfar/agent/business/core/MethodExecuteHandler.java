package com.transfar.agent.business.core;

import com.transfar.common.dto.AlarmPackage;
import com.transfar.common.dto.BaseResponsePackage;
import com.transfar.common.dto.HeartbeatPackage;
import com.transfar.common.dto.ServerPackage;

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
        Invoker invoker = InvokerHolder.getInvoker(com.transfar.agent.business.server.service.IHeartbeatService.class,
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
        Invoker invoker = InvokerHolder.getInvoker(com.transfar.agent.business.server.service.IAlarmService.class,
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
        Invoker invoker = InvokerHolder.getInvoker(com.transfar.agent.business.server.service.IServerService.class,
                "sendServerPackage");
        // 执行命令，返回执行结果
        return execute(invoker, serverPackage);
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
        BaseResponsePackage result;
        try {
            assert invoker != null;
            Object object = invoker.invoke(pkg);
            result = (BaseResponsePackage) object;
        } catch (Exception e) {
            result = new PackageConstructor().structureBaseResponsePackageByFail(e.getMessage());
        }
        return result;
    }

}
