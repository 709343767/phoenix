package com.gitee.pifeng.common.util;

import com.gitee.pifeng.common.constant.AppServerTypeEnums;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * <p>
 * 测试应用服务器工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/14 13:07
 */
@Slf4j
public class AppServerDetectorUtilsTest {

    /**
     * <p>
     * 测试获取应用服务器枚举类型
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/14 13:09
     */
    @Test
    public void test() {
        AppServerTypeEnums appServerTypeEnums = AppServerDetectorUtils.getAppServerTypeEnum();
        assertNotNull(appServerTypeEnums);
        log.info(appServerTypeEnums.toString());
    }

}
