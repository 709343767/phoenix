package com.gitee.pifeng.monitoring.common.web.toolkit;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.util.secure.SecureUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class HttpInputMessagePackageDecrypt implements HttpInputMessage {

    private final HttpHeaders headers;

    private final InputStream body;

    /**
     * <p>
     * 构造方法
     * </p>
     *
     * @param inputMessage HTTP输入消息
     * @author 皮锋
     * @custom.date 2021/4/11 10:51
     */
    @SneakyThrows
    public HttpInputMessagePackageDecrypt(HttpInputMessage inputMessage) {
        // 请求头
        this.headers = inputMessage.getHeaders();
        // 请求体转字符串
        String bodyStr = IOUtils.toString(inputMessage.getBody(), StandardCharsets.UTF_8);
        // 获取密文请求体（数据包）
        JsonStringEncoder encoder = JsonStringEncoder.getInstance();
        byte[] fb = encoder.encodeAsUTF8(bodyStr);
        CiphertextPackage ciphertextPackage = new ObjectMapper().readValue(fb, CiphertextPackage.class);
        // 解密后的字符串
        String decryptStr;
        // 是否需要进行UnGzip
        boolean isUnGzipEnabled = ciphertextPackage.isUnGzipEnabled();
        if (isUnGzipEnabled) {
            // 解密
            byte[] decrypt = SecureUtils.decrypt(ciphertextPackage.getCiphertext(), CharsetUtil.UTF_8);
            // 解压
            decryptStr = ZipUtil.unGzip(decrypt, CharsetUtil.UTF_8);
        } else {
            // 解密
            decryptStr = SecureUtils.decrypt(ciphertextPackage.getCiphertext(), StandardCharsets.UTF_8);
        }
        // 打印日志
        log.debug("请求包：{}", decryptStr);
        // 解密后的请求体
        assert decryptStr != null;
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
