package com.gitee.pifeng.monitoring.common.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * <p>
 * LayUI工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/9/30 8:34
 */
public class LayUiUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2024/9/30 8:34
     */
    private LayUiUtils() {
    }

    /**
     * <p>
     * LayUiTable数据中，过滤出选中的数据
     * </p>
     *
     * @param layUiTableData LayUiTable数据
     * @return LayUiTable选中的数据
     * @author 皮锋
     * @custom.date 2024/09/30 20:26
     */
    public static String filterCheckedWithLayUiTable(String layUiTableData) {
        JSONArray jsonArray = new JSONArray();
        if (StringUtils.isNotBlank(layUiTableData)) {
            List<JSONObject> jsonObjects = JSONArray.parseArray(layUiTableData, JSONObject.class);
            if (CollectionUtils.isNotEmpty(jsonObjects)) {
                for (JSONObject param : jsonObjects) {
                    Boolean layChecked = param.getBoolean("LAY_CHECKED");
                    if (BooleanUtils.isTrue(layChecked)) {
                        jsonArray.add(param);
                    }
                }
            }
        }
        return jsonArray.toJSONString();
    }

    /**
     * <p>
     * LayUiTable数据中，JSONArray格式的字符串转换为数组格式的字符串，只取key和value
     * </p>
     *
     * @param jsonArrayStr JSONArray格式的字符串，如：[{"key":1},{"value":2}]
     * @param isLayChecked 是否只转换选中状态的数据
     * @return 数组格式的字符串，如：[key=1,value=2]
     * @author 皮锋
     * @custom.date 2024/09/28 09:51
     */
    public static String jsonArrayStr2ArrayStrWithLayUiTable(String jsonArrayStr, boolean isLayChecked) {
        StringBuilder result = new StringBuilder("[");
        if (StringUtils.isNotBlank(jsonArrayStr)) {
            List<JSONObject> jsonObjects = JSONArray.parseArray(jsonArrayStr, JSONObject.class);
            if (CollectionUtils.isNotEmpty(jsonObjects)) {
                for (JSONObject jsonObject : jsonObjects) {
                    // 只转换选中状态的数据
                    if (isLayChecked) {
                        Boolean layChecked = jsonObject.getBoolean("LAY_CHECKED");
                        if (BooleanUtils.isTrue(layChecked)) {
                            result.append(jsonObject.getString("key")).append("=").append(jsonObject.getString("value")).append(",");
                        }
                    }
                    // 转换所有数据
                    else {
                        result.append(jsonObject.getString("key")).append("=").append(jsonObject.getString("value")).append(",");
                    }
                }
                // 以逗号结尾，去掉末尾的逗号
                if (StringUtils.endsWith(result, ",")) {
                    result.deleteCharAt(result.length() - 1);
                }
            }
        }
        result.append("]");
        return result.toString();
    }

}
