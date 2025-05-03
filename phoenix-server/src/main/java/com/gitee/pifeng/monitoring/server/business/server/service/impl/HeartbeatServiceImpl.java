package com.gitee.pifeng.monitoring.server.business.server.service.impl;

import com.gitee.pifeng.monitoring.common.constant.ResultMsgConstants;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.HeartbeatPackage;
import com.gitee.pifeng.monitoring.server.business.server.service.IHeartbeatService;
import com.gitee.pifeng.monitoring.server.business.server.service.IInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 心跳服务实现。
 * </p>
 * 把应用实例添加或者更新到数据库。
 *
 * @author 皮锋
 * @custom.date 2020/3/12 10:05
 */
@Service
public class HeartbeatServiceImpl implements IHeartbeatService {

    /**
     * 应用实例服务接口
     */
    @Autowired
    private IInstanceService instanceService;

    /**
     * <p>
     * 处理心跳包
     * </p>
     *
     * @param heartbeatPackage 心跳包
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020/3/12 10:18
     */
    @Override
    public Result dealHeartbeatPackage(HeartbeatPackage heartbeatPackage) {
        // 把应用实例添加或者更新到数据库
        this.instanceService.operateMonitorInstance(heartbeatPackage);
        // 返回结果
        return Result.builder().isSuccess(true).msg(ResultMsgConstants.SUCCESS).build();
    }

}
