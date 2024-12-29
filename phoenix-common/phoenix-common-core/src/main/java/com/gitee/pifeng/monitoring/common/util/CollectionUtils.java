package com.gitee.pifeng.monitoring.common.util;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * 集合工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/8/31 14:57
 */
public class CollectionUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/8/31 14:57
     */
    private CollectionUtils() {
    }

    /**
     * <p>
     * 根据条件进行去重
     * </p>
     *
     * @param <T>          泛型
     * @param keyExtractor 去重条件
     * @return {@link Predicate}
     * @author 皮锋
     * @custom.date 2021/8/31 15:01
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>(16);
        return object -> seen.putIfAbsent(keyExtractor.apply(object), Boolean.TRUE) == null;
    }

    /**
     * <p>
     * 根据指定大小拆分List
     * </p>
     *
     * @param <T>          泛型对象
     * @param resourceList 需要拆分的List
     * @param subListSize  每个子List的大小
     * @return 返回拆分后的各个List组成的List
     **/
    public static <T> List<List<T>> split(List<T> resourceList, int subListSize) {
        if (CollectionUtil.isEmpty(resourceList) || subListSize <= 0) {
            return Lists.newArrayList();
        }
        List<List<T>> result = Lists.newArrayList();
        int size = resourceList.size();
        if (size <= subListSize) {
            // 数据量不足 subListSize 指定的大小
            result.add(resourceList);
        } else {
            int pre = size / subListSize;
            int last = size % subListSize;
            // 前面pre个集合，每个大小都是 subListSize 个元素
            for (int i = 0; i < pre; i++) {
                List<T> itemList = Lists.newArrayList();
                for (int j = 0; j < subListSize; j++) {
                    itemList.add(resourceList.get(i * subListSize + j));
                }
                result.add(itemList);
            }
            // last的进行处理
            if (last > 0) {
                List<T> itemList = Lists.newArrayList();
                for (int i = 0; i < last; i++) {
                    itemList.add(resourceList.get(pre * subListSize + i));
                }
                result.add(itemList);
            }
        }
        return result;
    }

    /**
     * <p>
     * 拆分成指定个数的List
     * </p>
     *
     * @param <T>           泛型对象
     * @param resourceList  需要拆分的List
     * @param maxPartitions 拆分成多少个子List
     * @return 返回拆分后的各个List组成的List
     * @author 皮锋
     * @custom.date 2021/8/31 14:57
     **/
    public static <T> List<List<T>> partitionList(List<T> resourceList, int maxPartitions) {
        if (CollectionUtil.isEmpty(resourceList) || maxPartitions <= 0) {
            return Lists.newArrayList();
        }
        int size = resourceList.size();
        int partitionCount = Math.min(maxPartitions, size);
        // 计算每个分区的大小
        int partitionSize = (int) Math.ceil((double) size / partitionCount);
        List<List<T>> result = Lists.newArrayList();
        for (int i = 0; i < maxPartitions; i++) {
            int fromIndex = i * partitionSize;
            // 确保 toIndex 不会超过列表的大小
            int toIndex = Math.min(fromIndex + partitionSize, size);
            // 防止越界
            if (fromIndex < size) {
                result.add(new ArrayList<>(resourceList.subList(fromIndex, toIndex)));
            }
        }
        return result;
    }

}
