package com.imby.server.business.server.aop;

import com.alibaba.fastjson.JSON;
import com.imby.common.dto.HeartbeatPackage;
import com.imby.common.exception.NetException;
import com.imby.common.util.NetUtils;
import com.imby.server.business.server.controller.HeartbeatController;
import com.imby.server.business.server.core.InstancePool;
import com.imby.server.business.server.core.NetPool;
import com.imby.server.business.server.domain.Instance;
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
 * 处理应用实例信息和网络信息的切面。
 * </p>
 * 1.获取应用实例信息，并把应用实例信息添加或者更新到应用实例池;
 * 2.获取网络信息，并把网络信息添加或者更新到网络信息池。
 *
 * @author 皮锋
 * @custom.date 2020/5/12 11:07
 */
@Aspect
@Component
public class InstanceAndNetAspect {

    /**
     * 应用实例池
     */
    @Autowired
    private InstancePool instancePool;

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
     * 定义切入点，切入点为{@link HeartbeatController#acceptHeartbeatPackage(String)}这一个方法。
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
     * 处理前置通知。
     * </p>
     *
     * @param joinPoint 提供对连接点上可用状态和有关状态的静态信息的反射访问。
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/9/27 12:45
     */
    @Before("tangentPoint()")
    public void deal(JoinPoint joinPoint) throws NetException {
        String args = String.valueOf(joinPoint.getArgs()[0]);
        HeartbeatPackage heartbeatPackage = JSON.parseObject(args, HeartbeatPackage.class);
        this.operateInstancePool(heartbeatPackage);
        this.operateNetPool(heartbeatPackage);
    }

    /**
     * <p>
     * 获取应用实例信息，并把应用实例信息添加或者更新到应用实例池。
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @author 皮锋
     * @custom.date 2020/5/12 11:13
     */
    private void operateInstancePool(HeartbeatPackage heartbeatPackage) {
        // 把应用实例交给应用实例池管理
        String key = heartbeatPackage.getInstanceId();
        Instance instance = new Instance();
        // 实例本身信息
        instance.setEndpoint(heartbeatPackage.getEndpoint());
        instance.setInstanceId(key);
        instance.setInstanceName(heartbeatPackage.getInstanceName());
        instance.setIp(heartbeatPackage.getIp());
        instance.setComputerName(heartbeatPackage.getComputerName());
        instance.setInstanceDesc(heartbeatPackage.getInstanceDesc());
        // 实例状态信息
        instance.setOnline(true);
        instance.setOnConnect(true);
        instance.setDateTime(new Date());
        instance.setLineAlarm(this.instancePool.get(key) != null && this.instancePool.get(key).isLineAlarm());
        instance.setConnectAlarm(this.instancePool.get(key) != null && this.instancePool.get(key).isConnectAlarm());
        instance.setThresholdSecond((int) (heartbeatPackage.getRate() * this.config.getThreshold()));
        // 更新应用实例池
        this.instancePool.updateInstancePool(key, instance);
    }

    /**
     * <p>
     * 获取网络信息，并把网络信息添加或者更新到网络信息池。
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/3/25 10:18
     */
    private void operateNetPool(HeartbeatPackage heartbeatPackage) throws NetException {
        String key = heartbeatPackage.getIp();
        // 网络信息
        Net net = new Net();
        net.setDateTime(new Date());
        net.setOnConnect(true);
        net.setConnectAlarm(this.netPool.get(key) != null && this.netPool.get(key).isConnectAlarm());
        net.setThresholdSecond((int) (heartbeatPackage.getRate() * this.config.getThreshold()));
        net.setIpSource(key);
        net.setIpTarget(NetUtils.getLocalIp());
        net.setComputerName(heartbeatPackage.getComputerName());
        // 更新网络信息池
        this.netPool.updateNetPool(key, net);
    }

}
