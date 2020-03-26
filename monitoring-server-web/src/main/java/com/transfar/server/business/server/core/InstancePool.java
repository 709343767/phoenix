package com.transfar.server.business.server.core;

import com.transfar.common.inf.ISuper;
import com.transfar.server.business.server.domain.Instance;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 应用实例池，维护哪些应用在线，哪些应用离线
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月9日 上午10:53:52
 */
@SuppressWarnings("serial")
@Component
public class InstancePool extends ConcurrentHashMap<String, Instance> implements ISuper {

    /**
     * <p>
     * 更新应用实例池
     * </p>
     *
     * @param key      应用实例键
     * @param instance 应用实例
     * @author 皮锋
     * @custom.date 2020/3/26 15:30
     */
    public void updateInstancePool(String key, Instance instance) {
        Instance poolInstance = this.get(key);
        if (poolInstance == null) {
            // 加入到应用实例池
            this.put(key, instance);
        } else {
            this.replace(key, instance);
        }
    }
}
