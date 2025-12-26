package com.gitee.pifeng.monitoring.server.business.server.monitor.net;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorSubTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.util.CollectionUtils;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.common.util.Md5Utils;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.core.ServerPackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorNet;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorNetHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.INetHistoryService;
import com.gitee.pifeng.monitoring.server.business.server.service.INetService;
import com.gitee.pifeng.monitoring.server.constant.ComponentOrderConstants;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Collections;
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
@Order(ComponentOrderConstants.NET + 1)
@DisallowConcurrentExecution
public class NetMonitorJob extends QuartzJobBean {

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
     * 线程池
     */
    @Autowired
    @Qualifier("netMonitorThreadPoolExecutor")
    private ThreadPoolExecutor netMonitorThreadPoolExecutor;

    /**
     * 扫描数据库“MONITOR_NET”表中的所有网络信息，实时更新网络状态，发送告警。
     *
     * @param jobExecutionContext 作业执行上下文
     * @author 皮锋
     * @custom.date 2020/11/23 22:00
     */
    @Override
    protected void executeInternal(@NonNull JobExecutionContext jobExecutionContext) {
        // 是否监控网络
        boolean isEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getNetworkProperties().isEnable();
        if (!isEnable) {
            return;
        }
        // 是否监控网络状态
        boolean isStatusEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getNetworkProperties().getNetworkStatusProperties().isEnable();
        if (!isStatusEnable) {
            return;
        }
        synchronized (NetMonitorJob.class) {
            try {
                // 获取网络信息列表
                List<MonitorNet> monitorNets = this.netService.list(new LambdaQueryWrapper<MonitorNet>().eq(MonitorNet::getIpSource, NetUtils.getLocalIp()));
                // 打乱
                Collections.shuffle(monitorNets);
                // 按每个list大小为10拆分成多个list
                List<List<MonitorNet>> subMonitorNetLists = CollectionUtils.split(monitorNets, 10);
                for (List<MonitorNet> subMonitorNets : subMonitorNetLists) {
                    // 使用多线程，加快处理速度
                    this.netMonitorThreadPoolExecutor.execute(() -> {
                        // 循环处理每一个网络信息
                        for (MonitorNet monitorNet : subMonitorNets) {
                            // 是否开启监控（0：不开启监控；1：开启监控）
                            String isEnableMonitor = monitorNet.getIsEnableMonitor();
                            // 没有开启监控，直接跳过
                            if (!StringUtils.equals(ZeroOrOneConstants.ONE, isEnableMonitor)) {
                                continue;
                            }
                            // 目标IP地址
                            String ipTarget = monitorNet.getIpTarget();
                            // 测试IP地址能否ping通
                            boolean isConnected = false;
                            Map<String, Object> objectMap = Maps.newHashMap();
                            // 监控阈值
                            int threshold = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getThreshold();
                            for (int i = 0; i < threshold; i++) {
                                objectMap = NetUtils.isConnect(ipTarget);
                                // 测试IP地址能否ping通
                                isConnected = Boolean.parseBoolean(String.valueOf(objectMap.get("isConnect")));
                                if (isConnected) {
                                    break;
                                }
                            }
                            // 平均响应时间
                            Double avgTime = Double.valueOf(String.valueOf(objectMap.get("avgTime")));
                            // ping详情
                            String detail = String.valueOf(objectMap.get("detail"));
                            // 网络正常
                            if (isConnected) {
                                // 处理网络正常
                                this.connected(monitorNet, avgTime, detail);
                            }
                            // 网络异常
                            else {
                                // 处理网络异常
                                this.disconnected(monitorNet, avgTime, detail);
                            }
                        }
                    });
                }
            } catch (Exception e) {
                log.error("定时扫描数据库“MONITOR_NET”表中的所有网络信息异常！", e);
            }
        }
    }

    /**
     * <p>
     * 处理网络异常
     * </p>
     *
     * @param monitorNet 网络信息表
     * @param avgTime    平均响应时间
     * @param detail     详情
     * @author 皮锋
     * @custom.date 2020/11/23 11:19
     */
    private void disconnected(MonitorNet monitorNet, Double avgTime, String detail) {
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
        monitorNet.setPingDetail(detail);
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
        monitorNetHistory.setPingDetail(detail);
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
     * @param detail     详情
     * @author 皮锋
     * @custom.date 2020/11/23 11:12
     */
    private void connected(MonitorNet monitorNet, Double avgTime, String detail) {
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
        monitorNet.setPingDetail(detail);
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
        monitorNetHistory.setPingDetail(detail);
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
     * @throws NetException 获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/3/13 11:20
     */
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnum, AlarmReasonEnums alarmReasonEnum, MonitorNet net) throws NetException {
        // 告警是否打开
        boolean alarmEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getNetworkProperties().getNetworkStatusProperties().isAlarmEnable();
        if (!alarmEnable) {
            return;
        }
        // 是否开启告警（0：不开启告警；1：开启告警）
        String isEnableAlarm = net.getIsEnableAlarm();
        // 没有开启告警，直接结束
        if (!StringUtils.equals(ZeroOrOneConstants.ONE, isEnableAlarm)) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("源IP：").append(net.getIpSource())
                .append("，<br>目标IP：").append(net.getIpTarget());
        if (StringUtils.isNotBlank(net.getIpDesc())) {
            builder.append("，<br>描述：").append(net.getIpDesc());
        }
        if (StringUtils.isNotBlank(net.getMonitorEnv())) {
            builder.append("，<br>环境：").append(net.getMonitorEnv());
        }
        if (StringUtils.isNotBlank(net.getMonitorGroup())) {
            builder.append("，<br>分组：").append(net.getMonitorGroup());
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
                .monitorSubType(MonitorSubTypeEnums.SERVICE_STATUS)
                .alertedEntityId(String.valueOf(net.getId()))
                .build();
        AlarmPackage alarmPackage = this.serverPackageConstructor.structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
