package com.imby.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * <p>
 * 应用服务器类型
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/5 21:16
 */
@Getter
@ToString
@AllArgsConstructor
public enum AppServerTypeEnums {

    /**
     * Tomcat
     */
    TOMCAT("Tomcat"),

    /**
     * Undertow
     */
    UNDERTOW("Undertow"),

    /**
     * Jetty
     */
    JETTY("Jetty"),

    /**
     * WebLogic
     */
    WEBLOGIC("WebLogic"),

    /**
     * 未知
     */
    UNKNOWN("未知");

    /**
     * 应用服务器类型名
     */
    private final String name;

}
