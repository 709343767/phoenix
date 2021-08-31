package com.gitee.pifeng.monitoring.common.util;

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

}
