package com.gitee.pifeng.monitoring.common.property.server;

import com.gitee.pifeng.monitoring.common.constant.CommProtocolTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.EnterpriseEnums;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import lombok.*;

/**
 * <p>
 * 告警短信配置属性
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午2:22:46
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MonitoringAlarmSmsProperties implements ISuperBean {

    /**
     * 手机号码
     */
    private String[] phoneNumbers;

    /**
     * 接口地址
     */
    private String address;

    /**
     * 接口协议
     */
    private CommProtocolTypeEnums protocolTypeEnum;

    /**
     * 接口所属企业
     */
    private EnterpriseEnums enterpriseEnum;

}
