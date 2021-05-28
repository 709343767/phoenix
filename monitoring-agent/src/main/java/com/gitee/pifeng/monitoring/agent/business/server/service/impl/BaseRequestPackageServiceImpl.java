package com.gitee.pifeng.monitoring.agent.business.server.service.impl;

import com.gitee.pifeng.monitoring.agent.business.server.service.IBaseRequestPackageService;
import com.gitee.pifeng.monitoring.agent.business.server.service.IHttpService;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 跟服务端相关的基础请求包服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/4/5 15:01
 */
@Service
public class BaseRequestPackageServiceImpl implements IBaseRequestPackageService {

    /**
     * 跟服务端相关的HTTP服务接口
     */
    @Autowired
    private IHttpService httpService;

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
    @Override
    public BaseResponsePackage sendBaseRequestPackage(BaseRequestPackage baseRequestPackage, String url) throws Exception {
        return this.httpService.sendHttpPost(baseRequestPackage.toJsonString(), url);
    }

}
