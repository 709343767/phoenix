package com.imby.server.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.imby.server.business.web.entity.MonitorAlarmRecord;
import com.imby.server.business.web.vo.HomeAlarmRecordVo;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.MonitorAlarmRecordVo;

import java.util.List;

/**
 * <p>
 * 告警记录服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:03:49
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
     * @param type    告警类型
     * @param level   告警级别
     * @param status  告警状态
     * @param title   告警标题
     * @param content 告警内容
     * @return 分页Page对象
     * @author 皮锋
     * @custom.date 2020/8/3 11:07
     */
    Page<MonitorAlarmRecordVo> getMonitorAlarmRecordList(Long current, Long size, String type, String level, String status, String title, String content);

    /**
     * <p>
     * 删除告警记录
     * </p>
     *
     * @param monitorAlarmRecordVos 告警记录
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/7 17:00
     */
    LayUiAdminResultVo deleteMonitorAlarmRecord(List<MonitorAlarmRecordVo> monitorAlarmRecordVos);

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
     * 获取告警类型统计信息
     * </p>
     *
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/23 9:52
     */
    LayUiAdminResultVo getAlarmRecordTypeStatistics();
}
