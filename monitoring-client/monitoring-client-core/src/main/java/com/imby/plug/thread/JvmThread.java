package com.imby.plug.thread;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import com.imby.common.dto.JvmPackage;
import com.imby.plug.constant.UrlConstants;
import com.imby.plug.core.PackageConstructor;
import com.imby.plug.core.Sender;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;

import java.io.IOException;
import java.util.Date;

/**
 * <p>
 * 发送Java虚拟机信息线程
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/14 21:24
 */
@Slf4j
public class JvmThread implements Runnable {

    /**
     * <p>
     * 发送Java虚拟机信息包
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/14 21:25
     */
    @Override
    public void run() {
        try {
            JvmPackage jvmPackage = new PackageConstructor().structureJvmPackage();
            // 开始时间
            Date beginDate = new Date();
            // 发送请求
            String result = Sender.send(UrlConstants.JVM_URL, jvmPackage.toJsonString());
            // 结束时间
            Date endDate = new Date();
            // 时间差（毫秒）
            String betweenDay = DateUtil.formatBetween(beginDate, endDate, BetweenFormater.Level.MILLISECOND);
            log.debug("发送Java虚拟机信息包耗时：{}", betweenDay);
            log.debug("Java虚拟机信息包响应消息：{}", result);
        } catch (ClientProtocolException e) {
            log.error("客户端协议异常！", e);
        } catch (IOException e) {
            log.error("IO异常！", e);
        }
    }

}
