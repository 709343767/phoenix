package com.gitee.pifeng.monitoring.server.business.server.service;

import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmWayEnums;
import com.gitee.pifeng.monitoring.common.domain.Result;

/**
 * <p>
 * 发送模板消息门面服务接口
 * </p>
 * <strong>使用“门面设计模式(外观模式)”去实现模板消息的发送，为不同的消息发送方式提供统一的接口，
 * 使用方不需要关心子内部的实现细节，只需要跟门面交互就可以了。</strong>
 *
 * @author 皮锋
 * @custom.date 2023/5/24 8:05
 */
public interface ITemplateMsgSendFacadeService {

    /**
     * <p>
     * 发送模板文本消息
     * </p>
     *
     * @param alarmWay     告警方式
     * @param alarmLevel   告警级别
     * @param alarmTitle   告警标题
     * @param alarmContent 告警内容
     * @return {@link Result} 返回结果
     * @author 皮锋
     * @custom.date 2023/5/24 10:39
     */
    Result sendTemplateTextMsg(AlarmWayEnums alarmWay, AlarmLevelEnums alarmLevel, String alarmTitle, String alarmContent);

}
