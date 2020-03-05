package com.transfar.core;

import java.io.IOException;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;

import com.transfar.constant.EndpointTypeConstant;
import com.transfar.dto.HeartbeatPackage;
import com.transfar.exception.NotFoundHostException;
import com.transfar.util.HttpUtils;
import com.transfar.util.InstanceIdUtils;

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

	/**
	 * 接收心跳的包映射地址
	 */
	private static final String MAPPING_URL = "/heartbeat/accept-heartbeat-package";

	@Override
	public void run() {
		try {
			// 构建心跳包
			HeartbeatPackage heartbeatPackage = new HeartbeatPackage();
			// heartbeatPackage.setId(StrUtils.getUUID());
			heartbeatPackage.setEndpoint(EndpointTypeConstant.CLIENT);
			heartbeatPackage.setInstanceId(InstanceIdUtils.getInstanceId());
			heartbeatPackage.setInstanceName(ConfigLoader.monitoringProperties.getOwnProperties().getInstanceName());
			heartbeatPackage.setDateTime(new Date());

			// 获取url
			String url = HostChoiceHandler.choiceHost().getUrl() + MAPPING_URL;
			// 发送请求
			HttpUtils httpClient = HttpUtils.getInstance();
			String result = httpClient.sendHttpPostByJSON(url, heartbeatPackage.toJsonString());
			System.out.println("心跳包响应消息：" + result);
		} catch (NotFoundHostException e) {
			log.error("找不到主机异常！", e);
		} catch (ClientProtocolException e) {
			log.error("客户端协议异常！", e);
		} catch (IOException e) {
			log.error("IO异常！", e);
		}
	}

}
