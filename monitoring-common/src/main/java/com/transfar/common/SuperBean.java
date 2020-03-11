package com.transfar.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * <p>
 * 父抽象bean，用于定义一些公共方法
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 上午10:16:00
 */
public abstract class SuperBean {

    /**
     * <p>
     * 对象转Json字符串
     * </p>
     *
     * @return Json
     * @author 皮锋
     * @custom.date 2020年3月4日 上午10:16:43
     */
    public String toJsonString() {
        return JSON.toJSONString(this, SerializerFeature.WriteMapNullValue);
    }

}
