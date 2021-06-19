package com.gitee.pifeng.monitoring.server.business.server.component;

import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.monitoring.common.web.util.AccessObjectUtil;
import com.gitee.pifeng.monitoring.common.web.util.ContextUtils;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorLogException;
import com.gitee.pifeng.monitoring.server.business.server.service.ILogExceptionService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 异常日志切面
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/6/10 10:43
 */
@Aspect
@Component
public class ExceptionLogAspect {

    /**
     * 异常日志服务层接口
     */
    @Autowired
    private ILogExceptionService logExceptionService;

    /**
     * <p>
     * 异常日志切入点
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/6/10 10:49
     */
    @Pointcut("execution(* com.gitee.pifeng.monitoring.server.business.server..*.*(..))")
    public void exceptionLogPointCut() {
    }

    /**
     * <p>
     * 异常返回通知，用于记录异常日志，连接点抛出异常后执行
     * </p>
     *
     * @param joinPoint 切入点
     * @param e         异常信息
     * @author 皮锋
     * @custom.date 2021/6/10 10:58
     */
    @AfterThrowing(pointcut = "exceptionLogPointCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request;
        try {
            // 如果是http请求，则有请求对象
            request = ContextUtils.getRequest();
        } catch (Exception e1) {
            request = null;
        }
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的方法名
        String methodName = className + "#" + method.getName();
        // 异常名称
        String excName = e.getClass().getName();
        // 构建异常日志
        MonitorLogException.MonitorLogExceptionBuilder builder = MonitorLogException.builder();
        builder.reqParam(JSON.toJSONString(request != null ? this.convertParamMap(request.getParameterMap()) : null));
        builder.excName(excName);
        builder.excMessage(this.stackTraceToString(excName, e.getMessage(), e.getStackTrace()));
        builder.userId(-1L);
        builder.username("“monitoring-server”服务");
        builder.operMethod(methodName);
        builder.uri(request != null ? request.getRequestURI() : null);
        builder.ip(request != null ? AccessObjectUtil.getClientAddress(request) : null);
        builder.insertTime(new Date());
        MonitorLogException monitorLogException = builder.build();
        this.logExceptionService.save(monitorLogException);
    }

    /**
     * <p>
     * 转换请求参数
     * </p>
     *
     * @param paramMap 请求参数
     * @return {@link Map}
     * @author 皮锋
     * @custom.date 2021/6/10 13:45
     */
    private Map<String, String> convertParamMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<>(16);
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * <p>
     * 转换异常信息为字符串
     * </p>
     *
     * @param exceptionName    异常名称
     * @param exceptionMessage 异常信息
     * @param elements         堆栈信息
     * @return 异常信息字符串
     * @author 皮锋
     * @custom.date 2021/6/11 11:04
     */
    private String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
        StringBuilder builder = new StringBuilder();
        for (StackTraceElement stet : elements) {
            builder.append(stet).append("\n");
        }
        return exceptionName + ":" + exceptionMessage + "\n" + builder.toString();
    }

}
