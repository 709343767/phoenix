package com.gitee.pifeng.monitoring.server.business.server.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * <p>
 * Mongo数据库信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/20 15:20
 */
public interface IDbInfo4MongoService {

    /**
     * <p>
     * 获取Mongo信息列表
     * </p>
     *
     * @param uri URI
     * @return Mongo信息列表
     * @author 皮锋
     * @custom.date 2022/1/21 12:53
     */
    List<JSONObject> getMongoInfoList(String uri);
}
