package com.transfar.server.business.server.core;

import com.transfar.common.inf.ISuper;
import com.transfar.server.business.server.domain.Net;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 网络信息池，维护哪些网络可通，哪些网络不通
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/25 10:33
 */
@SuppressWarnings("serial")
@Component
public class NetPool extends ConcurrentHashMap<String, Net> implements ISuper {

    /**
     * <p>
     * 更新网络信息池
     * </p>
     *
     * @param key 网络信息键
     * @param net 网络信息
     * @author 皮锋
     * @custom.date 2020/3/25 12:28
     */
    public void updateNetPool(String key, Net net) {
        Net poolNet = this.get(key);
        // 网络信息池中没有当前IP，添加
        if (poolNet == null) {
            this.put(key, net);
        }
        // 网络信息池中有当前IP，替换
        else {
            this.replace(key, net);
        }
    }
}
