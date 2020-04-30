package com.imby.common.web.util;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * <p>
 * 上下文环境工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/12 20:30
 */
public class ContextUtils {

	/**
	 * <p>
	 * SpringMvc下获取request
	 * </p>
	 *
	 * @return {@link HttpServletRequest}
	 * @author 皮锋
	 * @custom.date 2020/3/12 20:31
	 */
	public static HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
				.getRequest();
	}

	/**
	 * <p>
	 * SpringMvc下获取session
	 * </p>
	 *
	 * @return {@link HttpSession}
	 * @author 皮锋
	 * @custom.date 2020/3/12 20:31
	 */
	public static HttpSession getSession() {
		return getRequest().getSession();
	}

}
