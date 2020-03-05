package com.transfar.business.core;

import java.util.Date;

import com.transfar.business.dto.AgentResponseHeartbeatPackage;
import com.transfar.dto.HeartbeatPackage;

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
					.setResult(false)//
					.setDateTime(new Date());
		}
		return result;
	}
}
