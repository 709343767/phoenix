package com.gitee.pifeng.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.server.business.server.entity.MonitorDb;

/**
 * <p>
 * 数据库表服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020-12-19
 */
public interface IMonitorDbService extends IService<MonitorDb> {

    /**
     * <p>
     * 更新数据库信息
     * </p>
     *
     * @param monitorDb 数据库
     * @return 更新记录数
     * @author 皮锋
     * @custom.date 2020/11/23 11:05
     */
    int updateMonitorDb(MonitorDb monitorDb);

}