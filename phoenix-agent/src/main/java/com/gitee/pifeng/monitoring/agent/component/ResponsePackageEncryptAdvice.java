package com.gitee.pifeng.monitoring.agent.component;

import com.gitee.pifeng.monitoring.agent.core.AgentPackageConstructor;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.web.core.http.HttpOutputMessagePackageEncrypt;
import com.gitee.pifeng.monitoring.common.web.util.AccessObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 响应包加密增强
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/28 17:02
 */
@Slf4j
@RestControllerAdvice(basePackages = {
        "com.gitee.pifeng.monitoring.agent.business.client.controller",
        "com.gitee.pifeng.monitoring.agent.business.server.controller"
})
public class ResponsePackageEncryptAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 包构造器接口
     */
    @Autowired
    private AgentPackageConstructor agentPackageConstructor;

    /**
     * <p>
     * 捕捉异常并进行处理
     * </p>
     *
     * @param throwable {@link Throwable}
     * @param request   {@link HttpServletRequest}
     * @return 密文数据包 {@link CiphertextPackage}
     * @author 皮锋
     * @custom.date 2023/5/28 10:30
     */
    @ExceptionHandler(value = Throwable.class)
    public CiphertextPackage handler(Throwable throwable, HttpServletRequest request) {
        String throwableString = throwable.toString();
        String clientAddress = AccessObjectUtils.getClientAddress(request);
        log.error("请求客户端IP：{}，异常：{}", clientAddress, throwableString);
        Result build = Result.builder().isSuccess(false).msg(throwableString).build();
        BaseResponsePackage baseResponsePackage = this.agentPackageConstructor.structureBaseResponsePackage(build);
        return new HttpOutputMessagePackageEncrypt().encrypt(baseResponsePackage);
    }

    @Override
    public boolean supports(@NonNull MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // 总开关：是否启用响应包加密
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType,
                                  @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request,
                                  @NonNull ServerHttpResponse response) {
        if (null != body) {
            // 加密
            return new HttpOutputMessagePackageEncrypt().encrypt(body);
        }
        return null;
    }

}
