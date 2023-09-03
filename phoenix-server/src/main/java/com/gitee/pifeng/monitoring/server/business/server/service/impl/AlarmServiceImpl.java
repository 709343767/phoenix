package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmWayEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.threadpool.ThreadPoolShutdownHelper;
import com.gitee.pifeng.monitoring.common.util.server.ProcessorsUtils;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorAlarmDefinitionDao;
import com.gitee.pifeng.monitoring.server.business.server.dao.IMonitorAlarmRecordDao;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorAlarmDefinition;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorAlarmRecord;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.IRealtimeMonitoringService;
import com.gitee.pifeng.monitoring.server.business.server.service.ITemplateMsgSendFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PreDestroy;
import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 告警服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月10日 下午1:30:07
 */
@Service
@Slf4j
public class AlarmServiceImpl implements IAlarmService {

    /**
     * 监控配置属性加载器
     */
    @Autowired
    private MonitoringConfigPropertiesLoader monitoringConfigPropertiesLoader;

    /**
     * 实时监控服务接口
     */
    @Autowired
    private IRealtimeMonitoringService realtimeMonitoringService;

    /**
     * 发送模板消息门面服务接口
     */
    @Autowired
    private ITemplateMsgSendFacadeService templateMsgSendFacadeService;

    /**
     * 监控告警定义数据访问对象
     */
    @Autowired
    private IMonitorAlarmDefinitionDao monitorAlarmDefinitionDao;

    /**
     * 监控告警记录数据访问对象
     */
    @Autowired
    private IMonitorAlarmRecordDao monitorAlarmRecordDao;

    /**
     * 线程池
     */
    public final ThreadPoolExecutor seService = new ThreadPoolExecutor(
            1,
            // 线程数 = Ncpu /（1 - 阻塞系数），IO密集型阻塞系数相对较大
            (int) (ProcessorsUtils.getAvailableProcessors() / (1 - 0.8)),
            60L,
            TimeUnit.SECONDS,
            // 2^16
            new LinkedBlockingQueue<>(65536),
            new BasicThreadFactory.Builder()
                    // 设置线程名
                    .namingPattern("phoenix-server-alarm-pool-thread")
                    // 设置为守护线程
                    .daemon(true)
                    .build(),
            new ThreadPoolExecutor.AbortPolicy());

    /**
     * 在程序关闭前优雅关闭线程池
     *
     * @author 皮锋
     * @custom.date 2023/6/4 22:00
     */
    @Order
    @PreDestroy
    public void shutdownThreadPoolGracefully() {
        new ThreadPoolShutdownHelper().shutdownGracefully(this.seService, "phoenix-server-alarm-pool-thread");
    }

    /**
     * <p>
     * 处理告警包
     * </p>
     *
     * @param alarmPackage 告警包
     * @return {@link Result} 返回结果
     * @author 皮锋
     * @custom.date 2020年3月10日 下午1:33:55
     */
    @Override
    public Result dealAlarmPackage(AlarmPackage alarmPackage) {
        // 返回结果
        Result result = new Result();
        // 处理告警信息
        this.dealAlarm(result, alarmPackage);
        return result;
    }

