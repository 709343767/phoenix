package com.gitee.pifeng.monitoring.plug;

import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.monitoring.common.constant.EndpointTypeEnums;
import com.gitee.pifeng.monitoring.common.constant.ThreadTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Alarm;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.AlarmPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.exception.ErrorConfigParamException;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.common.exception.NotFoundConfigFileException;
import com.gitee.pifeng.monitoring.common.exception.NotFoundConfigParamException;
import com.gitee.pifeng.monitoring.common.init.InitBanner;
import com.gitee.pifeng.monitoring.common.property.client.MonitoringProperties;
import com.gitee.pifeng.monitoring.common.threadpool.MonitoredScheduledThreadPoolExecutor;
import com.gitee.pifeng.monitoring.plug.constant.UrlConstants;
import com.gitee.pifeng.monitoring.plug.core.*;
import com.gitee.pifeng.monitoring.plug.scheduler.BusinessBuryingPointScheduler;
import com.gitee.pifeng.monitoring.plug.scheduler.HeartbeatTaskScheduler;
import com.gitee.pifeng.monitoring.plug.scheduler.JvmTaskScheduler;
import com.gitee.pifeng.monitoring.plug.scheduler.ServerTaskScheduler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 监控客户端入口类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午3:00:25
 */
@Slf4j
public class Monitor {

    /**
     * 客户端包构造器
     */
    private static final ClientPackageConstructor CLIENT_PACKAGE_CONSTRUCTOR = ClientPackageConstructor.getInstance();

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/9/16 14:17
     */
    private Monitor() {
    }

    /**
     * <p>
     * 开启监控
     * </p>
     *
     * @return {@link MonitoringProperties}
     * @author 皮锋
     * @custom.date 2020年3月5日 下午3:01:38
     */
    @SneakyThrows
    public static MonitoringProperties start() {
        return run(null, null, null);
    }

    /**
     * <p>
     * 开启监控，自定义配置文件路径和名字
     * </p>
     *
     * @param configPath 配置文件路径
     * @param configName 配置文件名字
     * @return {@link MonitoringProperties}
     * @author 皮锋
     * @custom.date 2020年3月5日 下午4:06:31
     */
    @SneakyThrows
    public static MonitoringProperties start(final String configPath, final String configName) {
        return run(configPath, configName, null);
    }

    /**
     * <p>
     * 开启监控
     * </p>
     *
     * @param monitoringProperties 监控属性
     * @return {@link MonitoringProperties}
     * @author 皮锋
     * @custom.date 2024/4/7 10:43
     */
    @SneakyThrows
    public static MonitoringProperties start(MonitoringProperties monitoringProperties) {
        run(null, null, monitoringProperties);
        return monitoringProperties;
    }

    /**
     * <p>
     * 运行监控
     * </p>
     *
     * @param configPath           配置文件路径
     * @param configName           配置文件名字
     * @param monitoringProperties 监控属性
     * @return {@link MonitoringProperties}
     * @throws NotFoundConfigFileException  找不到配置文件异常
     * @throws ErrorConfigParamException    错误的配置参数异常
     * @throws NotFoundConfigParamException 找不到配置参数异常
     * @author 皮锋
     * @custom.date 2020年3月5日 下午4:07:10
     */
    private static MonitoringProperties run(final String configPath, final String configName, MonitoringProperties monitoringProperties)
            throws NotFoundConfigFileException, ErrorConfigParamException, NotFoundConfigParamException {
        // 1.打印banner信息
        InitBanner.declare();
        // 2.加载配置信息
        if (monitoringProperties == null) {
            monitoringProperties = ConfigLoader.load(configPath, configName);
        } else {
            monitoringProperties = ConfigLoader.verify(monitoringProperties);
        }
        // 3.验证许可证信息
        String endpoint = monitoringProperties.getInstance().getEndpoint();
        String clientNameEn = EndpointTypeEnums.CLIENT.getNameEn();
        if (!StringUtils.equalsIgnoreCase(endpoint, clientNameEn)) {
            boolean isVerifyPassed = LicenseChecker.verify();
            if (!isVerifyPassed) {
                // 立即终止JVM，状态码为1
                Runtime.getRuntime().halt(1);
            }
        }
        // 4.开始定时发送心跳包
        HeartbeatTaskScheduler.run();
        // 5.开始定时发送服务器信息包
        ServerTaskScheduler.run();
        // 6.开始定时发送Java虚拟机信息包
        JvmTaskScheduler.run();
        // 最后：添加关闭钩子，在jvm退出前做一些操作
        ShutdownHook.addShutdownHook();
        // 返回监控属性
        return monitoringProperties;
    }

    /**
     * <p>
     * 发送告警
     * </p>
     *
     * @param alarm 告警信息
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020年3月6日 上午10:17:44
     */
    public static Result sendAlarm(Alarm alarm) {
        try {
            // 构造告警数据包
            AlarmPackage alarmPackage = CLIENT_PACKAGE_CONSTRUCTOR.structureAlarmPackage(alarm);
            String result = Sender.send(UrlConstants.ALARM_URL, alarmPackage.toJsonString());
            BaseResponsePackage baseResponsePackage = JSON.parseObject(result, BaseResponsePackage.class);
            return baseResponsePackage.getResult();
        } catch (IOException | NetException e) {
            log.error("监控程序发送告警信息异常！", e);
            return Result.builder().isSuccess(false).msg(e.getMessage()).build();
        }
    }

    /**
     * <p>
     * 业务埋点监控：定时监控业务运行情况
     * </p>
     *
     * @param command        要执行的任务
     * @param initialDelay   初次埋点监控延迟的时间
     * @param period         两次埋点监控任务之间的时间间隔
     * @param unit           时间单位
     * @param threadTypeEnum 线程类型：CPU密集型、IO密集型
     * @return {@link MonitoredScheduledThreadPoolExecutor}
     * @author 皮锋
     * @custom.date 2020/8/24 20:33
     */
    public static MonitoredScheduledThreadPoolExecutor buryingPoint(Runnable command, long initialDelay, long period, TimeUnit unit, ThreadTypeEnums threadTypeEnum) {
        return BusinessBuryingPointScheduler.run(command, initialDelay, period, unit, threadTypeEnum);
    }

}
