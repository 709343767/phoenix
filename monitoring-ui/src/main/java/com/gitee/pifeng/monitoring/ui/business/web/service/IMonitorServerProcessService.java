package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerProcess;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerProcessVo;

import java.util.List;

/**
 * <p>
 * 服务器进程服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-09-15
 */
public interface IMonitorServerProcessService extends IService<MonitorServerProcess> {

    /**
     * <p>
     * 获取服务器详情页面服务器进程信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器进程表现层对象
     * @author 皮锋
     * @custom.date 2021/9/18 15:56
     */
    List<MonitorServerProcessVo> getServerDetailPageServerProcessInfo(String ip);

}
