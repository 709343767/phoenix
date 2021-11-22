package com.gitee.pifeng.monitoring.common.util.secure;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 测试初始化加解密工具
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/8/14 14:45
 */
@Slf4j
public class SecureUtilsTest extends TestCase {

    /**
     * <p>
     * 测试秘钥生成方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/8/14 14:45
     */
    public void testGenerateKey() {
        log.info(SecureUtils.SECRET_TYPE);
        String key = SecureUtils.generateKey(StringUtils.upperCase(SecureUtils.SECRET_TYPE), 64);
        log.info(key);
    }

}