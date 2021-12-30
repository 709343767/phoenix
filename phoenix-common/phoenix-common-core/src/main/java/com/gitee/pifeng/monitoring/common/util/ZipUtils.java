package com.gitee.pifeng.monitoring.common.util;

import cn.hutool.core.io.unit.DataSize;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * <p>
 * 压缩工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/12/20 12:49
 */
@Slf4j
public class ZipUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/12/20 12:50
     */
    private ZipUtils() {
    }

    /**
     * <p>
     * 字符串是否需要进行gzip压缩
     * </p>
     *
     * @param str 输入字符串
     * @return boolean 否需要进行gzip压缩
     * @author 皮锋
     * @custom.date 2021/12/20 21:02
     */
    public static boolean isNeedGzip(String str) {
        if (StringUtils.isBlank(str)) {
            return false;
        }
        // 10KB
        long minSize = DataSize.ofKilobytes(10).toBytes();
        if (str.getBytes().length <= minSize) {
            return false;
        }
        log.info("字符串超过10KB，启用gzip！");
        return true;
    }

}