    /**
     * <p>
     * 处理告警信息
     * </p>
     *
     * @param result       {@link Result} 返回结果
     * @param alarmPackage 告警包
     * @author 皮锋
     * @custom.date 2020/4/2 11:49
     */
    private void dealAlarm(Result result, AlarmPackage alarmPackage) {
        // 获取告警信息
        Alarm alarm = alarmPackage.getAlarm();
        // 对告警进行前置判断，防止重复发送相同的告警
        if (!this.realtimeMonitoringService.beforeAlarmJudge(alarm)) {
            String expMsg = "前置判断——>不满足告警条件！";
            this.wrapFailResult(result, expMsg);
            return;
        }
        // 告警ID
        String alarmUuid = alarmPackage.getId();
        // 监控类型
        MonitorTypeEnums monitorTypeEnum = alarm.getMonitorType();
        // 告警代码
        String code = alarm.getCode();
        // 如果告警类型是自定义的业务告警，并且有告警代码，查询数据库中此告警代码对应的告警级别、告警标题、告警内容，数据库中有就用数据库的
        if (monitorTypeEnum == MonitorTypeEnums.CUSTOM && StringUtils.isNotBlank(code)) {
            try {
                LambdaQueryWrapper<MonitorAlarmDefinition> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(MonitorAlarmDefinition::getCode, code);
                MonitorAlarmDefinition monitorAlarmDefinition = this.monitorAlarmDefinitionDao.selectOne(lambdaQueryWrapper);
                if (monitorAlarmDefinition != null) {
                    // 数据库中的告警级别
                    String dbLevel = monitorAlarmDefinition.getGrade();
                    if (StringUtils.isNotBlank(dbLevel)) {
                        AlarmLevelEnums alarmLevelEnum = AlarmLevelEnums.str2Enum(dbLevel);
                        alarm.setAlarmLevel(alarmLevelEnum);
                    }
                    // 数据库中的告警标题
                    String dbTitle = monitorAlarmDefinition.getTitle();
                    if (StringUtils.isNotBlank(dbTitle)) {
                        alarm.setTitle(dbTitle);
                    }
                    // 数据库中的告警内容
                    String dbMsg = monitorAlarmDefinition.getContent();
                    if (StringUtils.isNotBlank(dbMsg)) {
                        alarm.setMsg(dbMsg);
                    }
                }
            } catch (Exception e) {
                String expMsg = "根据告警代码从数据库中查询告警定义失败！";
                this.wrapFailResult(result, expMsg);
                return;
            }
        }
        // 不管发不发送告警信息，先把告警记录存入数据库，AopContext.currentProxy()调用是为了让事务生效
        ((IAlarmService) AopContext.currentProxy()).insertMonitorAlarmRecordToDb(alarm, alarmUuid);
        // 是否发送告警信息
        if (!this.isSendAlarm(result, alarm)) {
            return;
        }
        // 注意：走到此处，就确定发送告警了，不管发送是否成功，result都是返回成功，代表处理此次告警成功了，发送告警和处理告警是两回事，
        // 因此采用异步的方式发送告警，防止阻塞此方法
        this.seService.execute(() -> {
            // 发送告警以及把告警记录计入数据库
            this.sendAlarm(alarm, alarmUuid);
        });
    }

    /**
     * <p>
     * 发送告警
     * </p>
     * 1.发送告警；<br>
     * 2.更新数据库中的告警发送结果。<br>
     *
     * @param alarm     告警信息
     * @param alarmUuid 告警代码
     * @author 皮锋
     * @custom.date 2020/9/14 12:56
     */
    private void sendAlarm(Alarm alarm, String alarmUuid) {
        // 告警级别
        AlarmLevelEnums alarmLevelEnum = alarm.getAlarmLevel();
        // 告警内容标题
        String alarmTitle = alarm.getTitle();
        // 告警内容
        String alarmMsg = alarm.getMsg();
        // 告警方式
        AlarmWayEnums[] alarmWayEnums = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getWayEnums();
        for (AlarmWayEnums alarmWayEnum : alarmWayEnums) {
            // 发送模板文本消息
            Result result = this.templateMsgSendFacadeService.sendTemplateTextMsg(alarmWayEnum, alarmLevelEnum, alarmTitle, alarmMsg);
            // 告警发送完更新数据库中告警发送结果
            this.updateMonitorAlarmRecordToDb(result, alarmUuid, alarmWayEnum);
        }
    }

