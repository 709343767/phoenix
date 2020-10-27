package com.imby.common.constant;

/**
 * <p>
 * 端点类型（服务端、代理端、客户端）
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午10:30:27
 */
public final class EndpointTypeConstants {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/10/27 13:26
     */
    private EndpointTypeConstants() {
    }

    /**
     * 服务端
     */
    public static final String SERVER = "server";

    /**
     * 代理端
     */
    public static final String AGENT = "agent";

    /**
     * 客户端
     */
    public static final String CLIENT = "client";

}
