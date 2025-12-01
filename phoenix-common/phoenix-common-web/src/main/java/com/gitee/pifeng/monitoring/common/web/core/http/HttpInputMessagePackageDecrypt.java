package com.gitee.pifeng.monitoring.common.web.core.http;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.exception.DecryptionException;
import com.gitee.pifeng.monitoring.common.util.secure.SecureUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.lang.NonNull;

import java.io.IOException;
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

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final HttpHeaders headers;

    private final InputStream originalBody;

    /**
     * <p>
     * 构造方法
     * </p>
     *
     * @param inputMessage HTTP输入消息
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2021/4/11 10:51
     */
    public HttpInputMessagePackageDecrypt(HttpInputMessage inputMessage) throws IOException {
        this.headers = inputMessage.getHeaders();
        this.originalBody = inputMessage.getBody();
    }

    /**
     * <p>
     * 获取解密后的请求体输入流。
     * 该方法会读取原始请求体，解析为 {@link CiphertextPackage} 密文数据包，
     * 根据是否启用 Gzip 压缩进行解密与解压，最终返回明文内容的输入流。
     * 注意：此方法仅应在 Spring 框架内部调用一次，重复调用可能导致原始流已被消费而抛出异常。
     * </p>
     *
     * @return 解密后的明文请求体输入流
     * @throws DecryptionException 解密异常
     * @author 皮锋
     * @custom.date 2025/12/1 11:20
     */
    @NonNull
    @Override
    public InputStream getBody() throws DecryptionException {
        try {
            // 请求体转字符串
            String bodyStr = IOUtils.toString(this.originalBody, StandardCharsets.UTF_8);
            // 获取密文请求体（数据包）
            JsonStringEncoder encoder = JsonStringEncoder.getInstance();
            byte[] fb = encoder.encodeAsUTF8(bodyStr);
            CiphertextPackage ciphertextPackage = OBJECT_MAPPER.readValue(fb, CiphertextPackage.class);
            // 解密后的字符串
            String decryptStr;
            // 是否需要进行UnGzip
            boolean isUnGzipEnabled = ciphertextPackage.isUnGzipEnabled();
            if (isUnGzipEnabled) {
                // 解密
                byte[] decrypt = SecureUtils.decrypt(ciphertextPackage.getCiphertext());
                // 解压
                decryptStr = ZipUtil.unGzip(decrypt, CharsetUtil.UTF_8);
            } else {
                // 解密
                decryptStr = SecureUtils.decrypt(ciphertextPackage.getCiphertext(), StandardCharsets.UTF_8);
            }
            // 打印日志
            if (log.isDebugEnabled()) {
                log.debug("请求包：{}", decryptStr);
            }
            // 解密后的请求体
            return IOUtils.toInputStream(decryptStr, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new DecryptionException("请求数据解密失败，请检查密钥或数据格式！");
        }
    }

    /**
     * <p>
     * 获取原始 HTTP 请求头信息。
     * 此方法直接返回构造时传入的请求头，不做任何修改。
     * </p>
     *
     * @return HTTP 请求头
     * @author 皮锋
     * @custom.date 2025/12/1 11:22
     */
    @NonNull
    @Override
    public HttpHeaders getHeaders() {
        return this.headers;
    }

}
