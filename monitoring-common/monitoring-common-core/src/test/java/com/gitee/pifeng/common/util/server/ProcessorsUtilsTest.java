package com.gitee.pifeng.common.util.server;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * <p>
 * 测试处理器工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/1/14 9:48
 */
@Slf4j
public class ProcessorsUtilsTest {

    /**
     * <p>
     * 测试系统可用的处理器核心数
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/8/25 9:16
     */
    @Test
    public void testGetAvailableProcessors() {
        int availableProcessors = ProcessorsUtils.getAvailableProcessors();
        log.info("系统可用的处理器核心数:" + availableProcessors);
        log.info("IO密集型线程池最佳线程数：" + (int) (availableProcessors / (1 - 0.8)));
        log.info("CPU密集型线程池最佳线程数：" + (int) (availableProcessors / (1 - 0.2)));
    }

}
