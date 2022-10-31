package com.gitee.pifeng.monitoring.server.business.server.monitor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmReasonEnums;
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
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorNetHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.INetHistoryService;
import com.gitee.pifeng.monitoring.server.business.server.service.INetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.SigarException;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

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
public class NetMonitorJob extends QuartzJobBean {

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
     * 网络信息历史记录服务接口
     */
    @Autowired
    private INetHistoryService netHistoryService;

    /**
     * 扫描数据库“MONITOR_NET”表中的所有网络信息，实时更新网络状态，发送告警。
     *
     * @param jobExecutionContext 作业执行上下文
     * @author 皮锋
     * @custom.date 2020/11/23 22:00
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
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
                    Map<String, Object> objectMap = NetUtils.isConnect(ipTarget);
                    // 测试IP地址能否ping通
                    boolean isConnected = Boolean.parseBoolean(String.valueOf(objectMap.get("isConnect")));
                    // 平均响应时间
                    Double avgTime = Double.valueOf(String.valueOf(objectMap.get("avgTime")));
                    // 网络正常
                    if (isConnected) {
                        // 处理网络正常
                        this.connected(monitorNet, avgTime);
                    }
                    // 网络异常
                    else {
                        // 处理网络异常
                        this.disconnected(monitorNet, avgTime);
                    }
                });
            }
        } catch (NetException e) {
            log.error("定时扫描数据库“MONITOR_NET”表中的所有网络信息异常！", e);
        }
    }

    /**
     * <p>
     * 处理网络异常
     * </p>
     *
     * @param monitorNet 网络信息表
     * @param avgTime    平均响应时间
     * @author 皮锋
     * @custom.date 2020/11/23 11:19
     */
    private void disconnected(MonitorNet monitorNet, Double avgTime) {
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
        Date date = new Date();
        monitorNet.setStatus(ZeroOrOneConstants.ZERO);
        monitorNet.setAvgTime(avgTime);
        monitorNet.setUpdateTime(date);
        // 更新数据库
        this.netService.updateById(monitorNet);
        // 添加及时记录
        MonitorNetHistory monitorNetHistory = new MonitorNetHistory();
        monitorNetHistory.setNetId(monitorNet.getId());
        monitorNetHistory.setIpSource(monitorNet.getIpSource());
        monitorNetHistory.setIpTarget(monitorNet.getIpTarget());
        monitorNetHistory.setIpDesc(monitorNet.getIpDesc());
        monitorNetHistory.setStatus(monitorNet.getStatus());
        monitorNetHistory.setAvgTime(monitorNet.getAvgTime());
        monitorNetHistory.setOfflineCount(monitorNet.getOfflineCount());
        monitorNetHistory.setInsertTime(date);
        monitorNetHistory.setUpdateTime(date);
        this.netHistoryService.save(monitorNetHistory);
    }

    /**
     * <p>
     * 处理网络正常
     * </p>
     *
     * @param monitorNet 网络信息
     * @param avgTime    平均响应时间
     * @author 皮锋
     * @custom.date 2020/11/23 11:12
     */
    private void connected(MonitorNet monitorNet, Double avgTime) {
        try {
            if (StringUtils.isNotBlank(monitorNet.getStatus())) {
                this.sendAlarmInfo("网络恢复", AlarmLevelEnums.INFO, AlarmReasonEnums.ABNORMAL_2_NORMAL, monitorNet);
            }
        } catch (Exception e) {
            log.error("网络告警异常！", e);
        }
        Date date = new Date();
        monitorNet.setStatus(ZeroOrOneConstants.ONE);
        monitorNet.setAvgTime(avgTime);
        monitorNet.setUpdateTime(date);
        // 更新数据库
        this.netService.updateById(monitorNet);
        // 添加及时记录
        MonitorNetHistory monitorNetHistory = new MonitorNetHistory();
        monitorNetHistory.setNetId(monitorNet.getId());
        monitorNetHistory.setIpSource(monitorNet.getIpSource());
        monitorNetHistory.setIpTarget(monitorNet.getIpTarget());
        monitorNetHistory.setIpDesc(monitorNet.getIpDesc());
        monitorNetHistory.setStatus(monitorNet.getStatus());
        monitorNetHistory.setAvgTime(monitorNet.getAvgTime());
        monitorNetHistory.setOfflineCount(monitorNet.getOfflineCount());
        monitorNetHistory.setInsertTime(date);
        monitorNetHistory.setUpdateTime(date);
        this.netHistoryService.save(monitorNetHistory);
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
