package com.transfar.agent.business.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.transfar.agent.business.constant.Urlconstants;
import com.transfar.agent.business.server.service.IAlarmService;
import com.transfar.common.dto.AlarmPackage;
import com.transfar.common.dto.BaseResponsePackage;
import com.transfar.common.dto.CiphertextPackage;
import com.transfar.common.util.DesEncryptUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Objects;

/**
 * <p>
 * 跟服务端相关的告警服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 下午3:30:01
 */
@Service
public class AlarmServiceImpl implements IAlarmService {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * <p>
     * 给服务端发告警包
     * </p>
     *
     * @param alarmPackage 告警包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月6日 下午3:27:17
     */
    @Override
    public BaseResponsePackage sendAlarmPackage(AlarmPackage alarmPackage) {
        // 加密告警数据包
        String encrypt = DesEncryptUtils.encrypt(alarmPackage.toJsonString());
        CiphertextPackage ciphertextPackage = new CiphertextPackage(encrypt);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<CiphertextPackage> entity = new HttpEntity<>(ciphertextPackage, headers);
        ResponseEntity<CiphertextPackage> responseEntity = this.restTemplate.exchange(Urlconstants.ALARM_URL,
                HttpMethod.POST, entity, CiphertextPackage.class);
        String ciphertext = Objects.requireNonNull(responseEntity.getBody()).getCiphertext();
        // 解密告警响应数据包
        String decrypt = DesEncryptUtils.decrypt(ciphertext);
        return JSON.parseObject(decrypt, BaseResponsePackage.class);
    }

}
