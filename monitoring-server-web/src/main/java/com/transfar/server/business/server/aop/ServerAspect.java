package com.transfar.server.business.server.aop;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.transfar.common.dto.ServerPackage;
import com.transfar.server.inf.IServerMonitoringListener;

/**
 * <p>
 * 在服务器信息包处理完成之后通过切面，调用服务器信息监听器回调接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年4月1日 下午3:21:19
 */
@Aspect
@Component
public class ServerAspect {

	/**
	 * 服务器信息监听器
	 */
	@Autowired
	private List<IServerMonitoringListener> serverMonitoringListeners;

	/**
	 * <p>
	 * 定义切入点，切入点为com.transfar.server.business.server.controller.ServerController.acceptServerPackage这一个方法
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020/2/22 17:56
	 */
	@Pointcut("execution(public * com.transfar.server.business.server.controller.ServerController.acceptServerPackage(..))")
	public void tangentPoint() {
	}

	/**
	 * <p> 
	 * 通过后置通知，在服务器信息包处理完成之后通过切面，调用服务器信息监听器回调接口
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年4月1日 下午3:34:06
	 * @param joinPoint 提供对连接点上可用状态和有关状态的静态信息的反射访问
	 */
	@AfterReturning("tangentPoint()")
	public void wakeUp(JoinPoint joinPoint) {
		String args = String.valueOf(joinPoint.getArgs()[0]);
		ServerPackage serverPackage = JSON.parseObject(args, ServerPackage.class);
		// 请求包中的IP
		String ip = serverPackage.getIp();
		// 调用监听器回调接口
		this.serverMonitoringListeners.forEach(e -> e.wakeUp(ip));
	}

}
