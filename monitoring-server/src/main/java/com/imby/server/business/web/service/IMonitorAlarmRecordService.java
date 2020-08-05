package com.imby.server.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imby.server.business.web.entity.MonitorAlarmRecord;
import com.imby.server.business.web.vo.HomeAlarmRecordVo;
import com.imby.server.business.web.vo.MonitorAlarmRecordVo;

/**
 * <p>
 * 告警记录服务类
 * </p>
 *
 * @author 皮锋
 * @since 2020-08-04
 */
public interface IMonitorAlarmRecordService extends IService<MonitorAlarmRecord> {

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
     * 获取监控告警列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @return 分页Page对象
     * @author 皮锋
     * @custom.date 2020/8/3 11:07
     */
    Page<MonitorAlarmRecordVo> getMonitorAlarmRecordList(Long current, Long size);
}
