package com.gitee.pifeng.monitoring.common.util;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 测试MD5工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/8/10 10:25
 */
@Slf4j
public class Md5UtilsTest extends TestCase {

    /**
     * <p>
     * 测试获取32位md5校验码
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/8/10 10:27
     */
    public void testEncrypt32() {
        String encrypt32 = Md5Utils.encrypt32("http://localhost:12000/phoenix-agent");
        log.info(encrypt32);
        assertEquals(encrypt32, "c366475a6c96f33a7eea8bc3126d1f4c");
    }

    /**
     * <p>
     * 测试获取16位md5校验码
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/8/10 10:28
     */
    public void testEncrypt16() {
        String encrypt16 = Md5Utils.encrypt16("http://localhost:12000/phoenix-agent");
        log.info(encrypt16);
        assertEquals(encrypt16, "6c96f33a7eea8bc3");
    }

}