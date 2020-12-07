package com.gitee.pifeng.common.inf;

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
     * @param serializerFeature 序列化特征，具体参考类：{@link SerializerFeature}
     * @return Json字符串
     * @author 皮锋
     * @custom.date 2020年3月4日 上午10:16:43
     */
    default String toJsonString(SerializerFeature serializerFeature) {
        return JSON.toJSONString(this, serializerFeature);
    }

    /**
     * <p>
     * 对象转Json字符串
     * </p>
     *
     * @return Json字符串
     * @author 皮锋
     * @custom.date 2020/9/10 11:56
     */
    default String toJsonString() {
        return this.toJsonString(SerializerFeature.WriteMapNullValue);
    }

}
