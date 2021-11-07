package com.gitee.pifeng.monitoring.server.business.server.monitor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.constant.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.threadpool.ThreadPool;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.common.util.Md5Utils;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorNet;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.INetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 在项目启动后，定时扫描数据库“MONITOR_NET”表中的所有网络信息，更新网络状态，发送告警。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/23 8:53
 */
@Slf4j
@Component
@Order(3)
public class NetMonitorTask implements CommandLineRunner {

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * 网络信息服务接口
     */
    @Autowired
    private INetService netService;

    /**
     * 项目启动完成后延迟10秒钟启动定时任务，扫描数据库“MONITOR_NET”表中的所有网络信息，实时更新网络状态，发送告警，
     * 然后在一次执行结束和下一次执行开始之间延迟5分钟。
     *
     * @param args 传入的主方法参数
     * @author 皮锋
     * @custom.date 2020/11/23 22:00
     */
    @Override
    public void run(String... args) {
        ThreadPool.COMMON_IO_INTENSIVE_SCHEDULED_THREAD_POOL.scheduleWithFixedDelay(() -> {
            // 是否监控网络
            boolean isMonitoringEnable = MonitoringConfigPropertiesLoader.getMonitoringProperties().getNetworkProperties().isEnable();
            if (!isMonitoringEnable) {
                return;
            }
            try {
                // 获取网络信息列表
                List<MonitorNet> monitorNets = this.netService.list(new LambdaQueryWrapper<MonitorNet>().eq(MonitorNet::getIpSource, NetUtils.getLocalIp()));
                // 循环处理每一个网络信息
                for (MonitorNet monitorNet : monitorNets) {
                    // 目标IP地址
                    String ipTarget = monitorNet.getIpTarget();
                    // 使用多线程，加快处理速度
                    ThreadPoolExecutor threadPoolExecutor = ThreadPool.COMMON_IO_INTENSIVE_THREAD_POOL;
                    threadPoolExecutor.execute(() -> {
                        // 测试IP地址能否ping通
                        boolean isConnected = NetUtils.isConnect(ipTarget);
                        // 网络正常
                        if (isConnected) {
                            // 处理网络正常
                            this.connected(monitorNet);
                        }
                        // 网络异常
                        else {
                            // 处理网络异常
                            this.disconnected(monitorNet);
                        }
                    });
                }
            } catch (NetException e) {
                log.error("定时扫描数据库“MONITOR_NET”表中的所有网络信息异常！", e);
            }
        }, 10, 300, TimeUnit.SECONDS);
    }

    /**
     * <p>
     * 处理网络异常
     * </p>
     *
     * @param monitorNet 网络信息表
     * @author 皮锋
     * @custom.date 2020/11/23 11:19
     */
    private void disconnected(MonitorNet monitorNet) {
        try {
            this.sendAlarmInfo("网络中断", AlarmLevelEnums.FATAL, AlarmReasonEnums.NORMAL_2_ABNORMAL, monitorNet);
        } catch (Exception e) {
            log.error("网络告警异常！", e);
        }
        // 原本是在线或者未知
        String status = monitorNet.getStatus();
        if (StringUtils.equalsIgnoreCase(status, ZeroOrOneConstants.ONE) || StringUtils.isBlank(status)) {
            // 离线次数 +1
            int offlineCount = monitorNet.getOfflineCount() == null ? 0 : monitorNet.getOfflineCount();
            monitorNet.setOfflineCount(offlineCount + 1);
        }
        monitorNet.setStatus(ZeroOrOneConstants.ZERO);
        monitorNet.setUpdateTime(new Date());
        // 更新数据库
        this.netService.updateById(monitorNet);
    }

    /**
     * <p>
     * 处理网络正常
     * </p>
     *
     * @param monitorNet 网络信息
     * @author 皮锋
     * @custom.date 2020/11/23 11:12
     */
    private void connected(MonitorNet monitorNet) {
        try {
            if (StringUtils.isNotBlank(monitorNet.getStatus())) {
                this.sendAlarmInfo("网络恢复", AlarmLevelEnums.INFO, AlarmReasonEnums.ABNORMAL_2_NORMAL, monitorNet);
            }
        } catch (Exception e) {
            log.error("网络告警异常！", e);
        }
        monitorNet.setStatus(ZeroOrOneConstants.ONE);
        monitorNet.setUpdateTime(new Date());
        // 更新数据库
        this.netService.updateById(monitorNet);
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnum  告警级别
     * @param alarmReasonEnum 告警原因
     * @param net             网络信息
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/13 11:20
     */
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnum, AlarmReasonEnums alarmReasonEnum, MonitorNet net) throws NetException, SigarException {
        StringBuilder builder = new StringBuilder();
        builder.append("源IP：").append(net.getIpSource())
                .append("，<br>目标IP：").append(net.getIpTarget());
        if (StringUtils.isNotBlank(net.getIpDesc())) {
            builder.append("，<br>描述：").append(net.getIpDesc());
        }
        builder.append("，<br>时间：").append(DateTimeUtils.dateToString(new Date()));
        Alarm alarm = Alarm.builder()
                // 保证code的唯一性
                .code(Md5Utils.encrypt32(net.getIpSource() + net.getIpTarget()))
                .title(title)
                .msg(builder.toString())
                .alarmLevel(alarmLevelEnum)
                .alarmReason(alarmReasonEnum)
                .monitorType(MonitorTypeEnums.NET)
                .build();
        AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
