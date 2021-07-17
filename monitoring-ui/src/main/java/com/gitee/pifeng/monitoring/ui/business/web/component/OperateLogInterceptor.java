package com.gitee.pifeng.monitoring.ui.business.web.component;

import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.monitoring.common.util.MapUtils;
import com.gitee.pifeng.monitoring.common.web.util.AccessObjectUtils;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorLogOperation;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorLogOperationService;
import com.gitee.pifeng.monitoring.ui.constant.OperateTypeConstants;
import com.gitee.pifeng.monitoring.ui.constant.UiModuleConstants;
import com.gitee.pifeng.monitoring.ui.util.SpringSecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

/**
 * <p>
 * 操作日志拦截器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/7/17 13:54
 */
@Component
public class OperateLogInterceptor implements HandlerInterceptor {

    /**
     * 操作日志服务类
     */
    @Autowired
    private IMonitorLogOperationService monitorLogOperationService;

    /**
     * <p>
     * 登录请求调用完成后，记录操作日志
     * </p>
     *
     * @param request  当前HTTP请求对象
     * @param response 当前HTTP响应对象
     * @param handler  {@link HandlerMethod} 封装由 method 和 bean 组成的处理程序方法的信息。提供对方法参数、方法返回值、方法注释等的访问
     * @param ex       异常对象
     * @author 皮锋
     * @custom.date 2021/7/17 14:22
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求URI
        String requestUri = request.getRequestURI();
        // 登录请求
        if (StringUtils.contains(requestUri, "/doLogin")) {
            // 获取请求的类名
            String className = ((HandlerMethod) handler).getBean().getClass().getName();
            // 获取切入点所在的方法
            Method method = ((HandlerMethod) handler).getMethod();
            // 获取请求的方法名
            String methodName = className + "#" + method.getName();
            // 操作日志
            MonitorLogOperation.MonitorLogOperationBuilder builder = MonitorLogOperation.builder();
            // 操作模块
            builder.operModule(UiModuleConstants.LOGIN);
            // 操作类型
            builder.operType(OperateTypeConstants.LOGIN);
            // 操作描述
            builder.operDesc("用户登录");
            // 转换请求参数
            Map<String, String> reqParamMap = MapUtils.convertParamMap(request.getParameterMap());
            builder.reqParam(reqParamMap.isEmpty() ? null : JSON.toJSONString(reqParamMap));
            builder.respParam(JSON.toJSONString(response));
            builder.userId(SpringSecurityUtils.getCurrentMonitorUserRealm().getId());
            builder.username(SpringSecurityUtils.getCurrentMonitorUserRealm().getUsrname());
            builder.operMethod(methodName);
            builder.uri(requestUri);
            builder.ip(AccessObjectUtils.getClientAddress(request));
            builder.insertTime(new Date());
            this.monitorLogOperationService.save(builder.build());
        }
    }

}
