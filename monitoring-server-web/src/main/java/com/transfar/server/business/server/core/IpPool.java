package com.transfar.server.business.server.core;

import com.transfar.common.inf.ISuper;
import com.transfar.server.business.server.domain.Ip;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * IP池，维护哪些IP可通，哪些IP不通
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/25 10:33
 */
@SuppressWarnings("serial")
@Component
public class IpPool extends ConcurrentHashMap<String, Ip> implements ISuper {
}
