package com.gitee.pifeng.monitoring.server.util.db;

import com.mongodb.MongoClient;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * <p>
 * 测试mongo工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/20 11:01
 */
@Slf4j
public class MongoUtilsTest {

    /**
     * <p>
     * 测试获取mongo连接
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/1/19 15:31
     */
    @Test
    public void getClient() {
        MongoClient client = MongoUtils.getClient("mongodb://127.0.0.1:27017");
        assertNotNull(client);
        MongoUtils.close(client);
    }

    /**
     * <p>
     * 测试Mongo数据库是否可连接
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/1/19 16:54
     */
    @Test
    public void isConnect() {
        MongoClient client = MongoUtils.getClient("mongodb://127.0.0.1:27017");
        boolean connect = MongoUtils.isConnect(client);
        assertTrue(connect);
        MongoUtils.close(client);
    }
}