package com.gitee.pifeng.monitoring.server.business.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;
import com.gitee.pifeng.monitoring.server.business.server.entity.MonitorJvmMemoryHistory;

/**
 * <p>
 * java虚拟机内存历史记录服务接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/4/6 9:56
 */
public interface IJvmMemoryHistoryService extends IService<MonitorJvmMemoryHistory> {

    /**
     * <p>
     * 把java虚拟机内存历史信息添加到数据库
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @author 皮锋
     * @custom.date 2020/8/28 9:28
     */
    void operateMonitorJvmMemoryHistory(JvmPackage jvmPackage);

}
