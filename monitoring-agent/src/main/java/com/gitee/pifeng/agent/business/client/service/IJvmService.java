package com.gitee.pifeng.agent.business.client.service;

import com.gitee.pifeng.common.dto.BaseResponsePackage;
import com.gitee.pifeng.common.dto.JvmPackage;

/**
 * <p>
 * Java虚拟机信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/15 22:23
 */
public interface IJvmService {

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
    BaseResponsePackage dealJvmPackage(JvmPackage jvmPackage);

}
