package com.transfar.business.plug.service.impl;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.transfar.business.core.ConfigLoader;
import com.transfar.business.core.Invoker;
import com.transfar.business.core.InvokerHolder;
import com.transfar.business.plug.service.IHeartbeatService;
import com.transfar.dto.HeartbeatDto;

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
	 * @param heartbeatDto 心跳对象
	 * @return HeartbeatDto
	 * @author 皮锋
	 * @custom.date 2020年3月4日 下午1:47:28
	 */
	@Override
	public HeartbeatDto dealHeartbeatPackage(HeartbeatDto heartbeatDto) {
		// 通过命令执行器管理器，获取指定的命令执行器
		Invoker invoker = InvokerHolder.getInvoker(com.transfar.business.server.service.IHeartbeatService.class,
				"sendHeartbeatPackage");
		// 执行命令，返回执行结果
		HeartbeatDto result;
		try {
			assert invoker != null;
			Object object = invoker.invoke(heartbeatDto);
			result = (HeartbeatDto) object;
		} catch (Exception e) {
			result = new HeartbeatDto().setResult(false).setInstanceName(ConfigLoader.getInstanceName())
					.setDateTime(new Date());
		}
		return result;
	}

}
