package com.imby.agent.business.plug.component;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.imby.common.dto.CiphertextPackage;
import com.imby.common.util.DesEncryptUtils;

import org.apache.commons.io.IOUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.InputStream;
import java.lang.reflect.Type;

/**
 * <p>
 * 请求包解密增强
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/28 15:30
 */
@RestControllerAdvice(basePackageClasses = {
        com.imby.agent.business.plug.controller.AlarmController.class,
        com.imby.agent.business.plug.controller.HeartbeatController.class,
        com.imby.agent.business.plug.controller.ServerController.class})
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
            return new MyHttpInputMessage(inputMessage);
        } catch (Exception e) {
            return inputMessage;
        }
    }

    static class MyHttpInputMessage implements HttpInputMessage {

        private final HttpHeaders headers;

        private final InputStream body;

        public MyHttpInputMessage(HttpInputMessage inputMessage) throws Exception {

            this.headers = inputMessage.getHeaders();

            String bodyStr = IOUtils.toString(inputMessage.getBody(), Charsets.UTF_8);
            // 密文数据包
            CiphertextPackage ciphertextPackage = JSONObject.parseObject(bodyStr, CiphertextPackage.class);

            // 解密
            String decryptStr = DesEncryptUtils.decrypt(ciphertextPackage.getCiphertext());

            this.body = IOUtils.toInputStream(decryptStr, Charsets.UTF_8);
        }

        @Override
        public InputStream getBody() {
            return this.body;
        }

        @Override
        public HttpHeaders getHeaders() {
            return this.headers;
        }
    }
}
