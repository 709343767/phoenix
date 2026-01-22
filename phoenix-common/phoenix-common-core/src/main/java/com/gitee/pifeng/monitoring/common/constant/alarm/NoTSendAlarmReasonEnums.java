package com.gitee.pifeng.monitoring.common.constant.alarm;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 不发送告警原因
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/5/3 9:48
 */
@Getter
@AllArgsConstructor
public enum NoTSendAlarmReasonEnums {

    /**
     * 告警开关没有打开
     */
    ALARM_SWITCH_OFF("001", "告警开关没有打开"),

    /**
     * 静默告警
     */
    SILENCE_ALARM_PERIOD("002", "静默告警"),

    /**
     * 测试信息
     */
    TEST_MSG("003", "测试信息"),

    /**
     * 小于配置的告警级别
     */
    LESS_THAN_THE_CONFIGURED_ALARM_LEVEL("004", "小于配置的告警级别"),

    /**
     * 告警标题为空
     */
    ALARM_TITLE_EMPTY("005", "告警标题为空"),

    /**
     * 告警内容为空
     */
    ALARM_CONTENT_EMPTY("006", "告警内容为空"),

    /**
     * 没有配置告警方式
     */
    NO_ALARM_MODE_CONFIGURED("007", "没有配置告警方式");

    private final String code;

    private final String msg;

    /**
     * <p>
     * 获取所有“不发送告警原因”枚举项对应的消息文本列表
     * </p>
     *
     * @return 所有“不发送告警原因”的消息文本组成的不可变 {@link List}，按枚举定义顺序排列；列表非 null，且不包含 null 元素。
     * @author 皮锋
     * @custom.date 2026/1/22 09:24
     */
    public static List<String> getAllNoSendAlarmReasonMsgs() {
        return Arrays.stream(NoTSendAlarmReasonEnums.values())
                .map(NoTSendAlarmReasonEnums::getMsg)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }

}
