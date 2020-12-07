package com.gitee.pifeng.server.business.server.service;

import java.util.Map;

/**
 * <p>
 * 监控配置服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/11/4 11:05
 */
public interface IMonitorConfigService {

    /**
     * <p>
     * 加载所有监控配置
     * </p>
     *
     * @return 所有监控配置
     * @author 皮锋
     * @custom.date 2020/11/4 11:29
     */
    Map<String, Object> loadAllMonitorConfig();

}
