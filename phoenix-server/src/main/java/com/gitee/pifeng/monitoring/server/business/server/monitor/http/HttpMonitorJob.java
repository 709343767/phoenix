package com.gitee.pifeng.monitoring.server.business.server.monitor.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmLevelEnums;
import com.gitee.pifeng.monitoring.common.constant.alarm.AlarmReasonEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorSubTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.util.CollectionUtils;
import com.gitee.pifeng.monitoring.common.util.DateTimeUtils;
import com.gitee.pifeng.monitoring.common.util.LayUiUtils;
import com.gitee.pifeng.monitoring.common.util.Md5Utils;
import com.gitee.pifeng.monitoring.common.util.server.NetUtils;
import com.gitee.pifeng.monitoring.plug.core.EnumPoolingHttpClient;
import com.gitee.pifeng.monitoring.server.business.server.core.MonitoringConfigPropertiesLoader;
import com.gitee.pifeng.monitoring.server.business.server.core.ServerPackageConstructor;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorHttp;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorHttpHistory;
import com.gitee.pifeng.monitoring.server.business.server.service.IAlarmService;
import com.gitee.pifeng.monitoring.server.business.server.service.IHttpHistoryService;
import com.gitee.pifeng.monitoring.server.business.server.service.IHttpService;
import com.gitee.pifeng.monitoring.server.constant.ComponentOrderConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
 * 在项目启动后，定时扫描数据库“MONITOR_HTTP”表中的所有HTTP信息，更新HTTP服务状态，发送告警。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/13 9:11
 */
