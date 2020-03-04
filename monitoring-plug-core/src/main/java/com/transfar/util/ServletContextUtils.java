package com.transfar.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <p>
 * Servlet上下文环境工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 下午1:34:58
 */
public final class ServletContextUtils {

	/**
	 * <p>
	 * SpringMvc下获取request
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月3日 下午1:34:33
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * <p>
	 * SpringMvc下获取session
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月3日 下午1:34:04
	 * @return HttpSession
	 */
	public static HttpSession getSession() {
		return getRequest().getSession();
	}

}
