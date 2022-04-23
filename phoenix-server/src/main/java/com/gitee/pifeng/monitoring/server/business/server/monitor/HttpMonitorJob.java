package com.gitee.pifeng.monitoring.server.business.server.monitor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.constant.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.threadpool.ThreadPool;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.common.util.Md5Utils;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.plug.util.EnumPoolingHttpUtils;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.core.PackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorHttp;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorHttpHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.IHttpHistoryService;
import com.gitee.pifeng.monitoring.server.business.server.service.IHttpService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.hyperic.sigar.SigarException;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 在项目启动后，定时扫描数据库“MONITOR_HTTP”表中的所有HTTP信息，更新HTTP服务状态，发送告警。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/13 9:11
 */
@Slf4j
@Component
@Order(8)
public class HttpMonitorJob extends QuartzJobBean {

    /**
     * 告警服务接口
     */
    @Autowired
    private IAlarmService alarmService;

    /**
     * HTTP信息服务接口
     */
    @Autowired
    private IHttpService httpService;

    /**
     * HTTP信息历史记录服务接口
     */
    @Autowired
    private IHttpHistoryService httpHistoryService;

    /**
     * <p>
     * 扫描数据库“MONITOR_HTTP”表中的所有HTTP信息，实时更新HTTP服务状态，发送告警。
     * </p>
     *
     * @param jobExecutionContext 作业执行上下文
     * @author 皮锋
     * @custom.date 2022/1/11 16:31
     */
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        // 是否监控HTTP服务
        boolean isMonitoringEnable = MonitoringConfigPropertiesLoader.getMonitoringProperties().getHttpProperties().isEnable();
        if (!isMonitoringEnable) {
            return;
        }
        try {
            // 获取HTTP信息列表
            List<MonitorHttp> monitorHttps = this.httpService.list(new LambdaQueryWrapper<MonitorHttp>().eq(MonitorHttp::getHostnameSource, NetUtils.getLocalIp()));
            // 循环处理每一个HTTP信息
            for (MonitorHttp monitorHttp : monitorHttps) {
                // 使用多线程，加快处理速度
                ThreadPoolExecutor threadPoolExecutor = ThreadPool.COMMON_IO_INTENSIVE_THREAD_POOL;
                threadPoolExecutor.execute(() -> {
                    // 请求方法
                    String method = monitorHttp.getMethod();
                    // GET请求
                    if (StringUtils.equalsIgnoreCase(HttpMethod.GET.name(), method)) {
                        // 检测HTTP GET请求
                        this.checkGet(monitorHttp);
                    }
                    // POST请求
                    if (StringUtils.equalsIgnoreCase(HttpMethod.POST.name(), method)) {
                        // 检测HTTP POST请求
                        this.checkPost(monitorHttp);
                    }
                });
            }
        } catch (Exception e) {
            log.error("定时扫描数据库“MONITOR_HTTP”表中的所有HTTP信息异常！", e);
        }
    }

    /**
     * <p>
     * 检测HTTP POST请求
     * </p>
     *
     * @param monitorHttp HTTP信息
     * @author 皮锋
     * @custom.date 2022/4/15 13:09
     */
    private void checkPost(MonitorHttp monitorHttp) {
        try {
            //HTTP线程池工具类
            EnumPoolingHttpUtils httpClient = EnumPoolingHttpUtils.getInstance();
            // URL地址（目的地）
            String urlTarget = monitorHttp.getUrlTarget();
            // 请求参数
            String parameter = monitorHttp.getParameter();
            Map<String, Object> stringObjectMap = httpClient.sendHttpPost(urlTarget, parameter);
            // 状态码
            int statusCode = Integer.parseInt(String.valueOf(stringObjectMap.get("statusCode")));
            // 响应时间
            long avgTime = Long.parseLong(String.valueOf(stringObjectMap.get("avgTime")));
            // 成功
            if (statusCode == HttpStatus.SC_OK) {
                // 处理HTTP服务正常
                this.connected(monitorHttp, statusCode, avgTime);
            } else {
                // 异常信息
                String excMessage = stringObjectMap.get("excMessage") != null ? String.valueOf(stringObjectMap.get("excMessage")) : null;
                // 处理HTTP服务异常
                this.disconnected(monitorHttp, statusCode, excMessage, avgTime);
            }
        } catch (Exception e) {
            log.error("检测HTTP POST请求异常！", e);
            // 处理HTTP服务异常
            this.disconnected(monitorHttp, 500, e.getMessage(), 30000);
        }
    }

    /**
     * <p>
     * 检测HTTP GET请求
     * </p>
     *
     * @param monitorHttp HTTP信息
     * @author 皮锋
     * @custom.date 2022/4/15 12:44
     */
    private void checkGet(MonitorHttp monitorHttp) {
        try {
            //HTTP线程池工具类
            EnumPoolingHttpUtils httpClient = EnumPoolingHttpUtils.getInstance();
            // URL地址（目的地）
            String urlTarget = monitorHttp.getUrlTarget();
            Map<String, Object> stringObjectMap = httpClient.sendHttpGet(urlTarget);
            // 状态码
            int statusCode = Integer.parseInt(String.valueOf(stringObjectMap.get("statusCode")));
            // 响应时间
            long avgTime = Long.parseLong(String.valueOf(stringObjectMap.get("avgTime")));
            // 成功
            if (statusCode == HttpStatus.SC_OK) {
                // 处理HTTP服务正常
                this.connected(monitorHttp, statusCode, avgTime);
            } else {
                // 异常信息
                String excMessage = stringObjectMap.get("excMessage") != null ? String.valueOf(stringObjectMap.get("excMessage")) : null;
                // 处理HTTP服务异常
                this.disconnected(monitorHttp, statusCode, excMessage, avgTime);
            }
        } catch (Exception e) {
            log.error("检测HTTP GET请求异常！", e);
            // 处理HTTP服务异常
            this.disconnected(monitorHttp, 500, e.getMessage(), 30000);
        }
    }

    /**
     * <p>
     * 处理HTTP服务异常
     * </p>
     *
     * @param monitorHttp HTTP信息表
     * @param statusCode  http状态码
     * @param excMessage  异常信息
     * @param avgTime     平均时间（毫秒）
     * @author 皮锋
     * @custom.date 2022/4/13 14:05
     */
    private void disconnected(MonitorHttp monitorHttp, int statusCode, String excMessage, long avgTime) {
        try {
            this.sendAlarmInfo("HTTP服务中断", AlarmLevelEnums.FATAL, AlarmReasonEnums.NORMAL_2_ABNORMAL, monitorHttp);
        } catch (Exception e) {
            log.error("HTTP服务告警异常！", e);
        }
        // 原本是未知或者200
        Integer status = monitorHttp.getStatus();
        if (null == status || status == HttpStatus.SC_OK) {
            // 离线次数 +1
            int offlineCount = monitorHttp.getOfflineCount() == null ? 0 : monitorHttp.getOfflineCount();
            monitorHttp.setOfflineCount(offlineCount + 1);
        }
        Date date = new Date();
        monitorHttp.setStatus(statusCode);
        monitorHttp.setExcMessage(excMessage);
        monitorHttp.setAvgTime(avgTime);
        monitorHttp.setUpdateTime(date);
        // 更新数据库
        this.httpService.updateById(monitorHttp);
        // 添加历史记录
        MonitorHttpHistory monitorHttpHistory = new MonitorHttpHistory();
        monitorHttpHistory.setHttpId(monitorHttp.getId());
        monitorHttpHistory.setHostnameSource(monitorHttp.getHostnameSource());
        monitorHttpHistory.setUrlTarget(monitorHttp.getUrlTarget());
        monitorHttpHistory.setMethod(monitorHttp.getMethod());
        monitorHttpHistory.setParameter(monitorHttp.getParameter());
        monitorHttpHistory.setDescr(monitorHttp.getDescr());
        monitorHttpHistory.setAvgTime(monitorHttp.getAvgTime());
        monitorHttpHistory.setStatus(monitorHttp.getStatus());
        monitorHttpHistory.setExcMessage(monitorHttp.getExcMessage());
        monitorHttpHistory.setOfflineCount(monitorHttp.getOfflineCount());
        monitorHttpHistory.setInsertTime(date);
        monitorHttpHistory.setUpdateTime(date);
        this.httpHistoryService.save(monitorHttpHistory);
    }

    /**
     * <p>
     * 处理HTTP服务正常
     * </p>
     *
     * @param monitorHttp HTTP信息表
     * @param statusCode  http状态码
     * @param avgTime     平均时间（毫秒）
     * @author 皮锋
     * @custom.date 2022/4/13 13:05
     */
    private void connected(MonitorHttp monitorHttp, int statusCode, long avgTime) {
        try {
            if (null != monitorHttp.getStatus()) {
                this.sendAlarmInfo("HTTP服务恢复", AlarmLevelEnums.INFO, AlarmReasonEnums.ABNORMAL_2_NORMAL, monitorHttp);
            }
        } catch (Exception e) {
            log.error("HTTP服务告警异常！", e);
        }
        Date date = new Date();
        monitorHttp.setStatus(statusCode);
        monitorHttp.setAvgTime(avgTime);
        monitorHttp.setUpdateTime(date);
        // 更新数据库
        this.httpService.updateById(monitorHttp);
        // 添加历史记录
        MonitorHttpHistory monitorHttpHistory = new MonitorHttpHistory();
        monitorHttpHistory.setHttpId(monitorHttp.getId());
        monitorHttpHistory.setHostnameSource(monitorHttp.getHostnameSource());
        monitorHttpHistory.setUrlTarget(monitorHttp.getUrlTarget());
        monitorHttpHistory.setMethod(monitorHttp.getMethod());
        monitorHttpHistory.setParameter(monitorHttp.getParameter());
        monitorHttpHistory.setDescr(monitorHttp.getDescr());
        monitorHttpHistory.setAvgTime(monitorHttp.getAvgTime());
        monitorHttpHistory.setStatus(monitorHttp.getStatus());
        monitorHttpHistory.setOfflineCount(monitorHttp.getOfflineCount());
        monitorHttpHistory.setInsertTime(date);
        monitorHttpHistory.setUpdateTime(date);
        this.httpHistoryService.save(monitorHttpHistory);
    }

    /**
     * <p>
     * 发送告警信息
     * </p>
     *
     * @param title           告警标题
     * @param alarmLevelEnum  告警级别
     * @param alarmReasonEnum 告警原因
     * @param monitorHttp     HTTP信息
     * @author 皮锋
     * @custom.date 2022/4/13 13:14
     */
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnum, AlarmReasonEnums alarmReasonEnum, MonitorHttp monitorHttp) throws SigarException {
        StringBuilder builder = new StringBuilder();
        // 请求方法
        String method = monitorHttp.getMethod();
        // 请求参数
        String parameter = monitorHttp.getParameter();
        // 主机名（来源）
        String hostnameSource = monitorHttp.getHostnameSource();
        // URL地址（目的地）
        String urlTarget = monitorHttp.getUrlTarget();
        builder.append("源主机：").append(hostnameSource)
                .append("，<br>目标URL：").append(urlTarget)
                .append("，<br>请求方法：").append(method);
        // POST请求
        if (StringUtils.equalsIgnoreCase(HttpMethod.POST.name(), method) && StringUtils.isNotBlank(parameter)) {
            builder.append("，<br>请求参数：").append(parameter);
        }
        if (StringUtils.isNotBlank(monitorHttp.getDescr())) {
            builder.append("，<br>描述：").append(monitorHttp.getDescr());
        }
        builder.append("，<br>时间：").append(DateTimeUtils.dateToString(new Date()));
        Alarm alarm = Alarm.builder()
                // 保证code的唯一性
                .code(Md5Utils.encrypt32(MonitorTypeEnums.HTTP4SERVICE.name() + hostnameSource + urlTarget + method))
                .title(title)
                .msg(builder.toString())
                .alarmLevel(alarmLevelEnum)
                .alarmReason(alarmReasonEnum)
                .monitorType(MonitorTypeEnums.HTTP4SERVICE)
                .build();
        AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }
}
