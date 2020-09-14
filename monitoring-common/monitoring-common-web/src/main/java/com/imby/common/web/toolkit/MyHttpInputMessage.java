package com.imby.common.web.toolkit;

import com.alibaba.fastjson.JSON;
import com.imby.common.dto.CiphertextPackage;
import com.imby.common.util.DesEncryptUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p>
 * 处理HTTP输入消息：获取密文数据包，并且解密。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年5月6日 下午12:37:38
 */
public class MyHttpInputMessage implements HttpInputMessage {

    private final HttpHeaders headers;

    private final InputStream body;

    public MyHttpInputMessage(HttpInputMessage inputMessage) throws Exception {
        // 请求头
        this.headers = inputMessage.getHeaders();
        // 请求体转字符串
        String bodyStr = IOUtils.toString(inputMessage.getBody(), StandardCharsets.UTF_8);
        // 密文请求体（数据包）
        CiphertextPackage ciphertextPackage = JSON.parseObject(bodyStr, CiphertextPackage.class);
        // 解密
        String decryptStr = DesEncryptUtils.decrypt(ciphertextPackage.getCiphertext());
        // 解密后的请求体
        this.body = IOUtils.toInputStream(decryptStr, StandardCharsets.UTF_8);
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
