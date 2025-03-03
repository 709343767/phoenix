package com.gitee.pifeng.monitoring.server.business.server.component;

import com.gitee.pifeng.monitoring.common.web.core.http.HttpInputMessagePackageDecrypt;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.lang.reflect.Type;

/**
 * <p>
 * 请求包解密增强
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/28 15:30
 */
@RestControllerAdvice(basePackages = "com.gitee.pifeng.monitoring.server.business.server.controller")
public class RequestPackageDecryptAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        // 总开关：是否启用请求包解密
        return true;
    }

    @NonNull
    @Override
    public Object afterBodyRead(@NonNull Object body, @NonNull HttpInputMessage inputMessage, @NonNull MethodParameter parameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, @NonNull HttpInputMessage inputMessage, @NonNull MethodParameter parameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @NonNull
    @Override
    public HttpInputMessage beforeBodyRead(@NonNull HttpInputMessage inputMessage, @NonNull MethodParameter parameter, @NonNull Type targetType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            return new HttpInputMessagePackageDecrypt(inputMessage);
        } catch (Exception e) {
            return inputMessage;
        }
    }

}
