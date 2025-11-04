package com.gitee.pifeng.monitoring.common.web.util;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * <p>
 * 访问对象工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/6/10 14:04
 */
@Slf4j
public class AccessObjectUtils {

    /**
     * <p>
     * 获取客户端真实IP地址
     * </p>
     * 获取客户端真实IP地址，不使用request.getRemoteAddr();的原因是有可能客户端使用了代理软件方式避免真实IP地址，
     * <a href="http://developer.51cto.com/art/201111/305181.htm">参考文章</a>，
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是客户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * 如：X-Forwarded-For：192.168.1.110,192.168.1.120,192.168.1.130,192.168.1.100
     * 客户端真实IP为： 192.168.1.110
     *
     * @param request 请求对象
     * @return IP地址
     * @author 皮锋
     * @custom.date 2021/6/10 14:07
     */
    public static String getClientAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if ("127.0.0.1".equals(ipAddress) || "0:0:0:0:0:0:0:1".equals(ipAddress)) {
                //根据网卡取本机配置的IP
                InetAddress inet;
                try {
                    inet = InetAddress.getLocalHost();
                    ipAddress = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    log.error(e.getMessage());
                }
            }
        }
        //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

}
