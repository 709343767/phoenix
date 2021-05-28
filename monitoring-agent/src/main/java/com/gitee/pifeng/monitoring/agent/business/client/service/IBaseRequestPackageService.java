package com.gitee.pifeng.monitoring.agent.business.client.service;

import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;

/**
 * <p>
 * 基础请求包服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/4/5 14:23
 */
public interface IBaseRequestPackageService {

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
    BaseResponsePackage dealBaseRequestPackage(BaseRequestPackage baseRequestPackage, String url);

}
