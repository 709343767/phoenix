package com.gitee.pifeng.monitoring.server.business.server.monitor;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.core.ServerPackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.constant.ComponentOrderConstants;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 在项目启动后，定时扫描数据库“MONITOR_ALARM_RECORD”表，看是否有需要定时告警的数据，有则发送告警。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/5/3 10:54
 */
@Slf4j
@Component
@Order(ComponentOrderConstants.OTHER + 2)
@DisallowConcurrentExecution
public class AlarmMonitorJob extends QuartzJobBean {

    /**
     * 监控配置属性加载器
     */
    @Autowired
    private MonitoringConfigPropertiesLoader monitoringConfigPropertiesLoader;

    /**
     * 服务端包构造器
     */
    @Autowired
    private ServerPackageConstructor serverPackageConstructor;

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * 查询告警记录表，看是否有需要定时告警的数据，有则发送告警。
     *
     * @param jobExecutionContext 作业执行上下文
     * @author 皮锋
     * @custom.date 2024/5/03 10:58
     */
    @Override
    protected void executeInternal(@NonNull JobExecutionContext jobExecutionContext) {
        // 当前时间
        DateTime now = new DateTime();
        // 过去24小时
        DateTime twentyFourHoursAgo = DateUtil.offsetDay(now, -1);
        // 获取过去24小时的静默告警记录数
        int silenceAlarmCount = this.alarmService.getSilenceAlarmCount(twentyFourHoursAgo, now);
        if (silenceAlarmCount > 0) {
            this.sendAlarmInfo("静默告警提醒",
                    "您有" + silenceAlarmCount + "条未查看的静默告警消息，请及时登录监控平台查看！",
                    AlarmLevelEnums.INFO,
                    AlarmReasonEnums.IGNORE,
                    MonitorTypeEnums.CUSTOM,
                    true);
        }
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param msg             告警内容
     * @param alarmLevelEnum  告警级别
     * @param alarmReasonEnum 告警原因
     * @param monitorTypeEnum 监控类型
     * @param isIgnoreSilence 是否无视静默告警
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/3/13 11:20
     */
    private void sendAlarmInfo(String title,
                               String msg,
                               AlarmLevelEnums alarmLevelEnum,
                               AlarmReasonEnums alarmReasonEnum,
                               MonitorTypeEnums monitorTypeEnum,
                               boolean isIgnoreSilence)
            throws NetException {
        // 告警是否打开
        boolean alarmEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getInstanceProperties().getInstanceStatusProperties().isAlarmEnable();
        if (!alarmEnable) {
            return;
        }
        Alarm alarm = Alarm.builder()
                .title(title)
                .msg(msg)
                .alarmLevel(alarmLevelEnum)
                .alarmReason(alarmReasonEnum)
                .monitorType(monitorTypeEnum)
                .isIgnoreSilence(isIgnoreSilence)
                .build();
        AlarmPackage alarmPackage = this.serverPackageConstructor.structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}