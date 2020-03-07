package com.transfar.business.core;

import com.transfar.business.dto.AgentResponseHeartbeatPackage;
import com.transfar.dto.AlarmPackage;
import com.transfar.dto.HeartbeatPackage;
import com.transfar.dto.ServerPackage;

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
	 * @author 皮锋
	 * @custom.date 2020年3月5日 上午11:01:46
	 * @param heartbeatPackage 心跳包
	 * @return HeartbeatPackage
	 */
	public static HeartbeatPackage sendHeartbeatPackage2Server(HeartbeatPackage heartbeatPackage) {
		// 通过命令执行器管理器，获取指定的命令执行器
		Invoker invoker = InvokerHolder.getInvoker(com.transfar.business.server.service.IHeartbeatService.class,
				"sendHeartbeatPackage");
		// 执行命令，返回执行结果
		HeartbeatPackage result;
		try {
			assert invoker != null;
			Object object = invoker.invoke(heartbeatPackage);
			result = (HeartbeatPackage) object;
		} catch (Exception e) {
			result = new AgentResponseHeartbeatPackage()//
					.setResult(false);
		}
		return result;
	}

	/**
	 * <p>
	 * 向服务端发送告警包
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月6日 下午3:23:56
	 * @param alarmPackage 告警包
	 * @return Boolean
	 */
	public static Boolean sendAlarmPackage2Server(AlarmPackage alarmPackage) {
		// 通过命令执行器管理器，获取指定的命令执行器
		Invoker invoker = InvokerHolder.getInvoker(com.transfar.business.server.service.IAlarmService.class,
				"sendAlarmPackage");
		// 执行命令，返回执行结果
		Boolean result;
		try {
			assert invoker != null;
			invoker.invoke(alarmPackage);
			result = true;
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/**
	 * <p>
	 * 向服务端发送服务器信息包
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月7日 下午5:18:32
	 * @param serverPackage 服务器信息包
	 * @return Boolean
	 */
	public static Boolean sendServerPackage2Server(ServerPackage serverPackage) {
		// 通过命令执行器管理器，获取指定的命令执行器
		Invoker invoker = InvokerHolder.getInvoker(com.transfar.business.server.service.IServerService.class,
				"sendServerPackage");
		// 执行命令，返回执行结果
		Boolean result;
		try {
			assert invoker != null;
			invoker.invoke(serverPackage);
			result = true;
		} catch (Exception e) {
			result = false;
		}
		return result;
	}
}
