package com.transfar.core;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.transfar.util.HttpUtils;

/**
 * <p>
 * 数据发送者
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 上午10:20:09
 */
public class Sender {

	/**
	 * <p>
	 * 发送数据
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月6日 上午10:21:25
	 * @param url  URL地址
	 * @param json JSON字符串格式的数据
	 * @return 服务端返回值
	 * @throws IOException             IO异常
	 * @throws ClientProtocolException 客户端协议异常
	 */
	public static String send(final String url, final String json) throws ClientProtocolException, IOException {
		// 发送请求
		HttpUtils httpClient = HttpUtils.getInstance();
		return httpClient.sendHttpPostByJSON(url, json);
	}

}
