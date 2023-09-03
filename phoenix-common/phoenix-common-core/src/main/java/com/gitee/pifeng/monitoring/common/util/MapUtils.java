package com.gitee.pifeng.monitoring.common.util;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * <p>
 * Map工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/7/17 14:56
 */
public class MapUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/7/17 14:57
     */
    private MapUtils() {
    }

    /**
     * <p>
     * 转换请求参数
     * </p>
     *
     * @param paramMap 请求参数
     * @return {@link Map}
     * @author 皮锋
     * @custom.date 2021/6/10 13:45
     */
    public static Map<String, Object> convertParamMap(Map<String, String[]> paramMap) {
        Map<String, Object> rtnMap = Maps.newHashMap();
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

    /**
     * <p>
     * Map转json字符串
     * </p>
     *
     * @param map Map对象
     * @param <K> Map入参键类型
     * @param <V> Map入参值类型
     * @return json字符串
     * @author 皮锋
     * @custom.date 2022/7/4 15:45
     */
    public static <K, V> String map2JsonString(Map<K, V> map) {
        if (org.apache.commons.collections.MapUtils.isEmpty(map)) {
            return null;
        }
        return JSONObject.toJSONString(map);
    }

}
