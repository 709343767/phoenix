package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmWayEnums;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorAlarmRecordDetailDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorAlarmRecordDetail;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorAlarmRecordDetailService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeAlarmRecordVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorAlarmRecordDetailVo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 告警记录详情服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025-05-03
 */
@Service
public class MonitorAlarmRecordDetailServiceImpl extends ServiceImpl<IMonitorAlarmRecordDetailDao, MonitorAlarmRecordDetail> implements IMonitorAlarmRecordDetailService {

    /**
     * <p>
     * 获取home页的告警记录信息
     * </p>
     *
     * @return home页的告警记录表现层对象
     * @author 皮锋
     * @custom.date 2020/8/4 17:01
     */
    @Override
    public HomeAlarmRecordVo getHomeAlarmRecordInfo() {
        Map<String, Object> map = this.baseMapper.getAlarmRecordSuccessRateStatistics();
        return HomeAlarmRecordVo.builder()
                .alarmRecordSum(NumberUtil.parseInt(map.get("alarmRecordSum").toString()))
                .alarmRecordSuccessSum(NumberUtil.parseInt(map.get("alarmRecordSuccessSum").toString()))
                .alarmRecordFailSum(NumberUtil.parseInt(map.get("alarmRecordFailSum").toString()))
                .alarmRecordUnsentSum(NumberUtil.parseInt(map.get("alarmRecordUnsentSum").toString()))
                .alarmSucRate(NumberUtil.round(map.get("alarmSucRate").toString(), 2).toString())
                .build();
    }

    /**
     * <p>
     * 获取最近7天的告警统计信息
     * </p>
     *
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/18 10:25
     */
    @Override
    public LayUiAdminResultVo getLast7DaysAlarmRecordStatistics() {
        List<Map<String, Object>> maps = this.baseMapper.getLast7DaysAlarmRecordStatistics();
        return LayUiAdminResultVo.ok(maps);
    }

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
    @Override
    public List<MonitorAlarmRecordDetailVo> getMonitorAlarmRecordDetails(Long alarmRecordId) {
        LambdaQueryWrapper<MonitorAlarmRecordDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MonitorAlarmRecordDetail::getAlarmRecordId, alarmRecordId);
        List<MonitorAlarmRecordDetail> monitorAlarmRecordDetails = this.baseMapper.selectList(queryWrapper);
        List<MonitorAlarmRecordDetailVo> result = Lists.newArrayList();
        for (MonitorAlarmRecordDetail monitorAlarmRecordDetail : monitorAlarmRecordDetails) {
            MonitorAlarmRecordDetailVo monitorAlarmRecordDetailVo = MonitorAlarmRecordDetailVo.builder().build().convertFor(monitorAlarmRecordDetail);
            // 告警方式（SMS、MAIL、...）
            String way = monitorAlarmRecordDetailVo.getWay();
            monitorAlarmRecordDetailVo.setWay(StringUtils.replaceEach(way,
                    new String[]{AlarmWayEnums.SMS.name(), AlarmWayEnums.MAIL.name()},
                    new String[]{"短信", "邮件"}));
            // 告警发送状态（0：失败；1：成功）
            String status = monitorAlarmRecordDetailVo.getStatus();
            monitorAlarmRecordDetailVo.setStatus(StringUtils.replaceEach(status,
                    new String[]{ZeroOrOneConstants.ZERO, ZeroOrOneConstants.ONE},
                    new String[]{"失败", "成功"}));
            if (StringUtils.isBlank(monitorAlarmRecordDetailVo.getStatus())) {
                monitorAlarmRecordDetailVo.setStatus("不提醒");
            }
            // 被告警人号码
            String number = monitorAlarmRecordDetailVo.getNumber();
            monitorAlarmRecordDetailVo.setNumber(StringUtils.isBlank(number) ? number : number.replace(",", "、"));
            result.add(monitorAlarmRecordDetailVo);
        }
        return result;
    }

}
