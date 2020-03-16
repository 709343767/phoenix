package com.transfar.agent.business.plug.service.impl;

import org.springframework.stereotype.Service;

import com.transfar.agent.business.core.MethodExecuteHandler;
import com.transfar.agent.business.plug.service.IHeartbeatService;
import com.transfar.common.dto.BaseResponsePackage;
import com.transfar.common.dto.HeartbeatPackage;

/**
 * <p>
 * 心跳服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午1:44:54
 */
@Service
public class HeartbeatServiceImpl implements IHeartbeatService {

	/**
	 * <p>
	 * 处理心跳包
	 * </p>
	 *
	 * @param heartbeatPackage 心跳包对象
	 * @return BaseResponsePackage
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午1:47:28
	 */
	@Override
	public BaseResponsePackage dealHeartbeatPackage(HeartbeatPackage heartbeatPackage) {
		// 把心跳包转发到服务端
		return MethodExecuteHandler.sendHeartbeatPackage2Server(heartbeatPackage);
	}

}
