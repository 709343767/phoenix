package com.transfar.agent.business.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.transfar.agent.business.constant.Urlconstants;
import com.transfar.agent.business.server.service.IHeartbeatService;
import com.transfar.common.dto.BaseResponsePackage;
import com.transfar.common.dto.CiphertextPackage;
import com.transfar.common.dto.HeartbeatPackage;
import com.transfar.common.util.DesEncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;

/**
 * <p>
 * 跟服务端相关的心跳服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午2:13:59
 */
@Service
public class HeartbeatServiceImpl implements IHeartbeatService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * <p>
     * 给服务端发心跳包
     * </p>
     *
     * @param heartbeatPackage 心跳包对象
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月4日 下午2:16:07
     */
    @Override
    public BaseResponsePackage sendHeartbeatPackage(HeartbeatPackage heartbeatPackage) {
        // 加密心跳包
        String encrypt = DesEncryptUtils.encrypt(heartbeatPackage.toJsonString());
        CiphertextPackage ciphertextPackage = new CiphertextPackage(encrypt);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<CiphertextPackage> entity = new HttpEntity<>(ciphertextPackage, headers);
        ResponseEntity<CiphertextPackage> responseEntity = this.restTemplate.exchange(Urlconstants.HEARTBEAT_URL,
                HttpMethod.POST, entity, CiphertextPackage.class);
        String ciphertext = Objects.requireNonNull(responseEntity.getBody()).getCiphertext();
        // 解密心跳响应数据包
        String decrypt = DesEncryptUtils.decrypt(ciphertext);
        return JSON.parseObject(decrypt, BaseResponsePackage.class);
    }

}
