package com.gitee.pifeng.monitoring.server.business.server.service;

import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.server.business.server.domain.Sms;

/**
 * <p>
 * 短信服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月9日 下午7:58:07
 */
public interface ISmsService {

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
    Result sendAlarmTemplateSms(Sms sms);

}
