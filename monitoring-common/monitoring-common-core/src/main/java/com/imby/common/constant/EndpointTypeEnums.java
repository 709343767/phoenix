package com.imby.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * <p>
 * 端点类型（服务端、代理端、客户端）
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午10:30:27
 */
@Getter
@ToString
@AllArgsConstructor
public enum EndpointTypeEnums {

    /**
     * 服务端
     */
    SERVER("server", "服务端"),

    /**
     * 代理端
     */
    AGENT("agent", "代理端"),

    /**
     * 客户端
     */
    CLIENT("client", "客户端");

    /**
     * 英文名字
     */
    private final String nameEn;

    /**
     * 中文名字
     */
    private final String nameCn;

}
