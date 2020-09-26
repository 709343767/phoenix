package com.imby.server.business.server.aop;

import com.alibaba.fastjson.JSON;
import com.imby.common.dto.HeartbeatPackage;
import com.imby.common.exception.NetException;
import com.imby.common.util.NetUtils;
import com.imby.server.business.server.core.NetPool;
import com.imby.server.business.server.domain.Net;
import com.imby.server.property.MonitoringServerWebProperties;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>
 * 获取网络信息的切面。
 * </p>
 * 获取IP地址，把IP地址加入到Spring容器中的网络信息池。
 *
 * @author 皮锋
 * @custom.date 2020/3/25 9:38
 */
@Aspect
@Component
public class NetAspect {

    /**
     * 网络信息池
     */
    @Autowired
    private NetPool netPool;

    /**
     * 监控配置属性
     */
    @Autowired
    private MonitoringServerWebProperties config;

    /**
     * <p>
     * 定义切入点，切入点为com.imby.server.business.server.controller.HeartbeatController.acceptHeartbeatPackage这一个方法。
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/2/22 17:56
     */
    @Pointcut("execution(public * com.imby.server.business.server.controller.HeartbeatController.acceptHeartbeatPackage(..))")
    public void tangentPoint() {
    }

    /**
     * <p>
     * 通过前置通知，获取IP地址，把IP地址加入到网络信息池。
     * </p>
     *
     * @param joinPoint 提供对连接点上可用状态和有关状态的静态信息的反射访问。
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/3/25 10:18
     */
    @Before("tangentPoint()")
    public void addIp2NetPool(JoinPoint joinPoint) throws NetException {
        String args = String.valueOf(joinPoint.getArgs()[0]);
        HeartbeatPackage heartbeatPackage = JSON.parseObject(args, HeartbeatPackage.class);
        // 请求包中的IP
        String packageIp = heartbeatPackage.getIp();
        // 网络信息
        Net net = new Net();
        net.setDateTime(new Date());
        net.setOnConnect(true);
        net.setConnectAlarm(this.netPool.get(packageIp) != null && this.netPool.get(packageIp).isConnectAlarm());
        net.setThresholdSecond((int) (heartbeatPackage.getRate() * config.getThreshold()));
        net.setIpSource(packageIp);
        net.setIpTarget(NetUtils.getLocalIp());
        net.setComputerName(heartbeatPackage.getComputerName());
        // 更新网络信息池
        this.netPool.updateNetPool(packageIp, net);
    }

}