    /**
     * <p>
     * 更新数据库中告警信息发送状态
     * </p>
     *
     * @param result        {@link Result} 返回结果
     * @param uuid          告警代码
     * @param alarmWayEnums 告警方式
     * @author 皮锋
     * @custom.date 2020/5/13 17:04
     */
    private void updateMonitorAlarmRecordToDb(Result result, String uuid, AlarmWayEnums alarmWayEnums) {
        MonitorAlarmRecord monitorAlarmRecord = new MonitorAlarmRecord();
        // 更新状态
        if (result.isSuccess()) {
            monitorAlarmRecord.setStatus(ZeroOrOneConstants.ONE);
        } else {
            monitorAlarmRecord.setStatus(ZeroOrOneConstants.ZERO);
        }
        // 更新时间
        monitorAlarmRecord.setUpdateTime(new Date());
        LambdaUpdateWrapper<MonitorAlarmRecord> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MonitorAlarmRecord::getCode, uuid);
        lambdaUpdateWrapper.eq(MonitorAlarmRecord::getWay, alarmWayEnums);
        this.monitorAlarmRecordDao.update(monitorAlarmRecord, lambdaUpdateWrapper);
    }

    /**
     * <p>
     * 把告警信息插入数据库
     * </p>
     *
     * @param alarm     告警信息
     * @param alarmUuid 告警代码
     * @author 皮锋
     * @custom.date 2020/5/13 16:21
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void insertMonitorAlarmRecordToDb(Alarm alarm, String alarmUuid) {
        // 告警定义编码
        String code = alarm.getCode();
        // 告警级别
        AlarmLevelEnums alarmLevelEnum = alarm.getAlarmLevel();
        // 监控类型
        MonitorTypeEnums monitorTypeEnum = alarm.getMonitorType();
        // 告警内容标题
        String alarmTitle = alarm.getTitle();
        // 告警内容
        String alarmMsg = alarm.getMsg();
        // 告警方式
        AlarmWayEnums[] alarmWayEnums = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getWayEnums();
        MonitorAlarmRecord monitorAlarmRecord = new MonitorAlarmRecord();
        monitorAlarmRecord.setCode(alarmUuid);
        monitorAlarmRecord.setAlarmDefCode(code);
        monitorAlarmRecord.setTitle(alarmTitle);
        monitorAlarmRecord.setContent(alarmMsg);
        monitorAlarmRecord.setLevel(alarmLevelEnum != null ? alarmLevelEnum.name() : null);
        monitorAlarmRecord.setType(monitorTypeEnum != null ? monitorTypeEnum.name() : null);
        // 没有告警级别
        if (alarmLevelEnum == null) {
            return;
        }
        // 告警方式
        for (AlarmWayEnums alarmWayEnum : alarmWayEnums) {
            switch (alarmWayEnum) {
                case SMS:
                    String[] phones = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getSmsProperties().getPhoneNumbers();
                    monitorAlarmRecord.setNumber(StringUtils.join(phones, ";"));
                    break;
                case MAIL:
                    String[] mails = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getMailProperties().getEmills();
                    monitorAlarmRecord.setNumber(StringUtils.join(mails, ";"));
                    break;
                default:
                    // 其它情况直接结束
                    return;
            }
            monitorAlarmRecord.setWay(alarmWayEnum.name());
            monitorAlarmRecord.setInsertTime(new Date());
            monitorAlarmRecord.setId(null);
            this.monitorAlarmRecordDao.insert(monitorAlarmRecord);
        }
    }

    /**
     * <p>
     * 是否发送告警信息
     * </p>
     *
     * @param result {@link Result} 返回结果
     * @param alarm  告警信息
     * @return 是 或 否
     * @author 皮锋
     * @custom.date 2020/9/14 12:37
     */
    private boolean isSendAlarm(Result result, Alarm alarm) {
        // 告警开关是否打开
        boolean isEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().isEnable();
        // 告警开关没有打开，不做处理，直接返回
        if (!isEnable) {
            String expMsg = "告警开关没有打开，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            return false;
        }
        // 是否是测试告警信息
        boolean isTest = alarm.isTest();
        // 是测试告警信息，不做处理，直接返回
        if (isTest) {
            String expMsg = "当前为测试信息，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            return false;
        }
        // 告警级别
        AlarmLevelEnums configAlarmLevelEnum = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getLevelEnum();
        // 告警级别
        AlarmLevelEnums levelEnum = alarm.getAlarmLevel();
        // 告警级别小于配置的告警级别，不做处理，直接返回
        if (!AlarmLevelEnums.isAlarm(configAlarmLevelEnum, levelEnum)) {
            String expMsg = "小于配置的告警级别，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            return false;
        }
        // 告警内容标题
        String alarmTitle = alarm.getTitle();
        // 没有告警标题，不做处理，直接返回
        if (StringUtils.isBlank(alarmTitle)) {
            String expMsg = "告警标题为空，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            return false;
        }
        // 告警内容
        String msg = alarm.getMsg();
        // 没有告警内容，不做处理，直接返回
        if (StringUtils.isBlank(msg)) {
            String expMsg = "告警内容为空，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            return false;
        }
        // 告警方式
        AlarmWayEnums[] alarmWayEnums = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getAlarmProperties().getWayEnums();
        // 没有配置告警方式，不做处理，直接返回
        if (ArrayUtil.isEmpty(alarmWayEnums)) {
            String expMsg = "没有配置告警方式，不发送告警信息！";
            this.wrapFailResult(result, expMsg);
            return false;
        }
        return true;
    }

    /**
     * <p>
     * 封装失败时的返回结果。
     * </p>
     *
     * @param result {@link Result} 返回结果
     * @param msg    结果信息
     * @author 皮锋
     * @custom.date 2020/9/13 21:35
     */
    private void wrapFailResult(Result result, String msg) {
        if (log.isDebugEnabled()) {
            log.debug(msg);
        }
        result.setSuccess(false);
        result.setMsg(msg);
    }

}
