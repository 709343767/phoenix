package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmWayEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.NoTSendAlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorAlarmRecordDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorAlarmRecord;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;

/**
 * <p>
 * 告警记录服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午1:30:07
 */
@Slf4j
@Service
public class AlarmRecordServiceImpl extends ServiceImpl<IMonitorAlarmRecordDao, MonitorAlarmRecord> implements IAlarmRecordService {

    /**
     * 监控配置属性加载器
     */
    @Autowired
    private MonitoringConfigPropertiesLoader monitoringConfigPropertiesLoader;

    /**
     * <p>
     * 把告警信息插入数据库
     * </p>
     *
     * @param alarm     告警信息
     * @param alarmCode 告警代码
     * @return 告警记录插入后的主键ID
     * @author 皮锋
     * @custom.date 2020/5/13 16:21
     */
    @Override
    public Long insertMonitorAlarmRecordToDb(Alarm alarm, String alarmCode) {
        // 告警定义编码
        String code = alarm.getCode();
        // 监控类型
        MonitorTypeEnums monitorTypeEnum = alarm.getMonitorType();
        // 告警级别
        AlarmLevelEnums alarmLevelEnum = alarm.getAlarmLevel();
        // 告警内容标题
        String alarmTitle = alarm.getTitle();
        // 告警内容
        String alarmMsg = alarm.getMsg();
        // 告警方式
        AlarmWayEnums[] alarmWayEnums = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getWayEnums();
        String alarmWay = ArrayUtils.isNotEmpty(alarmWayEnums) ? String.join(",", Arrays.stream(alarmWayEnums).map(Enum::name).toArray(String[]::new)) : null;
        // 插入时间
        Date insertTime = new Date();
        MonitorAlarmRecord monitorAlarmRecord = new MonitorAlarmRecord();
        monitorAlarmRecord.setCode(alarmCode);
        monitorAlarmRecord.setAlarmDefCode(code);
        monitorAlarmRecord.setType(monitorTypeEnum != null ? monitorTypeEnum.name() : null);
        monitorAlarmRecord.setLevel(alarmLevelEnum != null ? alarmLevelEnum.name() : null);
        monitorAlarmRecord.setWay(alarmWay);
        monitorAlarmRecord.setTitle(alarmTitle);
        monitorAlarmRecord.setContent(alarmMsg);
        monitorAlarmRecord.setInsertTime(insertTime);
        this.baseMapper.insert(monitorAlarmRecord);
        // 返回插入后的主键ID
        return monitorAlarmRecord.getId();
    }

    /**
     * <p>
     * 更新告警记录
     * </p>
     *
     * @param notSendAlarmReasonEnum 不发送告警原因
     * @param alarmCode              告警代码
     * @author 皮锋
     * @custom.date 2025/5/8 22:44
     */
    @Override
    public void updateMonitorAlarmRecord(NoTSendAlarmReasonEnums notSendAlarmReasonEnum, String alarmCode) {
        MonitorAlarmRecord monitorAlarmRecord = new MonitorAlarmRecord();
        monitorAlarmRecord.setNotSendReason(notSendAlarmReasonEnum.getMsg());
        monitorAlarmRecord.setWay(null);
        monitorAlarmRecord.setUpdateTime(new Date());
        LambdaUpdateWrapper<MonitorAlarmRecord> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MonitorAlarmRecord::getCode, alarmCode);
        this.baseMapper.update(monitorAlarmRecord, lambdaUpdateWrapper);
    }

    /**
     * <p>
     * 根据条件获取静默告警记录数
     * </p>
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 记录数
     * @author 皮锋
     * @custom.date 2024/11/4 12:19
     */
    @Override
    public int getSilenceAlarmCount(Date startTime, Date endTime) {
        return this.baseMapper.getSilenceAlarmCount(startTime, endTime);
    }

}
