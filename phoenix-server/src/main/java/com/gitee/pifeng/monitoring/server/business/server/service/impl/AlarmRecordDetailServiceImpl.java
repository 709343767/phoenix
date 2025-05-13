package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmWayEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorAlarmRecordDao;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorAlarmRecordDetailDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorAlarmRecord;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorAlarmRecordDetail;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmRecordDetailService;
import com.gitee.pifeng.monitoring.server.business.server.service.ITemplateMsgSendFacadeService;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 告警记录详情服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2025/5/7 08:09
 */
@Slf4j
@Service
public class AlarmRecordDetailServiceImpl extends ServiceImpl<IMonitorAlarmRecordDetailDao, MonitorAlarmRecordDetail> implements IAlarmRecordDetailService {

    /**
     * 监控配置属性加载器
     */
    @Autowired
    private MonitoringConfigPropertiesLoader monitoringConfigPropertiesLoader;

    /**
     * 发送模板消息门面服务接口
     */
    @Autowired
    private ITemplateMsgSendFacadeService templateMsgSendFacadeService;

    /**
     * 监控告警记录数据访问对象
     */
    @Autowired
    private IMonitorAlarmRecordDao monitorAlarmRecordDao;

    /**
     * <p>
     * 执行告警操作
     * </p>
     * 1.把告警详情信息插入数据库；<br>
     * 2.发送告警；<br>
     * 3.更新告警详情信息中的告警发送结果；<br>
     * 4.更新告警记录信息中每种告警方式的告警发送结果。<br>
     *
     * @param alarm         告警信息
     * @param alarmRecordId 告警记录表主键ID
     * @param alarmCode     告警代码
     * @author 皮锋
     * @custom.date 2025/5/3 17:35
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void executeAlarm(Alarm alarm, Long alarmRecordId, String alarmCode) {
        // 第一步：把告警详情信息插入数据库
        this.insertMonitorAlarmRecordDetailToDb(alarmRecordId, alarmCode);
        // 第二步：发送告警
        Table<AlarmWayEnums, Date, Result> alarmResultTable = this.batchSendAlarm(alarm);
        // 告警方式，多种方式用逗号分隔
        List<String> ways = Lists.newArrayList();
        // 告警发送完更新数据库中告警发送结果
        for (Table.Cell<AlarmWayEnums, Date, Result> cell : alarmResultTable.cellSet()) {
            // 告警方式
            AlarmWayEnums alarmWayEnum = cell.getRowKey();
            // 告警结果获取时间
            Date resultDate = cell.getColumnKey();
            // 告警结果
            Result result = cell.getValue();
            if (alarmWayEnum != null && result != null) {
                // 拼接告警方式，多种方式用逗号分隔
                ways.add(alarmWayEnum.name() + "(" + (result.isSuccess() ? "成功" : "失败") + ")");
                // 第三步：更新告警记录详情表中告警信息发送状态
                this.updateMonitorAlarmRecordDetailToDb(result, alarmRecordId, alarmWayEnum, resultDate);
            }
        }
        // 第四步：更新告警记录表中的告警方式，把每一种告警方式的告警结果回填进去
        MonitorAlarmRecord monitorAlarmRecord = new MonitorAlarmRecord();
        monitorAlarmRecord.setWay(String.join(",", ways));
        monitorAlarmRecord.setUpdateTime(new Date());
        LambdaUpdateWrapper<MonitorAlarmRecord> alarmRecordLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        alarmRecordLambdaUpdateWrapper.eq(MonitorAlarmRecord::getId, alarmRecordId);
        this.monitorAlarmRecordDao.update(monitorAlarmRecord, alarmRecordLambdaUpdateWrapper);
    }

    /**
     * <p>
     * 把告警详情信息插入数据库
     * </p>
     *
     * @param alarmRecordId 告警记录表主键ID
     * @param alarmCode     告警代码
     * @author 皮锋
     * @custom.date 2025/5/3 17:13
     */
    private void insertMonitorAlarmRecordDetailToDb(Long alarmRecordId, String alarmCode) {
        List<MonitorAlarmRecordDetail> monitorAlarmRecordDetails = Lists.newArrayList();
        // 告警方式
        AlarmWayEnums[] alarmWayEnums = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getWayEnums();
        // 新增时间
        Date insertTime = new Date();
        // 告警方式
        for (AlarmWayEnums alarmWayEnum : alarmWayEnums) {
            MonitorAlarmRecordDetail monitorAlarmRecordDetail = new MonitorAlarmRecordDetail();
            monitorAlarmRecordDetail.setAlarmRecordId(alarmRecordId);
            monitorAlarmRecordDetail.setCode(alarmCode);
            monitorAlarmRecordDetail.setWay(alarmWayEnum.name());
            monitorAlarmRecordDetail.setInsertTime(insertTime);
            switch (alarmWayEnum) {
                case SMS:
                    String[] phones = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSmsProperties().getPhoneNumbers();
                    monitorAlarmRecordDetail.setNumber(StringUtils.join(phones, ","));
                    break;
                case MAIL:
                    String[] mails = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getMailProperties().getEmails();
                    monitorAlarmRecordDetail.setNumber(StringUtils.join(mails, ","));
                    break;
                default:
                    // 其它情况直接结束
                    break;
            }
            monitorAlarmRecordDetails.add(monitorAlarmRecordDetail);
        }
        // 批量插入
        if (CollectionUtils.isNotEmpty(monitorAlarmRecordDetails)) {
            (((IAlarmRecordDetailService) AopContext.currentProxy())).saveBatch(monitorAlarmRecordDetails);
        }
    }

