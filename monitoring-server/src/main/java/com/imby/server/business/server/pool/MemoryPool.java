package com.imby.server.business.server.pool;

import com.imby.common.inf.ISuperBean;
import com.imby.server.business.server.domain.Memory;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 服务器内存信息池，维护各个服务器的内存使用情况，是否已经发送告警消息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/26 15:02
 */
@SuppressWarnings("serial")
@Component
public class MemoryPool extends ConcurrentHashMap<String, Memory> implements ISuperBean {

    /**
     * <p>
     * 更新服务器内存信息池
     * </p>
     *
     * @param key    服务器内存信息键
     * @param memory 服务器内存信息
     * @author 皮锋
     * @custom.date 2020/3/26 15:53
     */
    public void updateMemoryPool(String key, Memory memory) {
        Memory poolMemory = this.get(key);
        if (poolMemory == null) {
            this.put(key, memory);
        } else {
            this.replace(key, memory);
        }
    }
}
