package com.gitee.pifeng.monitoring.agent.business.server.service.impl;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ZipUtil;
import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.monitoring.agent.business.server.service.IHttpService;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.util.ZipUtils;
import com.gitee.pifeng.monitoring.common.util.secure.SecureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;

/**
 * <p>
 * 跟服务端相关的HTTP服务接口实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/30 16:29
 */
@Service
public class HttpServiceImpl implements IHttpService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * <p>
     * 发送HTTP POST请求。
     * </p>
     * 1.压缩数据；<br>
     * 2.加密数据；<br>
     * 3.把加密后的数据封装到密文数据包；<br>
     * 4.发送http请求；<br>
     * 5.获取响应密文数据包；<br>
     * 6.解密密文数据包；<br>
     * 8.解压；<br>
     * 9.转换成基础响应包。<br>
     *
     * @param json 请求参数
     * @param url  请求地址
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020/4/30 16:34
     */
    @Override
    public BaseResponsePackage sendHttpPost(String json, String url) {
        CiphertextPackage requestCiphertextPackage;
        // 字符串是否需要进行gzip压缩
        if (ZipUtils.isNeedGzip(json)) {
            // 压缩
            byte[] gzip = ZipUtil.gzip(json, CharsetUtil.UTF_8);
            // 加密
            String encrypt = SecureUtils.encrypt(gzip, StandardCharsets.UTF_8);
            requestCiphertextPackage = new CiphertextPackage(encrypt, true);
        } else {
            // 加密
            String encrypt = SecureUtils.encrypt(json, StandardCharsets.UTF_8);
            requestCiphertextPackage = new CiphertextPackage(encrypt, false);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<CiphertextPackage> entity = new HttpEntity<>(requestCiphertextPackage, headers);
        ResponseEntity<CiphertextPackage> responseEntity = this.restTemplate.exchange(url,
                HttpMethod.POST, entity, CiphertextPackage.class);
        CiphertextPackage responseciphertextPackage = Objects.requireNonNull(responseEntity.getBody());
        String ciphertext = responseciphertextPackage.getCiphertext();
        // 解密后的字符串
        String decryptStr;
        // 是否需要进行UnGzip
        boolean gzipEnabled = responseciphertextPackage.isUnGzipEnabled();
        if (gzipEnabled) {
            // 解密
            byte[] decrypt = SecureUtils.decrypt(ciphertext, CharsetUtil.UTF_8);
            // 解压
            decryptStr = ZipUtil.unGzip(decrypt, CharsetUtil.UTF_8);
        } else {
            // 解密
            decryptStr = SecureUtils.decrypt(ciphertext, StandardCharsets.UTF_8);
        }
        return JSON.parseObject(decryptStr, BaseResponsePackage.class);
    }
}
