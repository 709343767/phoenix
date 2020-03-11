package com.transfar.business.server.service;

import com.transfar.business.annotation.TargetInf;
import com.transfar.business.annotation.TargetMethod;
import com.transfar.dto.BaseResponsePackage;
import com.transfar.dto.ServerPackage;

/**
 * <p>
 * 跟服务端相关的服务器信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月7日 下午5:21:44
 */
@TargetInf(inf = IServerService.class)
public interface IServerService {

	/**
	 * <p>
	 * 给服务端发服务器信息包
	 * </p>
	 *
	 * @author 皮锋
	 * @param serverPackage 服务器信息包
	 * @custom.date 2020年3月7日 下午5:24:47
	 * @return BaseResponsePackage
	 * @throws Exception 所有异常
	 */
	// 加了注解的方法将会添加到命令执行器管理器，注册到bean容器
	@TargetMethod(method = "sendServerPackage")
	BaseResponsePackage sendServerPackage(ServerPackage serverPackage) throws Exception;
}
