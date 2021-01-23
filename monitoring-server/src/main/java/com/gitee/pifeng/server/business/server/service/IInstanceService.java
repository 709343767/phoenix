package com.gitee.pifeng.server.business.server.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.server.business.server.entity.MonitorInstance;

/**
 * <p>
 * 应用实例服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/6/29 15:20
 */
public interface IInstanceService extends IService<MonitorInstance> {

    /**
     * <p>
     * 更新应用实例
     * </p>
     *
     * @param monitorInstance     应用实例
     * @param lambdaUpdateWrapper 更新条件
     * @return 更新记录数
     * @author 皮锋
     * @custom.date 2020/6/29 15:27
     */
    int updateInstance(MonitorInstance monitorInstance, LambdaUpdateWrapper<MonitorInstance> lambdaUpdateWrapper);

}
