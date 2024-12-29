package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorSubTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorRealtimeMonitoringDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorRealtimeMonitoring;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorRealtimeMonitoringService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 实时监控服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024-12-20
 */
@Service
public class MonitorRealtimeMonitoringServiceImpl extends ServiceImpl<IMonitorRealtimeMonitoringDao, MonitorRealtimeMonitoring>
        implements IMonitorRealtimeMonitoringService {

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
    @Override
    public int delete(MonitorTypeEnums typeEnums, MonitorSubTypeEnums subTypeEnums, List<?> alertedEntityIds) {
        String type = typeEnums != null ? typeEnums.name() : null;
        String subType = subTypeEnums != null ? subTypeEnums.name() : null;
        LambdaUpdateWrapper<MonitorRealtimeMonitoring> lambdaUpdateWrapper = Wrappers.lambdaUpdate();
        lambdaUpdateWrapper.eq(StringUtils.isNotBlank(type), MonitorRealtimeMonitoring::getType, type);
        lambdaUpdateWrapper.eq(StringUtils.isNotBlank(subType), MonitorRealtimeMonitoring::getSubType, subType);
        if (CollectionUtils.isNotEmpty(alertedEntityIds)) {
            List<String> ids = alertedEntityIds.stream().map(Object::toString).collect(Collectors.toList());
            lambdaUpdateWrapper.in(MonitorRealtimeMonitoring::getAlertedEntityId, ids);
        }
        return this.baseMapper.delete(lambdaUpdateWrapper);
    }

}
