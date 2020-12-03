package com.imby.common.constant;

/**
 * <p>
 * 端点类型（服务端、代理端、客户端）
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午10:30:27
 */
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

    private final String code;
    private final String name;

    EndpointTypeEnums(String code, String name) {
        this.code = code;
        this.name = name();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

}
