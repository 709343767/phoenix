package com.imby.server.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.imby.common.constant.ZeroOrOneConstants;
import com.imby.server.business.web.dao.IMonitorAlarmRecordDao;
import com.imby.server.business.web.entity.MonitorAlarmRecord;
import com.imby.server.business.web.service.IMonitorAlarmRecordService;
import com.imby.server.business.web.vo.HomeAlarmRecordVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 告警记录服务实现类
 * </p>
 *
 * @author 皮锋
 * @since 2020-08-04
 */
@Service
public class MonitorAlarmRecordServiceImpl extends ServiceImpl<IMonitorAlarmRecordDao, MonitorAlarmRecord> implements IMonitorAlarmRecordService {

    /**
     * 告警记录数据访问对象
     */
    @Autowired
    private IMonitorAlarmRecordDao monitorAlarmRecordDao;

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
        List<MonitorAlarmRecord> monitorAlarmRecords = this.monitorAlarmRecordDao.selectList(new LambdaQueryWrapper<>());
        HomeAlarmRecordVo homeAlarmRecordVo = new HomeAlarmRecordVo();
        homeAlarmRecordVo.setAlarmRecordSum(monitorAlarmRecords.size());
        // 告警成功
        homeAlarmRecordVo.setAlarmRecordSuccessSum((int) monitorAlarmRecords.stream().filter(e -> (
                // 没发短信 && 没发邮件
                (StringUtils.isBlank(e.getSmsStatus()) && StringUtils.isBlank(e.getMailStatus())) ||
                        // 没发邮件 && 短信发送成功
                        (StringUtils.isBlank(e.getMailStatus()) && StringUtils.equals(e.getSmsStatus(), ZeroOrOneConstants.ONE)) ||
                        // 没发短信 && 邮件发送成功
                        (StringUtils.isBlank(e.getSmsStatus()) && StringUtils.equals(e.getMailStatus(), ZeroOrOneConstants.ONE)) ||
                        // 短信和邮件都发送成功
                        (StringUtils.equals(e.getSmsStatus(), ZeroOrOneConstants.ONE) && StringUtils.equals(e.getMailStatus(), ZeroOrOneConstants.ONE))))
                .count());
        // 告警失败
        homeAlarmRecordVo.setAlarmRecordFailSum((int) monitorAlarmRecords.stream().filter(e -> (
                // 短信发送失败
                StringUtils.equals(e.getSmsStatus(), ZeroOrOneConstants.ZERO) ||
                        // 邮件发送失败
                        StringUtils.equals(e.getMailStatus(), ZeroOrOneConstants.ZERO)))
                .count());
        return homeAlarmRecordVo;
    }
}
