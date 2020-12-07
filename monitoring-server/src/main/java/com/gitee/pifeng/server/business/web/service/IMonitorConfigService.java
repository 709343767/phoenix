package com.gitee.pifeng.server.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.server.business.web.entity.MonitorConfig;
import com.gitee.pifeng.server.business.web.vo.MonitorConfigPageFormVo;

/**
 * <p>
 * 监控配置服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020-11-04
 */
public interface IMonitorConfigService extends IService<MonitorConfig> {

    /**
     * <p>
     * 更新监控配置
     * </p>
     *
     * @param monitorConfigPageFormVo 监控配置页面表单对象
     * @return 是否更新成功
     * @author 皮锋
     * @custom.date 2020/11/9 20:11
     */
    boolean update(MonitorConfigPageFormVo monitorConfigPageFormVo);

}
