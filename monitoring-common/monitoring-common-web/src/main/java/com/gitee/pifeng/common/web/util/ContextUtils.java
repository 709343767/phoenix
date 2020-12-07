package com.gitee.pifeng.common.web.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

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
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/9/9 8:43
     */
    private ContextUtils() {
    }

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
