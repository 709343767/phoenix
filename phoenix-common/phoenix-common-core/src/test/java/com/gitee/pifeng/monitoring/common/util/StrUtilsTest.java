package com.gitee.pifeng.monitoring.common.util;

import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 测试字符串工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/11/21 18:47
 */
@Slf4j
public class StrUtilsTest extends TestCase {

    /**
     * <p>
     * 测试 从字符串末尾字母起点位置点分割成数组
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/11/21 18:48
     */
    public void testSplitAllEndLetter() {
        String[] result = StrUtils.splitAllEndLetter("30kB");
        for (String s : result) {
            log.info(s);
        }
    }

}
