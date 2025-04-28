package com.gitee.pifeng.monitoring.ui.business.web.component;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.common.util.ExceptionUtils;
import com.gitee.pifeng.monitoring.common.util.MapUtils;
import com.gitee.pifeng.monitoring.common.web.util.AccessObjectUtils;
import com.gitee.pifeng.monitoring.common.web.util.ContextUtils;
import com.gitee.pifeng.monitoring.plug.Monitor;
import com.gitee.pifeng.monitoring.plug.core.InstanceGenerator;
import com.gitee.pifeng.monitoring.ui.business.web.annotation.OperateLog;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorInstance;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogException;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogOperation;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorInstanceService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLogExceptionService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLogOperationService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.util.SpringSecurityUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
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
     * 应用实例服务类
     */
    @Autowired
    private IMonitorInstanceService monitorInstanceService;

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
     * @throws Throwable 异常
     * @author 皮锋
     * @custom.date 2021/6/10 10:55
     */
    @Around(value = "operateLogPointCut()")
    public Object saveOperateLog(ProceedingJoinPoint joinPoint) throws Throwable {
        // 计时器
        TimeInterval timer = DateUtil.timer();
        HttpServletRequest request = ContextUtils.getRequest();
        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        // 获取请求的类名
        String className = joinPoint.getTarget().getClass().getName();
        // 获取请求的方法名
        String methodName = className + "#" + method.getName();
        // 参数
        Object[] args = joinPoint.getArgs();
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
        Map<String, Object> reqParamMap = MapUtils.convertParamMap(request.getParameterMap());
        if (reqParamMap.isEmpty()) {
            if (ArrayUtils.isNotEmpty(args)) {
                // 遍历方法参数
                for (int i = 0; i < args.length; i++) {
                    Parameter[] parameters = method.getParameters();
                    String paramName = parameters[i].getName();
                    Object paramValue = args[i];
                    reqParamMap.put(paramName, paramValue != null ? JSON.toJSON(paramValue) : null);
                }
            }
        }
        builder.reqParam(reqParamMap.isEmpty() ? "" : JSON.toJSONString(reqParamMap));
        builder.userId(SpringSecurityUtils.getCurrentMonitorUserRealm() == null ? null : SpringSecurityUtils.getCurrentMonitorUserRealm().getId());
        builder.username(SpringSecurityUtils.getCurrentMonitorUserRealm() == null ? null : SpringSecurityUtils.getCurrentMonitorUserRealm().getUsrname());
        builder.operMethod(methodName);
        builder.uri(request.getMethod() + " " + request.getRequestURI());
        builder.ip(AccessObjectUtils.getClientAddress(request));
        builder.insertTime(new Date());
        // 返回值
        Object response = null;
        try {
            response = joinPoint.proceed(args);
        } catch (Throwable throwable) {
            log.error(throwable.getMessage(), throwable);
            response = LayUiAdminResultVo.fail(throwable.getMessage());
            // 在 ControllerExceptionHandlerAdvice 进行全局异常处理，返回的也是 LayUiAdminResultVo.fail(throwable.getMessage())
            throw throwable;
        } finally {
            builder.respParam(response != null ? JSON.toJSONString(response) : "");
            // 时间差（毫秒）
            String duration = timer.intervalPretty();
            builder.duration(duration);
            this.monitorLogOperationService.save(builder.build());
        }
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
        // 参数
        Object[] args = joinPoint.getArgs();
        // 构建异常日志
        MonitorLogException.MonitorLogExceptionBuilder builder = MonitorLogException.builder();
        // 转换请求参数
        Map<String, Object> reqParamMap = MapUtils.convertParamMap(request.getParameterMap());
        if (reqParamMap.isEmpty()) {
            if (ArrayUtils.isNotEmpty(args)) {
                // 遍历方法参数
                for (int i = 0; i < args.length; i++) {
                    Parameter[] parameters = method.getParameters();
                    String paramName = parameters[i].getName();
                    Object paramValue = args[i];
                    reqParamMap.put(paramName, paramValue != null ? JSON.toJSON(paramValue) : null);
                }
            }
        }
        builder.reqParam(reqParamMap.isEmpty() ? "" : JSON.toJSONString(reqParamMap));
        builder.excName(excName);
        builder.excMessage(ExceptionUtils.stackTraceToString(excName, e.getMessage(), e.getStackTrace()));
        builder.userId(SpringSecurityUtils.getCurrentMonitorUserRealm() == null ? null : SpringSecurityUtils.getCurrentMonitorUserRealm().getId());
        builder.username(SpringSecurityUtils.getCurrentMonitorUserRealm() == null ? null : SpringSecurityUtils.getCurrentMonitorUserRealm().getUsrname());
        builder.operMethod(methodName);
        builder.uri(request.getMethod() + " " + request.getRequestURI());
        builder.ip(AccessObjectUtils.getClientAddress(request));
        builder.instanceId(InstanceGenerator.getInstanceId());
        // 虽然设置的是发送告警，但是最终有没有发告警，此处保证不了呢
        builder.isAlarm(ZeroOrOneConstants.ONE);
        builder.insertTime(new Date());
        MonitorLogException monitorLogException = builder.build();
        this.monitorLogExceptionService.save(monitorLogException);
        // 根据应用ID查询应用信息
        LambdaQueryWrapper<MonitorInstance> monitorInstanceLambdaQueryWrapper = new LambdaQueryWrapper<>();
        monitorInstanceLambdaQueryWrapper.eq(MonitorInstance::getInstanceId, monitorLogException.getInstanceId());
        List<MonitorInstance> monitorInstances = this.monitorInstanceService.list(monitorInstanceLambdaQueryWrapper);
        if (CollectionUtils.isEmpty(monitorInstances)) {
            return;
        }
        MonitorInstance monitorInstance = monitorInstances.get(0);
        // 拼接告警消息
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("应用ID：").append(monitorInstance.getInstanceId())
                .append("，<br>应用名称：").append(monitorInstance.getInstanceName());
        // 应用实例描述
        String instanceDesc = monitorInstance.getInstanceDesc();
        if (StringUtils.isNotBlank(instanceDesc)) {
            // 应用实例摘要
            String instanceSummary = monitorInstance.getInstanceSummary();
            // 如果应用实例摘要不为空，则把摘要当做描述。因为：摘要是用户通过UI界面设置的，优先级比描述高
            if (StringUtils.isNotBlank(instanceSummary)) {
                msgBuilder.append("，<br>应用描述：").append(instanceSummary);
            } else {
                msgBuilder.append("，<br>应用描述：").append(instanceDesc);
            }
        }
        msgBuilder.append("，<br>异常名称：").append(monitorLogException.getExcName())
                .append("，<br>异常信息：").append(monitorLogException.getExcMessage())
                .append("，<br>操作用户：").append(monitorLogException.getUsername())
                .append("，<br>操作方法：").append(monitorLogException.getOperMethod())
                .append("，<br>请求参数：").append(monitorLogException.getReqParam())
                .append("，<br>请求URI：").append(monitorLogException.getUri())
                .append("，<br>请求IP：").append(monitorLogException.getIp())
                .append("，<br>时间：").append(DateTimeUtils.dateToString(monitorLogException.getInsertTime()));
        // 发送告警
        Alarm alarm = Alarm.builder()
                .alarmLevel(AlarmLevelEnums.ERROR)
                .alarmReason(AlarmReasonEnums.IGNORE)
                .monitorType(MonitorTypeEnums.INSTANCE)
                .charset(StandardCharsets.UTF_8)
                .title(excName)
                .msg(msgBuilder.toString())
                .build();
        Monitor.sendAlarm(alarm);
    }

}
