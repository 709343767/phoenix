package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorDb;

/**
 * <p>
 * 数据库表服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020-12-19
 */
public interface IDbService extends IService<MonitorDb> {

    /**
     * <p>
     * 测试数据库连通性
     * </p>
     *
     * @param url      数据库URL
     * @param dbType   数据库类型
     * @param username 用户名
     * @param password 密码
     * @return true 或者 false
     * @author 皮锋
     * @custom.date 2022/10/10 22:04
     */
    Boolean testMonitorDb(String url, String dbType, String username, String password);

}