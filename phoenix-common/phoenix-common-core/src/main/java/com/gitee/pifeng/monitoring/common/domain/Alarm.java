package com.gitee.pifeng.monitoring.common.domain;

import com.gitee.pifeng.monitoring.common.abs.AbstractSuperBean;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorSubTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import lombok.*;
import lombok.experimental.Accessors;

import java.nio.charset.Charset;

/**
 * <p>
 * 告警信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午2:35:27
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class Alarm extends AbstractSuperBean {

    /**
     * 告警级别，默认为：WARN(警告)。<br>
     * 注意：如果是自定义业务告警（MonitorTypeEnums.CUSTOM），并且设置了告警编码，会根据告警编码查询数据库中对应的告警级别，
     * 数据库中对应的告警级别 会 覆盖直接设置的告警级别。
     *
     * @see Alarm#code
     * @see MonitorTypeEnums#CUSTOM
     * @see AlarmLevelEnums
     */
    @Builder.Default
    private AlarmLevelEnums alarmLevel = AlarmLevelEnums.WARN;

    /**
     * 告警原因，默认为：IGNORE(忽略)。
     *
     * @see AlarmReasonEnums
     */
    @Builder.Default
    private AlarmReasonEnums alarmReason = AlarmReasonEnums.IGNORE;

    /**
     * 监控类型，默认为：CUSTOM(自定义)。
     *
     * @see MonitorTypeEnums
     */
    @Builder.Default
    private MonitorTypeEnums monitorType = MonitorTypeEnums.CUSTOM;

    /**
     * 监控子类型，默认为：EMPTY(空)。
     *
     * @see MonitorSubTypeEnums
     */
    @Builder.Default
    private MonitorSubTypeEnums monitorSubType = MonitorSubTypeEnums.EMPTY;

    /**
     * 字符集，如果当前字符集不是UTF-8，请指明字符集。
     *
     * @see Charset
     */
    private Charset charset;

    /**
     * 是否是测试告警，测试告警服务端不发送告警消息。
     */
    private boolean isTest;

    /**
     * 告警标题。<br>
     * 注意：如果是自定义业务告警（MonitorTypeEnums.CUSTOM），并且设置了告警编码，会根据告警编码查询数据库中对应的告警标题，
     * 数据库中对应的告警标题 会 覆盖直接设置的告警标题。
     *
     * @see Alarm#code
     * @see MonitorTypeEnums#CUSTOM
     */
    private String title;

    /**
     * 告警内容。<br>
     * 注意：如果是自定义业务告警（MonitorTypeEnums.CUSTOM），并且设置了告警编码，会根据告警编码查询数据库中对应的告警内容，
     * 数据库中对应的告警内容 会 覆盖直接设置的告警内容。
     *
     * @see Alarm#code
     * @see MonitorTypeEnums#CUSTOM
     */
    private String msg;

    /**
     * 告警编码。<br>
     * 针对每一个自定义的业务告警，都可以设置一个唯一的编码，用此编码来查找数据库中对应的告警级别、告警标题、告警内容，如果不设置编码，
     * 将直接使用设置的告警级别、告警标题、告警内容。<br>
     * 注意：如果是自定义业务告警（MonitorTypeEnums.CUSTOM），编码在数据库中对应的告警级别、告警标题、告警内容 会 覆盖直接设置的告警级别、告警标题、告警内容。
     */
    private String code;

    /**
     * 被告警主体唯一ID。
     */
    private String alertedEntityId;

    /**
     * 是否无视静默告警，如果无视，则即使在静默告警时间段，也会发送告警，默认不无视。
     */
    private boolean isIgnoreSilence;

}
