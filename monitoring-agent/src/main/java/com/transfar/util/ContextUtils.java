package com.transfar.util;

import com.transfar.business.core.ConfigLoader;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.Inet4Address;
import java.net.InetAddress;
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
     * SpringMvc下获取request
     * </p>
     *
     * @return HttpServletRequest
     * @author 皮锋
     * @custom.date 2020/3/12 20:31
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    /**
     * <p>
     * SpringMvc下获取session
     * </p>
     *
     * @return HttpSession
     * @author 皮锋
     * @custom.date 2020/3/12 20:31
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * <p>
     * 获取项目根路径
     * </p>
     *
     * @return 项目根URL
     * @author 皮锋
     * @custom.date 2020/3/12 21:46
     */
    @SneakyThrows
    public static String getRootUrl() {
        InetAddress address = Inet4Address.getLocalHost();
        String contextPath = StringUtils.isBlank(ConfigLoader.serverProperties.getServlet().getContextPath()) ? "" : ConfigLoader.serverProperties.getServlet().getContextPath();
        return "http://" + address.getHostAddress() + ":" + ConfigLoader.serverPort + contextPath;
    }

}
