package com.imby.server.business.web.vo;

import com.imby.common.inf.ISuperBean;
import io.swagger.annotations.ApiModel;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 监控配置页面表单对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/6 11:16
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "监控配置页面表单对象")
public class MonitorConfigPageFormVo implements ISuperBean {

    /**
     * 监控阈值
     */
    private int threshold;

    /**
     * 网络监控是否打开
     */
    private int netEnable;

    /**
     * 监控告警是否打开
     */
    private int alarmEnable;

    /**
     * 监控告警级别
     */
    private String alarmLevel;

    /**
     * 监控告警方式
     */
    private String alarmWay;

    /**
     * 告警收件人邮箱地址，多个地址用英文分号隔开
     */
    private String alarmMailEmills;

    /**
     * 告警短信接口地址
     */
    private String alarmSmsAddress;

    /**
     * 短信接口企业
     */
    private String alarmSmsEnterprise;

    /**
     * 告警短信手机号码，多个号码用英文分号隔开
     */
    private String alarmSmsPhoneNumbers;

    /**
     * 告警短信接口协议
     */
    private String alarmSmsProtocol;

}
