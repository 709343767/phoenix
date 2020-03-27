package com.transfar.server.business.server.core;

import com.transfar.common.inf.ISuperBean;
import com.transfar.server.business.server.domain.Cpu;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 服务器CPU信息池，维护各个服务器的CPU使用情况，是否已经发送告警消息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/27 15:49
 */
@SuppressWarnings("serial")
@Component
public class CpuPool extends ConcurrentHashMap<String, Cpu> implements ISuperBean {

    /**
     * <p>
     * 更新服务器CPU信息池
     * </p>
     *
     * @param key 服务器CPU信息键
     * @param cpu 服务器CPU信息
     * @author 皮锋
     * @custom.date 2020/3/26 15:53
     */
    public void updateMemoryPool(String key, Cpu cpu) {
        Cpu poolCpu = this.get(key);
        if (poolCpu == null) {
            this.put(key, cpu);
        } else {
            this.replace(key, cpu);
        }
    }
}
