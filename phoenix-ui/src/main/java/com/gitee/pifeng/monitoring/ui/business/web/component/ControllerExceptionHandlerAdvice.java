package com.gitee.pifeng.monitoring.ui.business.web.component;

import com.gitee.pifeng.monitoring.common.web.util.AccessObjectUtils;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 监控UI端web业务的全局异常捕获
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 21:35
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.gitee.pifeng.monitoring.ui.business.web.controller")
public class ControllerExceptionHandlerAdvice {

    /**
     * <p>
     * 捕捉异常并进行处理
     * </p>
     *
     * @param throwable {@link Throwable}
     * @param request   {@link HttpServletRequest}
     * @return LayUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/4 21:54
     */
    @ExceptionHandler(value = Throwable.class)
    public LayUiAdminResultVo handler(Throwable throwable, HttpServletRequest request) {
        String throwableString = throwable.toString();
        String clientAddress = AccessObjectUtils.getClientAddress(request);
        log.error("请求客户端IP：{}，异常：{}", clientAddress, throwableString, throwable);
        return LayUiAdminResultVo.fail(throwable.getMessage());
    }

}
