package com.transfar.server.business.server.core;

import com.transfar.common.inf.ISuper;
import com.transfar.server.business.server.domain.Memory;
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
public class MemoryPool extends ConcurrentHashMap<String, Memory> implements ISuper {
}
