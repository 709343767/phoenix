package com.imby.common.web.toolkit;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.imby.common.dto.CiphertextPackage;
import com.imby.common.util.DesEncryptUtils;

/**
 * <p>
 * 处理HTTP输入消息：获取密文数据包，并且解密。
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年5月6日 下午12:37:38
 */
public class MyHttpInputMessage implements HttpInputMessage {

	private final HttpHeaders headers;

	private final InputStream body;

	public MyHttpInputMessage(HttpInputMessage inputMessage) throws Exception {

		this.headers = inputMessage.getHeaders();

		String bodyStr = IOUtils.toString(inputMessage.getBody(), Charsets.UTF_8);
		// 密文数据包
		CiphertextPackage ciphertextPackage = JSONObject.parseObject(bodyStr, CiphertextPackage.class);
		// 解密
		String decryptStr = DesEncryptUtils.decrypt(ciphertextPackage.getCiphertext());

		this.body = IOUtils.toInputStream(decryptStr, Charsets.UTF_8);
	}

	@Override
	public InputStream getBody() {
		return this.body;
	}

	@Override
	public HttpHeaders getHeaders() {
		return this.headers;
	}

}
