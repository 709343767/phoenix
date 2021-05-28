package com.gitee.pifeng.monitoring.agent.business.client.service.impl;

import com.gitee.pifeng.monitoring.agent.business.client.service.IBaseRequestPackageService;
import com.gitee.pifeng.monitoring.agent.core.MethodExecuteHandler;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 基础请求包服务实现
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/4/5 14:34
 */
@Service
public class BaseRequestPackageServiceImpl implements IBaseRequestPackageService {

    /**
     * <p>
     * 处理基础请求包
     * </p>
     *
     * @param baseRequestPackage 基础请求包
     * @param url                URL路径
     * @return {@link BaseResponsePackage}
     * @author 皮锋
     * @custom.date 2020年3月6日 下午3:09:12
     */
    @Override
    public BaseResponsePackage dealBaseRequestPackage(BaseRequestPackage baseRequestPackage, String url) {
        // 把基础请求包转发到服务端
        return MethodExecuteHandler.sendBaseRequestPackage2Server(baseRequestPackage, url);
    }

}
