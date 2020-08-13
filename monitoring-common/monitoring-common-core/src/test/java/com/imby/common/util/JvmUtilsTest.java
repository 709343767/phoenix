package com.imby.common.util;

import com.imby.common.domain.server.JvmDomain;
import org.junit.Test;

/**
 * <p>
 * 测试JVM工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/13 22:22
 */
public class JvmUtilsTest {

    /**
     * <p>
     * 测试获取JVM运行时信息
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/13 22:24
     */
    @Test
    public void testGetRuntimeDomainInfo() {
        JvmDomain.RuntimeDomain runtimeDomain = JvmUtils.getRuntimeDomainInfo();
        System.out.println(runtimeDomain.toJsonString());
    }
}
