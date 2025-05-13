package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorAlarmRecordDetail;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeAlarmRecordVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorAlarmRecordDetailVo;

import java.util.List;

/**
 * <p>
 * 告警记录详情服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025-05-03
 */
public interface IMonitorAlarmRecordDetailService extends IService<MonitorAlarmRecordDetail> {

    /**
     * <p>
     * 获取home页的告警记录信息
     * </p>
     *
     * @return home页的告警记录表现层对象
     * @author 皮锋
     * @custom.date 2020/8/4 17:01
     */
    HomeAlarmRecordVo getHomeAlarmRecordInfo();

    /**
     * <p>
     * 获取最近7天的告警统计信息
     * </p>
     *
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/18 10:25
     */
    LayUiAdminResultVo getLast7DaysAlarmRecordStatistics();

    /**
     * <p>
     * 获取监控告警记录详情信息
     * </p>
     *
     * @param alarmRecordId 告警记录ID
     * @return 监控告警详情表现层对象列表
     * @author 皮锋
     * @custom.date 2025/5/7 21:00
     */
    List<MonitorAlarmRecordDetailVo> getMonitorAlarmRecordDetails(Long alarmRecordId);

}
