package com.gitee.pifeng.monitoring.agent.component;

import com.gitee.pifeng.monitoring.common.web.core.http.HttpInputMessagePackageDecrypt;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
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
@RestControllerAdvice(basePackages = {
        "com.gitee.pifeng.monitoring.agent.business.client.controller",
        "com.gitee.pifeng.monitoring.agent.business.server.controller"
})
public class RequestPackageDecryptAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 总开关：是否启用请求包解密
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        try {
            return new HttpInputMessagePackageDecrypt(inputMessage);
        } catch (Exception e) {
            return inputMessage;
        }
    }

}
