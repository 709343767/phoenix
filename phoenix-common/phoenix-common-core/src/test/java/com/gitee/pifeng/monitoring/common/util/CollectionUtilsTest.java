package com.gitee.pifeng.monitoring.common.util;

import com.google.common.collect.Lists;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * <p>
 * 测试集合工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023/6/2 12:19
 */
@Slf4j
public class CollectionUtilsTest extends TestCase {

    /**
     * <p>
     * 测试 根据指定大小拆分List
     * </p>
     *
     * @author 皮锋
     * @custom.date 2023/6/2 12:19
     */
    public void testSplit() {
        List<String> list = Lists.newArrayList();
        int size = 1099;
        for (int i = 1; i <= size; i++) {
            list.add("hello-" + i);
        }
        // 大集合里面包含多个小集合
        List<List<String>> temps = CollectionUtils.split(list, 100);
        int j = 0;
        // 对大集合里面的每一个小集合进行操作
        for (List<String> obj : temps) {
            log.info(String.format("row:%s -> size:%s,data:%s", ++j, obj.size(), obj));
        }
    }

    /**
     * <p>
     * 测试 拆分成指定个数的List
     * </p>
     *
     * @author 皮锋
     * @custom.date 2024/11/13 16:06
     */
    public void testPartitionList() {
        List<String> list = Lists.newArrayList();
        int size = 1099;
        for (int i = 1; i <= size; i++) {
            list.add("hello-" + i);
        }
        // 大集合里面包含多个小集合
        List<List<String>> temps = CollectionUtils.partitionList(list, 100);
        int j = 0;
        // 对大集合里面的每一个小集合进行操作
        for (List<String> obj : temps) {
            log.info(String.format("row:%s -> size:%s,data:%s", ++j, obj.size(), obj));
        }
    }

}
