package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.gitee.pifeng.monitoring.common.constant.EnterpriseEnums;
import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.domain.MonitoringSms;
import com.gitee.pifeng.monitoring.server.business.server.domain.Sms;
import com.gitee.pifeng.monitoring.server.business.server.service.ISmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * <p>
 * 短信服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月9日 下午7:57:47
 */
@Service
@Slf4j
public class SmsServiceImpl implements ISmsService {

    /**
     * 监控配置属性加载器
     */
    @Autowired
    private MonitoringConfigPropertiesLoader monitoringConfigPropertiesLoader;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * <p>
     * 发送告警模板短信
     * </p>
     *
     * @param sms 短信实体对象
     * @return {@link Result} 返回结果
     * @author 皮锋
     * @custom.date 2021/1/29 10:05
     */
    @Override
    public Result sendAlarmTemplateSms(Sms sms) {
        // 短信接口商家
        EnterpriseEnums enterpriseEnum = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSmsProperties().getEnterpriseEnum();
        // 判断短信接口商家，不同的商家调用不同的接口
        if (EnterpriseEnums.PHOENIX == enterpriseEnum) {
            // 调用phoenix短信接口
            return this.callMonitoringSmsApi(sms);
        }
        return Result.builder().isSuccess(false).msg("没有设置短信接口商家！").build();
    }

    /**
     * <p>
     * 封装数据，调用Monitoring公司的短信接口发送短信
     * </p>
     *
     * @param sms 短信实体对象
     * @return {@link Result} 返回结果
     * @author 皮锋
     * @custom.date 2020年3月10日 下午3:19:36
     */
    private Result callMonitoringSmsApi(Sms sms) {
        String enterprise = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSmsProperties().getEnterpriseEnum().name();
        // 短信不支持<br>标签换行
        String text = sms.getContent().replace("<br>", "");
        String title = sms.getTitle();
        MonitoringSms monitoringSms = MonitoringSms.builder()
                .content(StringUtils.isBlank(title) ? text : ("[" + title + "]" + text))
                .type(sms.getLevel())
                .phone(StringUtils.join(sms.getPhones(), ";"))
                .identity(enterprise)
                .build();
        // Monitoring公司短信接口
        return this.sendSmsByMonitoringApi(monitoringSms);
    }

    /**
     * <p>
     * 调用Monitoring的短信接口发送短信
     * </p>
     *
     * @param sms Monitoring短信实体对象
     * @return {@link Result} 短信发送结果
     * @author 皮锋
     * @custom.date 2020年3月10日 上午11:01:47
     */
    private Result sendSmsByMonitoringApi(MonitoringSms sms) {
        String alarmSmsAddress = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSmsProperties().getAddress();
        // URL地址
        String url = alarmSmsAddress + "?phone=" + sms.getPhone() + "&type=" + sms.getType() + "&content=" + sms.getContent() + "&identity=" + sms.getIdentity();
        log.info("Monitoring的短信接口URL：{}", url);
        try {
            ResponseEntity<String> responseEntity = this.restTemplate.getForEntity(url, String.class);
            String body = responseEntity.getBody();
            boolean notBlank = StringUtils.isNotBlank(body);
            if (notBlank) {
                // 短信发送成功
                return Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build();
            }
            return Result.builder().isSuccess(false).msg("发送短信失败！").build();
        } catch (Exception e) {
            log.error("调用Monitoring的短信接口发送短信异常！", e);
            return Result.builder().isSuccess(false).msg(e.getMessage()).build();
        }
    }

}
