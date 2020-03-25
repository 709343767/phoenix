package com.transfar.server.business.server.aop;

import com.alibaba.fastjson.JSON;
import com.transfar.common.dto.HeartbeatPackage;
import com.transfar.server.business.server.core.IpPool;
import com.transfar.server.business.server.domain.Ip;
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
 * 获取IP地址的切面
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/25 9:38
 */
@Aspect
@Component
public class IpAspect {

    /**
     * IP池，维护哪些IP可通，哪些IP不通
     */
    @Autowired
    private IpPool ipPool;

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
     * 通过前置通知，获取IP地址，加入到IP地址池
     * </p>
     *
     * @param joinPoint 提供对连接点上可用状态和有关状态的静态信息的反射访问
     * @author 皮锋
     * @custom.date 2020/3/25 10:18
     */
    @Before("tangentPoint()")
    public void addIp2IpPool(JoinPoint joinPoint) {
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
            // 根据IP更新IP池
            this.updateIpPool(requestIp, heartbeatPackage);
        }
        // 请求包中的IP
        String packageIp = heartbeatPackage.getId();
        // 根据IP更新IP池
        this.updateIpPool(packageIp, heartbeatPackage);
    }

    /**
     * <p>
     * 根据IP更新IP池
     * </p>
     *
     * @param ip               IP地址
     * @param heartbeatPackage 心跳包
     * @author 皮锋
     * @custom.date 2020/3/25 12:28
     */
    private void updateIpPool(String ip, HeartbeatPackage heartbeatPackage) {
        Ip poolIpObject = ipPool.get(ip);
        // IP池中没有当前IP，添加
        if (poolIpObject == null) {
            Ip ipObject = new Ip();
            ipObject.setDateTime(heartbeatPackage.getDateTime());
            ipObject.setOnConnect(true);
            ipObject.setThresholdSecond((int) (heartbeatPackage.getRate() * config.getThreshold()));
            this.ipPool.put(ip, ipObject);
        }
        // IP池中有当前IP，替换
        else {
            poolIpObject.setDateTime(heartbeatPackage.getDateTime());
            poolIpObject.setOnConnect(true);
            poolIpObject.setThresholdSecond((int) (heartbeatPackage.getRate() * config.getThreshold()));
            this.ipPool.replace(ip, poolIpObject);
        }
    }

}
