package com.gitee.pifeng.monitoring.agent.business.client.service.impl;

import com.gitee.pifeng.monitoring.agent.core.MethodExecuteHandler;
import com.gitee.pifeng.monitoring.agent.business.client.service.IJvmService;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import org.springframework.stereotype.Service;

/**
 * <p>
 * Java虚拟机信息服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/15 22:25
 */
@Service
public class JvmServiceImpl implements IJvmService {

    /**
     * <p>
     * 处理Java虚拟机信息包
     * </p>
     *
     * @param jvmPackage Java虚拟机信息包
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月7日 下午5:14:29
     */
    @Override
    public BaseResponsePackage dealJvmPackage(JvmPackage jvmPackage) {
        // 把Java虚拟机信息包转发到服务端
        return MethodExecuteHandler.sendJvmPackage2Server(jvmPackage);
    }
}
