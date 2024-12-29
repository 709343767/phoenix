package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorSubTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorRealtimeMonitoring;

import java.util.List;

/**
 * <p>
 * 实时监控服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024-12-20
 */
public interface IMonitorRealtimeMonitoringService extends IService<MonitorRealtimeMonitoring> {

    /**
     * <p>
     * 删除实时监控信息
     * </p>
     *
     * @param typeEnums        监控类型
     * @param subTypeEnums     监控子类型
     * @param alertedEntityIds 被告警主体唯一ID列表
     * @return 删除记录数
     * @author 皮锋
     * @custom.date 2024/12/21 10:09
     */
    int delete(MonitorTypeEnums typeEnums, MonitorSubTypeEnums subTypeEnums, List<?> alertedEntityIds);

}
