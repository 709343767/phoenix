package com.imby.plug.thread;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.imby.common.domain.Result;
import com.imby.common.dto.BaseResponsePackage;
import com.imby.common.dto.HeartbeatPackage;
import com.imby.plug.constant.UrlConstants;
import com.imby.plug.core.PackageConstructor;
import com.imby.plug.core.Sender;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.Date;
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
            // 开始时间
            Date beginDate = new Date();
            // 发送请求
            String result = Sender.send(UrlConstants.HEARTBEAT_URL, heartbeatPackage.toJsonString());
            // 结束时间
            Date endDate = new Date();
            // 时间差（毫秒）
            String betweenDay = DateUtil.formatBetween(beginDate, endDate, BetweenFormater.Level.MILLISECOND);
            log.debug("发送心跳包耗时：{}", betweenDay);
            log.debug("心跳包响应消息：{}", result);
            BaseResponsePackage baseResponsePackage = JSON.parseObject(result, BaseResponsePackage.class);
            return baseResponsePackage.getResult();
        } catch (ClientProtocolException e) {
            log.error("客户端协议异常！", e);
        } catch (IOException e) {
            log.error("IO异常！", e);
        }
        return Result.builder().isSuccess(false).build();
    }

}