    /**
     * <p>
     * 批量发送告警
     * </p>
     *
     * @param alarm 告警信息
     * @return 告警发送结果
     * @author 皮锋
     * @custom.date 2020/9/14 12:56
     */
    private Table<AlarmWayEnums, Date, Result> batchSendAlarm(Alarm alarm) {
        // 告警级别
        AlarmLevelEnums alarmLevelEnum = alarm.getAlarmLevel();
        // 告警内容标题
        String alarmTitle = alarm.getTitle();
        // 告警内容
        String alarmMsg = alarm.getMsg();
        // 告警方式
        AlarmWayEnums[] alarmWayEnums = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getWayEnums();
        // 告警发送结果
        Table<AlarmWayEnums, Date, Result> resultTable = HashBasedTable.create();
        for (AlarmWayEnums alarmWayEnum : alarmWayEnums) {
            // 发送模板文本消息
            Result result = this.templateMsgSendFacadeService.sendTemplateTextMsg(alarmWayEnum, alarmLevelEnum, alarmTitle, alarmMsg);
            resultTable.put(alarmWayEnum, new Date(), result);
        }
        return resultTable;
    }

    /**
     * <p>
     * 更新数据库中告警信息发送状态
     * </p>
     *
     * @param result        {@link Result} 返回结果
     * @param alarmRecordId 告警记录表主键ID
     * @param alarmWayEnums 告警方式
     * @param resultDate    告警结果获取时间
     * @author 皮锋
     * @custom.date 2020/5/13 17:04
     */
    private void updateMonitorAlarmRecordDetailToDb(Result result, Long alarmRecordId, AlarmWayEnums alarmWayEnums, Date resultDate) {
        MonitorAlarmRecordDetail monitorAlarmRecordDetail = new MonitorAlarmRecordDetail();
        // 更新状态
        if (result.isSuccess()) {
            monitorAlarmRecordDetail.setStatus(ZeroOrOneConstants.ONE);
        } else {
            monitorAlarmRecordDetail.setStatus(ZeroOrOneConstants.ZERO);
        }
        // 异常信息
        if (!result.isSuccess() && !StringUtils.equals(result.getMsg(), ResultMsgConstants.SUCCESS)) {
            monitorAlarmRecordDetail.setExcMessage(result.getMsg());
        }
        // 更新时间
        monitorAlarmRecordDetail.setUpdateTime(resultDate);
        LambdaUpdateWrapper<MonitorAlarmRecordDetail> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MonitorAlarmRecordDetail::getAlarmRecordId, alarmRecordId);
        lambdaUpdateWrapper.eq(MonitorAlarmRecordDetail::getWay, alarmWayEnums);
        this.baseMapper.update(monitorAlarmRecordDetail, lambdaUpdateWrapper);
    }

}