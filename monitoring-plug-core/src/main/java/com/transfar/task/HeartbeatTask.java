package com.transfar.task;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.transfar.constant.UrlConstants;
import com.transfar.core.PackageConstructor;
import com.transfar.core.Sender;
import com.transfar.dto.HeartbeatPackage;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 心跳任务
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月5日 下午2:50:46
 */
@Slf4j
public class HeartbeatTask implements Runnable {

	@Override
	public void run() {
		try {
			// 构建心跳数据包
			HeartbeatPackage heartbeatPackage = PackageConstructor.structureHeartbeatPackage();
			// 发送请求
			String result = Sender.send(UrlConstants.HEARTBEAT_URL, heartbeatPackage.toJsonString());
			log.info("心跳包响应消息：{}", result);
		} catch (ClientProtocolException e) {
			log.error("客户端协议异常！", e);
		} catch (IOException e) {
			log.error("IO异常！", e);
		}
	}

}
