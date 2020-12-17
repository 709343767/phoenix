package com.gitee.pifeng.server.business.server.pool;

import com.gitee.pifeng.common.inf.ISuperBean;
import com.gitee.pifeng.server.business.server.domain.Server;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 服务器信息池
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/17 20:01
 */
@SuppressWarnings("serial")
@Component
public class ServerPool extends ConcurrentHashMap<String, Server> implements ISuperBean {

    /**
     * <p>
     * 更新服务器信息池
     * </p>
     *
     * @param key    服务器信息键
     * @param server 服务器信息
     * @author 皮锋
     * @custom.date 2020/3/26 15:53
     */
    public void updateServerPool(String key, Server server) {
        Server poolServer = this.get(key);
        if (poolServer == null) {
            this.put(key, server);
        } else {
            this.replace(key, server);
        }
    }

}
