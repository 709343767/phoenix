package com.transfar.constant;

import com.transfar.core.HostChoiceHandler;

/**
 * <p>
 * URL地址
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月6日 下午2:23:41
 */
public final class UrlConstants {

	/**
	 * 心跳地址
	 */
	public static final String HEARTBEAT_URL = HostChoiceHandler.choiceHost().getUrl()
			+ "/heartbeat/accept-heartbeat-package";

	/**
	 * 告警地址
	 */
	public static final String ALARM_URL = HostChoiceHandler.choiceHost().getUrl() + "/alarm/accept-alarm-package";

	/**
	 * 发送服务器信息地址
	 */
	public static final String SERVER_URL = HostChoiceHandler.choiceHost().getUrl() + "/server/accept-server-package";

}
