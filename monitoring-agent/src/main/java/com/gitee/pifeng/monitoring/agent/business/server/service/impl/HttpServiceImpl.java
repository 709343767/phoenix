package com.gitee.pifeng.monitoring.agent.business.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.monitoring.agent.business.server.service.IHttpService;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.CiphertextPackage;
import com.gitee.pifeng.monitoring.common.init.InitSecurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
     * 1.加密数据；<br>
     * 2.把加密后的数据封装到密文数据包；<br>
     * 3.发送http请求；<br>
     * 4.获取响应密文数据包；<br>
     * 5.解密密文数据包；<br>
     * 6.把密文数据包转换成基础响应包。<br>
     *
     * @param json 请求参数
     * @param url  请求地址
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020/4/30 16:34
     */
    @Override
    public BaseResponsePackage sendHttpPost(String json, String url) {
        // 加密告警数据包
        String encrypt = InitSecurer.encrypt(json);
        CiphertextPackage ciphertextPackage = new CiphertextPackage(encrypt);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<CiphertextPackage> entity = new HttpEntity<>(ciphertextPackage, headers);
        ResponseEntity<CiphertextPackage> responseEntity = this.restTemplate.exchange(url,
                HttpMethod.POST, entity, CiphertextPackage.class);
        String ciphertext = Objects.requireNonNull(responseEntity.getBody()).getCiphertext();
        // 解密告警响应数据包
        String decrypt = InitSecurer.decrypt(ciphertext);
        return JSON.parseObject(decrypt, BaseResponsePackage.class);
    }
}
