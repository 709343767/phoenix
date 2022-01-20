package com.gitee.pifeng.monitoring.server.util.db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoIterable;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * mongo工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/19 14:53
 */
@Slf4j
public class MongoUtils {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/01/19 14:09
     */
    private MongoUtils() {
    }

    /**
     * <p>
     * 获取 MongoClient
     * </p>
     *
     * @param uri URI
     * @return {@link MongoClient}
     * @author 皮锋
     * @custom.date 2022/1/19 14:54
     */
    public static MongoClient getClient(String uri) {
        try {
            return new MongoClient(new MongoClientURI(uri));
        } catch (Exception e) {
            log.error("与mongo数据库建立连接异常！");
            return null;
        }
    }

    /**
     * <p>
     * Mongo数据库是否可连接
     * </p>
     *
     * @param mongoClient {@link MongoClient}
     * @return boolean 是 或者 否
     * @author 皮锋
     * @custom.date 2022/1/19 16:49
     */
    public static boolean isConnect(MongoClient mongoClient) {
        if (mongoClient == null) {
            return false;
        }
        try {
            MongoIterable<String> databaseNames = mongoClient.listDatabaseNames();
            MongoCursor<String> it = databaseNames.iterator();
            if (it.hasNext()) {
                it.next();
            }
            return true;
        } catch (Exception e) {
            log.error("检查连接异常！", e);
            return false;
        }
    }

    /**
     * <p>
     * 关闭 MongoClient
     * </p>
     *
     * @param mongoClient {@link MongoClient}
     * @author 皮锋
     * @custom.date 2022/1/19 16:17
     */
    public static void close(MongoClient mongoClient) {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }

}
