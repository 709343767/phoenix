package com.transfar.common.domain;

import java.nio.charset.Charset;

import com.transfar.common.common.SuperBean;
import com.transfar.common.constant.AlarmLevelEnums;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

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
     * 告警级别
     */
    private AlarmLevelEnums alarmLevel;

    /**
     * 字符集
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

}
