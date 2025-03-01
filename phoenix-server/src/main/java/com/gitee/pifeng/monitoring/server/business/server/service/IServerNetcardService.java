package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.ServerPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorServerNetcard;

/**
 * <p>
 * 服务器网卡信息服务层接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/5 14:45
 */
public interface IServerNetcardService extends IService<MonitorServerNetcard> {

    /**
     * <p>
     * 把服务器网卡信息添加或更新到数据库
     * </p>
     *
     * @param serverPackage 服务器信息包
     * @author 皮锋
     * @custom.date 2021/1/26 11:21
     */
    void operateServerNetcard(ServerPackage serverPackage);

}
