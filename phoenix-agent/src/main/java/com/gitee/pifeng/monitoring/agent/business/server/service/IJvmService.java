package com.gitee.pifeng.monitoring.agent.business.server.service;

import com.gitee.pifeng.monitoring.agent.annotation.TargetInf;
import com.gitee.pifeng.monitoring.agent.annotation.TargetMethod;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;

/**
 * <p>
 * 跟服务端相关的Java虚拟机信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/15 22:11
 */
@TargetInf(inf = IJvmService.class)
public interface IJvmService {

    /**
     * <p>
     * 给服务端发Java虚拟机信息包
     * </p>
     *
     * @param jvmPackage Java虚拟机信息包
     * @return {@link BaseResponsePackage}
     * @throws Exception 所有异常
     * @author 皮锋
     * @custom.date 2020年3月7日 下午5:24:47
     */
    @TargetMethod(method = "sendJvmPackage")
    BaseResponsePackage sendJvmPackage(JvmPackage jvmPackage) throws Exception;

}
