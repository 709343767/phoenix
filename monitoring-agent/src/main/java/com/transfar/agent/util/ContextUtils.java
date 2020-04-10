package com.transfar.agent.util;

import com.transfar.agent.business.core.ConfigLoader;
import com.transfar.common.util.NetUtils;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
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
     * SpringMvc下获取request
     * </p>
     *
     * @return {@link HttpServletRequest}
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
     * @return {@link HttpSession}
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
        String contextPath = StringUtils.isBlank(ConfigLoader.serverProperties.getServlet().getContextPath()) ? "" : ConfigLoader.serverProperties.getServlet().getContextPath();
        return "http://" + NetUtils.getLocalIp() + ":" + ConfigLoader.serverPort + contextPath;
    }

}
