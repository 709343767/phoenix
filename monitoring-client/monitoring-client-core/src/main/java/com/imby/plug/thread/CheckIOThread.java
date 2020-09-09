package com.imby.plug.thread;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import com.alibaba.fastjson.JSON;
import com.imby.common.domain.Result;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.common.dto.HeartbeatPackage;
import com.imby.common.exception.NetException;
import com.imby.plug.constant.UrlConstants;
import com.imby.plug.core.PackageConstructor;
import com.imby.plug.core.Sender;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;

import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * <p>
 * 检测HTTP连接IO情况的线程
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/16 11:56
 */
@Slf4j
@Deprecated
public class CheckIOThread implements Callable<Result> {

    /**
     * <p>
     * 发送心跳包
     * </p>
     *
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020/8/16 17:48
     */
    @Override
    public Result call() {
        try {
            // 构建心跳数据包
            HeartbeatPackage heartbeatPackage = new PackageConstructor().structureHeartbeatPackage();
            // 计时器
            TimeInterval timer = DateUtil.timer();
            // 发送请求
            String result = Sender.send(UrlConstants.HEARTBEAT_URL, heartbeatPackage.toJsonString());
            // 时间差（毫秒）
            String betweenDay = timer.intervalPretty();
            log.debug("发送心跳包耗时：{}", betweenDay);
            log.debug("心跳包响应消息：{}", result);
            BaseResponsePackage baseResponsePackage = JSON.parseObject(result, BaseResponsePackage.class);
            return baseResponsePackage.getResult();
        } catch (IOException e) {
            log.error("IO异常！", e);
        } catch (NetException e) {
            log.error("获取网络信息异常！", e);
        } catch (SigarException e) {
            log.error("Sigar异常！", e);
        }
        return Result.builder().isSuccess(false).build();
    }

}
