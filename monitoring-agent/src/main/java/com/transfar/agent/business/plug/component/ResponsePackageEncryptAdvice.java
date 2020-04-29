package com.transfar.agent.business.plug.component;

import com.alibaba.fastjson.JSONObject;
import com.transfar.common.dto.CiphertextPackage;
import com.transfar.common.util.DesEncryptUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
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
@RestControllerAdvice(basePackageClasses = {
        com.transfar.agent.business.plug.controller.AlarmController.class,
        com.transfar.agent.business.plug.controller.HeartbeatController.class,
        com.transfar.agent.business.plug.controller.ServerController.class})
public class ResponsePackageEncryptAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        // 总开关：是否启用响应包加密
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (null != body) {
            // 加密
            String encrypt = DesEncryptUtils.encrypt(JSONObject.toJSONString(body));
            return new CiphertextPackage(encrypt);
        }
        return null;
    }
}