@Slf4j
@Component
@Order(ComponentOrderConstants.HTTP + 1)
@DisallowConcurrentExecution
public class HttpMonitorJob extends QuartzJobBean {

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
     * 线程池
     */
    @Autowired
    @Qualifier("httpMonitorThreadPoolExecutor")
    private ThreadPoolExecutor httpMonitorThreadPoolExecutor;

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
    protected void executeInternal(@NonNull JobExecutionContext jobExecutionContext) {
        // 是否监控HTTP服务
        boolean isEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getHttpProperties().isEnable();
        if (!isEnable) {
            return;
        }
        // 是否监控HTTP状态
        boolean isStatusEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getHttpProperties().getHttpStatusProperties().isEnable();
        if (!isStatusEnable) {
            return;
        }
        synchronized (HttpMonitorJob.class) {
            try {
                // 获取HTTP信息列表
                List<MonitorHttp> monitorHttps = this.httpService.list(new LambdaQueryWrapper<MonitorHttp>().eq(MonitorHttp::getHostnameSource, NetUtils.getLocalIp()));
                // 打乱
                Collections.shuffle(monitorHttps);
                // 按每个list大小为10拆分成多个list
                List<List<MonitorHttp>> subMonitorHttpLists = CollectionUtils.split(monitorHttps, 10);
                for (List<MonitorHttp> subMonitorHttps : subMonitorHttpLists) {
                    // 使用多线程，加快处理速度
                    this.httpMonitorThreadPoolExecutor.execute(() -> {
                        // 循环处理每一个HTTP信息
                        for (MonitorHttp monitorHttp : subMonitorHttps) {
                            // 是否开启监控（0：不开启监控；1：开启监控）
                            String isEnableMonitor = monitorHttp.getIsEnableMonitor();
                            // 没有开启监控，直接跳过
                            if (!StringUtils.equals(ZeroOrOneConstants.ONE, isEnableMonitor)) {
                                continue;
                            }
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
                        }
                    });
                }
            } catch (Exception e) {
                log.error("定时扫描数据库“MONITOR_HTTP”表中的所有HTTP信息异常！", e);
            }
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
            EnumPoolingHttpClient httpClient = EnumPoolingHttpClient.getInstance();
            // URL地址（目的地）
            String urlTarget = monitorHttp.getUrlTarget();
            // 媒体类型
            String contentType = monitorHttp.getContentType();
            // 请求头参数（LayUiTable数据中，过滤出选中的数据）
            String headerParameter = LayUiUtils.filterCheckedWithLayUiTable(monitorHttp.getHeaderParameter());
            // 请求体参数
            String bodyParameter = monitorHttp.getBodyParameter();
            if (StringUtils.isNotBlank(bodyParameter)) {
                JSONObject bodyParameterJson = JSON.parseObject(bodyParameter);
                if (bodyParameterJson != null) {
                    if (StringUtils.equalsIgnoreCase(MediaType.APPLICATION_FORM_URLENCODED_VALUE, contentType)) {
                        JSONArray bodyApplicationFormUrlencodedParameterJsons = bodyParameterJson.getJSONArray("bodyApplicationFormUrlencodedParameter");
                        // LayUiTable数据中，过滤出选中的数据
                        bodyParameter = LayUiUtils.filterCheckedWithLayUiTable(bodyApplicationFormUrlencodedParameterJsons.toJSONString());
                    } else if (StringUtils.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE, contentType)) {
                        bodyParameter = String.valueOf(bodyParameterJson.get("bodyApplicationJsonParameter"));
                    }
                }
            }
            Map<String, Object> stringObjectMap = httpClient.sendHttpPost(urlTarget, contentType, headerParameter, bodyParameter);
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
            EnumPoolingHttpClient httpClient = EnumPoolingHttpClient.getInstance();
            // URL地址（目的地）
            String urlTarget = monitorHttp.getUrlTarget();
            // 请求头参数（LayUiTable数据中，过滤出选中的数据）
            String headerParameter = LayUiUtils.filterCheckedWithLayUiTable(monitorHttp.getHeaderParameter());
            Map<String, Object> stringObjectMap = httpClient.sendHttpGet(urlTarget, headerParameter);
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
        monitorHttpHistory.setContentType(monitorHttp.getContentType());
        monitorHttpHistory.setHeaderParameter(monitorHttp.getHeaderParameter());
        monitorHttpHistory.setBodyParameter(monitorHttp.getBodyParameter());
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
        monitorHttp.setExcMessage(null);
        monitorHttp.setUpdateTime(date);
        // 更新数据库
        this.httpService.updateById(monitorHttp);
        // 添加历史记录
        MonitorHttpHistory monitorHttpHistory = new MonitorHttpHistory();
        monitorHttpHistory.setHttpId(monitorHttp.getId());
        monitorHttpHistory.setHostnameSource(monitorHttp.getHostnameSource());
        monitorHttpHistory.setUrlTarget(monitorHttp.getUrlTarget());
        monitorHttpHistory.setMethod(monitorHttp.getMethod());
        monitorHttpHistory.setContentType(monitorHttp.getContentType());
        monitorHttpHistory.setHeaderParameter(monitorHttp.getHeaderParameter());
        monitorHttpHistory.setBodyParameter(monitorHttp.getBodyParameter());
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
    private void sendAlarmInfo(String title, AlarmLevelEnums alarmLevelEnum, AlarmReasonEnums alarmReasonEnum, MonitorHttp monitorHttp) {
        // 告警是否打开
        boolean alarmEnable = this.monitoringConfigPropertiesLoader.getMonitoringProperties().getHttpProperties().getHttpStatusProperties().isAlarmEnable();
        if (!alarmEnable) {
            return;
        }
        // 是否开启告警（0：不开启告警；1：开启告警）
        String isEnableAlarm = monitorHttp.getIsEnableAlarm();
        // 没有开启告警，直接结束
        if (!StringUtils.equals(ZeroOrOneConstants.ONE, isEnableAlarm)) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        // 请求方法
        String method = monitorHttp.getMethod();
        // 媒体类型
        String contentType = monitorHttp.getContentType();
        // 请求头参数
        String headerParameter = monitorHttp.getHeaderParameter();
        headerParameter = LayUiUtils.jsonArrayStr2ArrayStrWithLayUiTable(headerParameter, true);
        // 请求体参数
        String bodyParameter = monitorHttp.getBodyParameter();
        // 主机名（来源）
        String hostnameSource = monitorHttp.getHostnameSource();
        // URL地址（目的地）
        String urlTarget = monitorHttp.getUrlTarget();
        builder.append("源主机：").append(hostnameSource)
                .append("，<br>目标URL：").append(urlTarget)
                .append("，<br>请求方法：").append(method)
                .append("，<br>请求头：").append(headerParameter);
        // POST请求
        if (StringUtils.equalsIgnoreCase(HttpMethod.POST.name(), method)) {
            builder.append("，<br>媒体类型：").append(contentType);
            if (StringUtils.isNotBlank(bodyParameter)) {
                JSONObject bodyParameterJson = JSON.parseObject(bodyParameter);
                if (StringUtils.equalsIgnoreCase(contentType, MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
                    JSONArray bodyApplicationFormUrlencodedParameterJsons = bodyParameterJson.getJSONArray("bodyApplicationFormUrlencodedParameter");
                    String bodyApplicationFormUrlencodedParameterJsonStr = bodyApplicationFormUrlencodedParameterJsons.toJSONString();
                    bodyParameter = LayUiUtils.jsonArrayStr2ArrayStrWithLayUiTable(bodyApplicationFormUrlencodedParameterJsonStr, true);
                    builder.append("，<br>请求体：").append(bodyParameter);
                } else if (StringUtils.equalsIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE)) {
                    String bodyJsonParameter = String.valueOf(bodyParameterJson.get("bodyApplicationJsonParameter"));
                    builder.append("，<br>请求体：").append(bodyJsonParameter);
                }
            }
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
                .monitorSubType(MonitorSubTypeEnums.SERVICE_STATUS)
                .alertedEntityId(String.valueOf(monitorHttp.getId()))
                .build();
        AlarmPackage alarmPackage = this.serverPackageConstructor.structureAlarmPackage(alarm);
        this.alarmService.dealAlarmPackage(alarmPackage);
    }

}
