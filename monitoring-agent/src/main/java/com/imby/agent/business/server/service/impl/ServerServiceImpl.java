package com.imby.agent.business.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.imby.agent.business.constant.Urlconstants;
import com.imby.agent.business.server.service.IServerService;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.common.dto.CiphertextPackage;
import com.imby.common.dto.ServerPackage;
import com.imby.common.util.DesEncryptUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;

/**
 * <p>
 * 跟服务端相关的服务器信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:27:43
 */
@Service
public class ServerServiceImpl implements IServerService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * <p>
     * 给服务端发服务器信息包
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月7日 下午5:24:47
     */
    @Override
    public BaseResponsePackage sendServerPackage(ServerPackage serverPackage) {
        // 加密服务器信息包
        String encrypt = DesEncryptUtils.encrypt(serverPackage.toJsonString());
        CiphertextPackage ciphertextPackage = new CiphertextPackage(encrypt);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<CiphertextPackage> entity = new HttpEntity<>(ciphertextPackage, headers);
        ResponseEntity<CiphertextPackage> responseEntity = this.restTemplate.exchange(Urlconstants.SERVER_URL,
                HttpMethod.POST, entity, CiphertextPackage.class);
        String ciphertext = Objects.requireNonNull(responseEntity.getBody()).getCiphertext();
        // 解密服务器响应信息包
        String decrypt = DesEncryptUtils.decrypt(ciphertext);
        return JSON.parseObject(decrypt, BaseResponsePackage.class);
    }

}
