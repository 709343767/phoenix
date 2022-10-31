package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorHttp;

/**
 * <p>
 * HTTP信息服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/13 9:04
 */
public interface IHttpService extends IService<MonitorHttp> {

    /**
     * <p>
     * 测试HTTP连通性
     * </p>
     *
     * @param method    请求方法
     * @param urlTarget 目标URL
     * @param parameter 请求参数
     * @return HTTP状态码
     * @author 皮锋
     * @custom.date 2022/10/10 20:09
     */
    String testMonitorHttp(String method, String urlTarget, String parameter);

}
