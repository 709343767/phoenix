package com.gitee.pifeng.monitoring.common.util;

import java.util.HashMap;
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
    public static Map<String, String> convertParamMap(Map<String, String[]> paramMap) {
        Map<String, String> rtnMap = new HashMap<>(16);
        for (String key : paramMap.keySet()) {
            rtnMap.put(key, paramMap.get(key)[0]);
        }
        return rtnMap;
    }

}
