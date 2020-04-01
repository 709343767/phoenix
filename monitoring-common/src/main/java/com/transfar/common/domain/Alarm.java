package com.transfar.common.domain;

import com.transfar.common.abs.SuperBean;
import com.transfar.common.constant.AlarmLevelEnums;
import com.transfar.common.constant.AlarmTypeEnums;
import lombok.*;
import lombok.experimental.Accessors;

import java.nio.charset.Charset;

/**
 * <p>
 * 告警
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午2:35:27
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Alarm extends SuperBean {

    /**
     * 告警内容
     */
    private String msg;

    /**
     * 告警级别，默认为：WARN
     */
    @Builder.Default
    private AlarmLevelEnums alarmLevel = AlarmLevelEnums.WARN;

    /**
     * 告警类型
     */
    private AlarmTypeEnums alarmTypeEnums;

    /**
     * 字符集，如果当前字符集不是UTF-8，请指明字符集
     */
    private Charset charset;

    /**
     * 是否是测试告警，测试告警服务端不发送告警消息
     */
    private boolean isTest;

    /**
     * 告警内容标题
     */
    private String title;

    /**
     * 编码
     */
    private String code;

}
