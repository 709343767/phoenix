package com.gitee.pifeng.monitoring.agent.business.server.service;

import com.gitee.pifeng.monitoring.agent.annotation.TargetInf;
import com.gitee.pifeng.monitoring.agent.annotation.TargetMethod;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;

/**
 * <p>
 * 跟服务端相关的基础请求包服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/4/5 14:47
 */
@TargetInf(inf = IBaseRequestPackageService.class)
public interface IBaseRequestPackageService {

    /**
     * <p>
     * 给服务端发基础请求包
     * </p>
     *
     * @param baseRequestPackage 基础请求包
     * @param url                URL路径
     * @return {@link BaseResponsePackage}
     * @throws Exception 所有异常
     * @author 皮锋
     * @custom.date 2021/4/5 14:49
     */
    @TargetMethod(method = "sendBaseRequestPackage")
    BaseResponsePackage sendBaseRequestPackage(BaseRequestPackage baseRequestPackage, String url) throws Exception;

}
