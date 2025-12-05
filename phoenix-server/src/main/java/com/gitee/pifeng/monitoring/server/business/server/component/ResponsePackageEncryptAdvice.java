package com.gitee.pifeng.monitoring.server.business.server.component;

import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.web.core.http.HttpOutputMessagePackageEncrypt;
import com.gitee.pifeng.monitoring.common.web.util.AccessObjectUtils;
import com.gitee.pifeng.monitoring.server.business.server.core.ServerPackageConstructor;
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
@RestControllerAdvice(basePackages = "com.gitee.pifeng.monitoring.server.business.server.controller")
@Slf4j
public class ResponsePackageEncryptAdvice implements ResponseBodyAdvice<Object> {

    /**
     * 服务端包构造器
     */
    @Autowired
    private ServerPackageConstructor serverPackageConstructor;

    /**
     * <p>
     * 捕捉异常并进行处理
     * </p>
     *
     * @param throwable {@link Throwable}
     * @param request   {@link HttpServletRequest}
     * @return 密文数据包 {@link CiphertextPackage}
     * @author 皮锋
     * @custom.date 2020/9/4 21:54
     */
    @ExceptionHandler(value = Throwable.class)
    public CiphertextPackage handler(Throwable throwable, HttpServletRequest request) {
        String throwableString = throwable.toString();
        String clientAddress = AccessObjectUtils.getClientAddress(request);
        String uri = request.getRequestURI();
        log.error("请求客户端IP：{}，URI：{}，异常：{}", clientAddress, uri, throwableString);
        Result build = Result.builder().isSuccess(false).msg(throwableString).build();
        BaseResponsePackage baseResponsePackage = this.serverPackageConstructor.structureBaseResponsePackage(build);
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
