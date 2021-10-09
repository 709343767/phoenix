package com.gitee.pifeng.monitoring.server.business.server.component;

import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.web.toolkit.HttpInputMessagePackageEncrypt;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

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
     * <p>
     * 捕捉异常并进行处理
     * </p>
     *
     * @param throwable {@link Throwable}
     * @return 密文数据包 {@link CiphertextPackage}
     * @author 皮锋
     * @custom.date 2020/9/4 21:54
     */
    @ExceptionHandler(value = Throwable.class)
    public CiphertextPackage handler(Throwable throwable) {
        log.error("异常：", throwable);
        Result build = Result.builder().isSuccess(false).msg(throwable.toString()).build();
        BaseResponsePackage baseResponsePackage = new PackageConstructor().structureBaseResponsePackage(build);
        return new HttpInputMessagePackageEncrypt().encrypt(baseResponsePackage);
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 总开关：是否启用响应包加密
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (null != body) {
            // 加密
            return new HttpInputMessagePackageEncrypt().encrypt(body);
        }
        return null;
    }

}
