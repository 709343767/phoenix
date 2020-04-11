package com.transfar.plug;

import com.alibaba.fastjson.JSON;
import com.transfar.common.domain.Alarm;
import com.transfar.common.domain.Result;
import com.transfar.common.dto.AlarmPackage;
import com.transfar.common.dto.BaseResponsePackage;
import com.transfar.plug.constant.UrlConstants;
import com.transfar.plug.core.ConfigLoader;
import com.transfar.plug.core.PackageConstructor;
import com.transfar.plug.core.Sender;
import com.transfar.plug.scheduler.HeartbeatTaskScheduler;
import com.transfar.plug.scheduler.ServerTaskScheduler;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

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
        // 1.加载配置信息
        try {
            ConfigLoader.load(configPath, configName);
            log.info("监控程序加载配置信息成功！");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return;
        }
        // 2.开始定时发送心跳包
        HeartbeatTaskScheduler.run();
        // 3.开始定时发送服务器信息包
        ServerTaskScheduler.run();
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
        } catch (IOException e) {
            log.error("监控程序发送告警信息异常！", e);
            return Result.builder().isSuccess(false).msg(e.getMessage()).build();
        }
    }

}
