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
}
