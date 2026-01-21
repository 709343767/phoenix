package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorAlarmRecord;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorAlarmRecordVo;

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
     * 获取监控告警列表
     * </p>
     *
     * @param current    当前页
     * @param size       每页显示条数
     * @param type       告警类型
     * @param level      告警级别
     * @param way        告警方式
     * @param status     告警状态（0：失败；1：成功）
     * @param title      告警标题
     * @param content    告警内容
     * @param insertDate 记录日期
     * @return 分页Page对象
     * @author 皮锋
     * @custom.date 2020/8/3 11:07
     */
    Page<MonitorAlarmRecordVo> getMonitorAlarmRecordList(Long current, Long size, String type, String level, String way,
                                                         String status, String title, String content, String insertDate);

    /**
     * <p>
     * 获取监控告警列表
     * </p>
     *
     * @param type       告警类型
     * @param level      告警级别
     * @param way        告警方式
     * @param status     告警状态（0：失败；1：成功）
     * @param title      告警标题
     * @param content    告警内容
     * @param insertDate 记录日期
     * @return 监控告警表现层对象列表
     * @author 皮锋
     * @custom.date 2021/5/18 22:50
     */
    List<MonitorAlarmRecordVo> getMonitorAlarmRecordList(String type, String level, String way, String status,
                                                         String title, String content, String insertDate);

    /**
     * <p>
     * 删除告警记录
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/8/7 17:00
     */
    LayUiAdminResultVo deleteMonitorAlarmRecord(List<Long> ids);

    /**
     * <p>
     * 清空告警记录
     * </p>
     *
     * @return layUiAdmin响应对象：如果清空成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/7/13 10:09
     */
    LayUiAdminResultVo cleanupMonitorAlarmRecord();

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

    /**
     * <p>
     * 获取监控告警记录信息
     * </p>
     *
     * @param id 告警记录ID
     * @return 监控告警表现层对象
     * @author 皮锋
     * @custom.date 2021/6/18 22:15
     */
    MonitorAlarmRecordVo getMonitorAlarmRecord(Long id);

    /**
     * <p>
     * 获取最新的5条告警记录
     * </p>
     *
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2021/9/1 9:30
     */
    LayUiAdminResultVo getLast5AlarmRecord();

}
