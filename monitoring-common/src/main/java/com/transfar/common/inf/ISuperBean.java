package com.transfar.common.inf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * <p>
 * 父接口，定义默认方法
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/25 10:49
 */
public interface ISuperBean {

    /**
     * <p>
     * 对象转Json字符串
     * </p>
     *
     * @return Json字符串
     * @author 皮锋
     * @custom.date 2020年3月4日 上午10:16:43
     */
    default String toJsonString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}
