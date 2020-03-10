package com.transfar.business.server.service;

import com.transfar.business.server.domain.TransfarSms;

/**
 * <p>
 * 短信服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月9日 下午7:58:07
 */
public interface ISmsService {

	/**
	 * <p>
	 * 调用创发的短信接口发送短信
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月10日 上午11:01:47
	 * @param sms 创发短信实体对象
	 * @return 短信发送结果
	 */
	String sendSmsByTransfarApi(TransfarSms sms);

}
