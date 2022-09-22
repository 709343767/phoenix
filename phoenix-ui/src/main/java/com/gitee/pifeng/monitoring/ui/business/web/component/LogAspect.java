package com.gitee.pifeng.monitoring.ui.business.web.component;

import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.monitoring.common.constant.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.common.util.ExceptionUtils;
import com.gitee.pifeng.monitoring.common.util.MapUtils;
import com.gitee.pifeng.monitoring.common.web.util.AccessObjectUtils;
import com.gitee.pifeng.monitoring.common.web.util.ContextUtils;
import com.gitee.pifeng.monitoring.plug.Monitor;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogException;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogOperation;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLogExceptionService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLogOperationService;
import com.gitee.pifeng.monitoring.ui.util.SpringSecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 操作日志、异常日志切面
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/6/10 10:43
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    /**
     * 异常日志服务类
     */
    @Autowired
    private IMonitorLogExceptionService monitorLogExceptionService;

    /**
     * 操作日志服务类
     */
    @Autowired
    private IMonitorLogOperationService monitorLogOperationService;

    /**
     * <p>
     * 操作日志切入点
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/6/10 10:47
     */
    @Pointcut("@annotation(com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog)")
    public void operateLogPointCut() {
    }

    /**
     * <p>
     * 异常日志切入点
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/6/10 10:49
     */
    @Pointcut("execution(* com.gitee.pifeng.monitoring.ui.business.web.controller..*.*(..))")
    public void exceptionLogPointCut() {
    }

    /**
     * <p>
     * 记录用户操作日志
     * </p>
     *
     * @param joinPoint 切入点
     * @return 方法返回值
     * @author 皮锋
     * @custom.date 2021/6/10 10:55
     */
    @Around(value = "operateLogPointCut()")
    public Object saveOperateLog(ProceedingJoinPoint joinPoint) {
        HttpServletRequest request = ContextUtils.getRequest();
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的方法名
        String methodName = className + "#" + method.getName();
        // 操作日志
        MonitorLogOperation.MonitorLogOperationBuilder builder = MonitorLogOperation.builder();
        // 获取操作
        OperateLog operateLog = method.getAnnotation(OperateLog.class);
        if (operateLog != null) {
            String operateModule = operateLog.operModule();
            String operateType = operateLog.operType();
            String operateDesc = operateLog.operDesc();
            // 操作模块
            builder.operModule(operateModule);
            // 操作类型
            builder.operType(operateType);
            // 操作描述
            builder.operDesc(operateDesc);
        }
        // 转换请求参数
        Map<String, String> reqParamMap = MapUtils.convertParamMap(request.getParameterMap());
        builder.reqParam(reqParamMap.isEmpty() ? "" : JSON.toJSONString(reqParamMap));
        builder.userId(SpringSecurityUtils.getCurrentMonitorUserRealm() == null ? null : SpringSecurityUtils.getCurrentMonitorUserRealm().getId());
        builder.username(SpringSecurityUtils.getCurrentMonitorUserRealm() == null ? null : SpringSecurityUtils.getCurrentMonitorUserRealm().getUsrname());
        builder.operMethod(methodName);
        builder.uri(request.getRequestURI());
        builder.ip(AccessObjectUtils.getClientAddress(request));
        builder.insertTime(new Date());
        // 返回值
        Object response = null;
        try {
            response = joinPoint.proceed(joinPoint.getArgs());
            builder.respParam(response != null ? JSON.toJSONString(response) : "");
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
        }
        this.monitorLogOperationService.save(builder.build());
        return response;
    }

    /**
     * <p>
     * 异常返回通知，用于记录异常日志，发送告警，连接点抛出异常后执行
     * </p>
     *
     * @param joinPoint 切入点
     * @param e         异常信息
     * @author 皮锋
     * @custom.date 2021/6/10 10:58
     */
    @AfterThrowing(pointcut = "exceptionLogPointCut()", throwing = "e")
    public void saveExceptionLog(JoinPoint joinPoint, Throwable e) {
        HttpServletRequest request = ContextUtils.getRequest();
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
        // 转换请求参数
        Map<String, String> reqParamMap = MapUtils.convertParamMap(request.getParameterMap());
        builder.reqParam(reqParamMap.isEmpty() ? "" : JSON.toJSONString(reqParamMap));
        builder.excName(excName);
        builder.excMessage(ExceptionUtils.stackTraceToString(excName, e.getMessage(), e.getStackTrace()));
        builder.userId(SpringSecurityUtils.getCurrentMonitorUserRealm() == null ? null : SpringSecurityUtils.getCurrentMonitorUserRealm().getId());
        builder.username(SpringSecurityUtils.getCurrentMonitorUserRealm() == null ? null : SpringSecurityUtils.getCurrentMonitorUserRealm().getUsrname());
        builder.operMethod(methodName);
        builder.uri(request.getRequestURI());
        builder.ip(AccessObjectUtils.getClientAddress(request));
        builder.insertTime(new Date());
        MonitorLogException monitorLogException = builder.build();
        this.monitorLogExceptionService.save(monitorLogException);
        // 发送告警
        String msg = "请求参数：" + monitorLogException.getReqParam() +
                "，<br>异常名称：" + monitorLogException.getExcName() +
                "，<br>异常信息：" + monitorLogException.getExcMessage() +
                "，<br>操作用户：" + monitorLogException.getUsername() +
                "，<br>操作方法：" + monitorLogException.getOperMethod() +
                "，<br>请求URI：" + monitorLogException.getUri() +
                "，<br>请求IP：" + monitorLogException.getIp() +
                "，<br>时间：" + DateTimeUtils.dateToString(monitorLogException.getInsertTime());
        Alarm alarm = Alarm.builder()
                .alarmLevel(AlarmLevelEnums.ERROR)
                .monitorType(MonitorTypeEnums.INSTANCE)
                .charset(StandardCharsets.UTF_8)
                .title(excName)
                .msg(msg)
                .build();
        Monitor.sendAlarm(alarm);
    }

}
