package com.transfar.plug.task;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.hyperic.sigar.SigarException;

import com.transfar.common.dto.ServerPackage;
import com.transfar.plug.constant.UrlConstants;
import com.transfar.plug.core.PackageConstructor;
import com.transfar.plug.core.Sender;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 发送服务器信息任务
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午4:48:48
 */
@Slf4j
public class ServerTask implements Runnable {

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
