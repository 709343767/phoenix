package com.gitee.pifeng.plug;

import com.alibaba.fastjson.JSON;
import com.gitee.pifeng.common.constant.ThreadTypeEnums;
import com.gitee.pifeng.common.domain.Alarm;
import com.gitee.pifeng.common.domain.Result;
import com.gitee.pifeng.common.dto.AlarmPackage;
import com.gitee.pifeng.common.dto.BaseResponsePackage;
import com.gitee.pifeng.common.exception.NetException;
import com.gitee.pifeng.common.init.InitBanner;
import com.gitee.pifeng.plug.constant.UrlConstants;
import com.gitee.pifeng.plug.core.ConfigLoader;
import com.gitee.pifeng.plug.core.PackageConstructor;
import com.gitee.pifeng.plug.core.Sender;
import com.gitee.pifeng.plug.scheduler.BusinessBuryingPointScheduler;
import com.gitee.pifeng.plug.scheduler.HeartbeatTaskScheduler;
import com.gitee.pifeng.plug.scheduler.JvmTaskScheduler;
import com.gitee.pifeng.plug.scheduler.ServerTaskScheduler;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
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
     * @author 皮锋
     * @custom.date 2020年3月5日 下午3:01:38
     */
    public static void start() {
        run(null, null);
    }

    /**
     * <p>
     * 开启监控，自定义配置文件路径和名字
     * </p>
     *
     * @param configPath 配置文件路径
     * @param configName 配置文件名字
     * @author 皮锋
     * @custom.date 2020年3月5日 下午4:06:31
     */
    public static void start(final String configPath, final String configName) {
        run(configPath, configName);
    }

    /**
     * <p>
     * 运行监控
     * </p>
     *
     * @param configPath 配置文件路径
     * @param configName 配置文件名字
     * @author 皮锋
     * @custom.date 2020年3月5日 下午4:07:10
     */
    private static void run(final String configPath, final String configName) {
        // 1.打印banner信息
        InitBanner.declare();
        // 2.加载配置信息
        try {
            ConfigLoader.load(configPath, configName);
            log.info("从监控配置文件中加载监控配置成功！");
        } catch (Exception e) {
            log.error("从监控配置文件中加载监控配置失败！", e);
            // 直接退出程序
            System.exit(0);
            return;
        }
        // 3.开始定时发送心跳包
        HeartbeatTaskScheduler.run();
        // 4.开始定时发送服务器信息包
        ServerTaskScheduler.run();
        // 5.开始定时发送Java虚拟机信息包
        JvmTaskScheduler.run();
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
            AlarmPackage alarmPackage = new PackageConstructor().structureAlarmPackage(alarm);
            String result = Sender.send(UrlConstants.ALARM_URL, alarmPackage.toJsonString());
            BaseResponsePackage baseResponsePackage = JSON.parseObject(result, BaseResponsePackage.class);
            return baseResponsePackage.getResult();
        } catch (IOException | NetException | SigarException e) {
            log.error("监控程序发送告警信息异常！", e);
            return Result.builder().isSuccess(false).msg(e.getMessage()).build();
        }
    }

    /**
     * <p>
     * 业务埋点监测：定时监测业务运行情况
     * </p>
     *
     * @param command        要执行的任务
     * @param initialDelay   初次埋点监测延迟的时间
     * @param period         两次埋点监测任务之间的时间间隔
     * @param unit           时间单位
     * @param threadTypeEnum 线程类型：CPU密集型、IO密集型
     * @return {@link ScheduledExecutorService}
     * @author 皮锋
     * @custom.date 2020/8/24 20:33
     */
    public static ScheduledExecutorService buryingPoint(Runnable command, long initialDelay, long period, TimeUnit unit, ThreadTypeEnums threadTypeEnum) {
        return BusinessBuryingPointScheduler.run(command, initialDelay, period, unit, threadTypeEnum);
    }

}
