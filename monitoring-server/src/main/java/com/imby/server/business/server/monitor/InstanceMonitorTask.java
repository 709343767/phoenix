package com.imby.server.business.server.monitor;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.imby.common.constant.AlarmLevelEnums;
import com.imby.common.constant.AlarmTypeEnums;
import com.imby.common.constant.ZeroOrOneConstants;
import com.imby.common.domain.Alarm;
import com.imby.common.dto.AlarmPackage;
import com.imby.common.exception.NetException;
import com.imby.common.util.DateTimeUtils;
import com.imby.server.business.server.core.PackageConstructor;
import com.imby.server.business.server.domain.Instance;
import com.imby.server.business.server.entity.MonitorInstance;
import com.imby.server.business.server.pool.InstancePool;
import com.imby.server.business.server.service.IAlarmService;
import com.imby.server.business.server.service.IInstanceService;
import com.imby.server.core.ThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 在项目启动后，定时扫描应用实例池中的所有应用实例，实时更新应用实例状态（应用实例池、数据库），发送告警。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/12 12:07
 */
@Slf4j
@Component
@Order(1)
public class InstanceMonitorTask implements CommandLineRunner {

    /**
     * 应用实例池
     */
    @Autowired
    private InstancePool instancePool;

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * 应用实例服务接口
     */
    @Autowired
    private IInstanceService instanceService;

    /**
     * <p>
     * 项目启动完成后延迟5秒钟启动定时任务，扫描应用实例池中的所有应用实例，实时更新应用实例状态，发送告警，
     * 然后在一次执行结束和下一次执行开始之间延迟30秒。
     * </p>
     *
     * @param args 传入的主方法参数
     * @author 皮锋
     * @custom.date 2020/4/7 22:00
     */
    @Override
    public void run(String... args) {
        ThreadPool.COMMON_SCHEDULED_THREAD_POOL.scheduleWithFixedDelay(() -> {
            try {
                // 循环所有应用实例
                for (Map.Entry<String, Instance> entry : this.instancePool.entrySet()) {
                    Instance instance = entry.getValue();
                    // 允许的误差时间
                    int thresholdSecond = instance.getThresholdSecond();
                    // 最后一次通过心跳包更新的时间
                    Date dateTime = instance.getDateTime();
                    // 判决时间
                    DateTime judgeDateTime = new DateTime(dateTime).plusSeconds(thresholdSecond);
                    // 注册上来的服务失去响应
                    if (judgeDateTime.isBeforeNow()) {
                        // 已经离线
                        if (!instance.isOnline()) {
                            continue;
                        }
                        // 离线
                        this.offLine(instance);
                    }
                    // 注册上来的服务恢复响应
                    else {
                        // 恢复在线
                        this.onLine(instance);
                    }
                }
                // 打印当前应用池中的所有应用情况
                log.info("当前应用实例池大小：{}，正常：{}，离线：{}，详细信息：{}",
                        this.instancePool.size(),
                        this.instancePool.entrySet().stream()
                                .filter(e -> e.getValue().isOnline()).count(),
                        this.instancePool.entrySet().stream().filter(e -> !e.getValue().isOnline()).count(),
                        this.instancePool.toJsonString());
            } catch (Exception e) {
                log.error("定时扫描应用实例池中的所有应用实例异常！", e);
            }
        }, 5, 30, TimeUnit.SECONDS);
    }

    /**
     * <p>
     * 处理恢复在线
     * </p>
     *
     * @param instance 实例
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/23 11:58
     */
    private void onLine(Instance instance) throws NetException, SigarException {
        instance.setOnline(true);
        // 是否已经发过离线告警信息
        boolean isLineAlarm = instance.isLineAlarm();
        if (isLineAlarm) {
            // 发送在线通知信息
            this.sendAlarmInfo("应用程序上线", AlarmLevelEnums.INFO, instance);
            instance.setLineAlarm(false);
        }
    }

    /**
     * <p>
     * 处理离线
     * </p>
     *
     * @param instance 实例
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/23 11:40
     */
    private void offLine(Instance instance) throws NetException, SigarException {
        // 离线
        instance.setOnline(false);
        // 是否已经发过离线告警信息
        boolean isLineAlarm = instance.isLineAlarm();
        // 没发送离线告警
        if (!isLineAlarm) {
            // 发送离线告警信息
            this.sendAlarmInfo("应用程序离线", AlarmLevelEnums.FATAL, instance);
            instance.setLineAlarm(true);
        }
        // 更新数据库
        this.updateDb(instance);
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnums 告警级别
     * @param instance        应用实例详情
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/13 11:20
     */
    @Async
    public void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnums, Instance instance) throws NetException, SigarException {
        String msg = "应用ID：" + instance.getInstanceId()
                + "，<br>应用名称：" + instance.getInstanceName()
                + "，<br>应用描述：" + instance.getInstanceDesc()
                + "，<br>应用端点：" + instance.getEndpoint()
                + "，<br>IP地址：" + instance.getIp()
                + "，<br>服务器：" + instance.getComputerName()
                + "，<br>时间：" + DateTimeUtils.dateToString(instance.getDateTime());
        Alarm alarm = Alarm.builder()
                .title(title)
                .msg(msg)
                .alarmLevel(alarmLevelEnums)
                .alarmType(AlarmTypeEnums.INSTANCE)
                .build();
        AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

    /**
     * <p>
     * 更新数据库中的应用实例
     * </p>
     *
     * @param instance 应用实例
     * @author 皮锋
     * @custom.date 2020/5/11 10:18
     */
    private void updateDb(Instance instance) {
        boolean isOnline = instance.isOnline();
        MonitorInstance monitorInstance = new MonitorInstance();
        monitorInstance.setUpdateTime(new Date());
        // 离线
        if (!isOnline) {
            monitorInstance.setIsOnLine(ZeroOrOneConstants.ZERO);
        }
        LambdaUpdateWrapper<MonitorInstance> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(MonitorInstance::getInstanceId, instance.getInstanceId());
        this.instanceService.updateInstance(monitorInstance, lambdaUpdateWrapper);
    }

}
