package com.transfar.server.business.server.aop;

import com.alibaba.fastjson.JSON;
import com.transfar.common.dto.HeartbeatPackage;
import com.transfar.server.business.server.core.NetPool;
import com.transfar.server.business.server.domain.Net;
import com.transfar.server.property.MonitoringServerWebProperties;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 获取网络信息的切面
 * </p>
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
     * 定义切入点，切入点为com.transfar.server.business.server.controller.HeartbeatController.acceptHeartbeatPackage这一个方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/2/22 17:56
     */
    @Pointcut("execution(public * com.transfar.server.business.server.controller.HeartbeatController.acceptHeartbeatPackage(..))")
    public void tangentPoint() {
    }

    /**
     * <p>
     * 通过前置通知，获取IP地址，把IP地址加入到网络信息池
     * </p>
     *
     * @param joinPoint 提供对连接点上可用状态和有关状态的静态信息的反射访问
     * @author 皮锋
     * @custom.date 2020/3/25 10:18
     */
    @Before("tangentPoint()")
    public void addIp2NetPool(JoinPoint joinPoint) {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String args = String.valueOf(joinPoint.getArgs()[0]);
        HeartbeatPackage heartbeatPackage = JSON.parseObject(args, HeartbeatPackage.class);
        // 请求地址中的IP
        String requestIp = request.getRemoteAddr();
        // 不是本机地址
        if (!StringUtils.equals("127.0.0.1", requestIp)) {
            // 更新网络信息池
            this.updateNetPool(requestIp, heartbeatPackage);
        }
        // 请求包中的IP
        String packageIp = heartbeatPackage.getIp();
        if (!StringUtils.equals(packageIp, requestIp)) {
            // 更新网络信息池
            this.updateNetPool(packageIp, heartbeatPackage);
        }
    }

    /**
     * <p>
     * 更新网络信息池
     * </p>
     *
     * @param ip               IP地址
     * @param heartbeatPackage 心跳包
     * @author 皮锋
     * @custom.date 2020/3/25 12:28
     */
    private void updateNetPool(String ip, HeartbeatPackage heartbeatPackage) {
        Net poolNet = netPool.get(ip);
        // 网络信息池中没有当前IP，添加
        if (poolNet == null) {
            Net net = new Net();
            net.setDateTime(heartbeatPackage.getDateTime());
            net.setOnConnect(true);
            net.setThresholdSecond((int) (heartbeatPackage.getRate() * config.getThreshold()));
            net.setIp(ip);
            this.netPool.put(ip, net);
        }
        // 网络信息池中有当前IP，替换
        else {
            poolNet.setDateTime(heartbeatPackage.getDateTime());
            poolNet.setOnConnect(true);
            poolNet.setThresholdSecond((int) (heartbeatPackage.getRate() * config.getThreshold()));
            poolNet.setIp(ip);
            this.netPool.replace(ip, poolNet);
        }
    }

}
