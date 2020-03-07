package com.transfar.business.plug.service.impl;

import org.springframework.stereotype.Service;

import com.transfar.business.core.MethodExecuteHandler;
import com.transfar.business.plug.service.IServerService;
import com.transfar.dto.ServerPackage;

/**
 * <p>
 * 服务器信息服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:16:30
 */
@Service
public class ServerServiceImpl implements IServerService {

	/**
	 * <p>
	 * 处理服务器信息包
	 * </p>
	 *
	 * @author 皮锋
	 * @custom.date 2020年3月7日 下午5:14:29
	 * @param serverPackage 服务器信息包
	 * @return Boolean
	 */
	@Override
	public Boolean dealServerPackage(ServerPackage serverPackage) {
		// 把服务器信息包转发到服务端
		return MethodExecuteHandler.sendServerPackage2Server(serverPackage);
	}

}
