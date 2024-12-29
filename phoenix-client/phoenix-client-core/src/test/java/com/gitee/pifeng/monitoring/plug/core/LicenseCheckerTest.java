package com.gitee.pifeng.monitoring.plug.core;

import cn.hutool.core.lang.Console;
import com.gitee.pifeng.monitoring.common.domain.License;
import org.junit.Test;

import java.time.LocalDateTime;

/**
 * <p>
 * 许可证验证器测试类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/5/6 10:51
 */
public class LicenseCheckerTest {

    /**
     * <p>
     * 测试生成许可证信息（未加密）
     * </p>
     *
     * @author 皮锋
     * @custom.date 2024/5/6 10:52
     */
    @Test
    public void createLicense() {
        // 多久后过期
        LocalDateTime deadline = LocalDateTime.now().plusDays(365);
        License license = License.builder().deadline(deadline).build();
        String licenseJson = LicenseChecker.createLicense(license);
        Console.log(licenseJson);
    }

}
