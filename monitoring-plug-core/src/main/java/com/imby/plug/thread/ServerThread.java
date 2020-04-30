package com.imby.plug.thread;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.hyperic.sigar.SigarException;

import com.imby.common.dto.ServerPackage;
import com.imby.plug.constant.UrlConstants;
import com.imby.plug.core.PackageConstructor;
import com.imby.plug.core.Sender;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 发送服务器信息线程
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午4:48:48
 */
@Slf4j
public class ServerThread implements Runnable {

    /**
     * <p>
     * 发送服务器信息包
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/4/9 17:30
     */
    @Override
    public void run() {
        try {
            // 构建服务器数据包
            ServerPackage serverPackage = new PackageConstructor().structureServerPackage();
            // 发送请求
            String result = Sender.send(UrlConstants.SERVER_URL, serverPackage.toJsonString());
            log.debug("服务器包响应消息：{}", result);
        } catch (ClientProtocolException e) {
            log.error("客户端协议异常！", e);
        } catch (IOException e) {
            log.error("IO异常！", e);
        } catch (SigarException e) {
            log.error("Sigar异常！", e);
        }
    }

}
