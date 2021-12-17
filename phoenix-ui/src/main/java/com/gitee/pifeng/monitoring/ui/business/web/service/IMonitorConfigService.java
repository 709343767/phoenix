package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorConfig;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorConfigPageFormVo;
import org.hyperic.sigar.SigarException;

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
     * 获取监控配置页面表单信息
     * </p>
     *
     * @return 监控配置页面表单对象
     * @author 皮锋
     * @custom.date 2021/1/28 9:10
     */
    MonitorConfigPageFormVo getMonitorConfigPageFormInfo();

    /**
     * <p>
     * 更新监控配置
     * </p>
     *
     * @param monitorConfigPageFormVo 监控配置页面表单对象
     * @return layUiAdmin响应对象：如果更新数据库成功，LayUiAdminResultVo.data="success"；<br>
     * 如果更新数据库成功，但是更新监控服务端配置失败，LayUiAdminResultVo.data="refreshFail"；<br>
     * 否则，LayUiAdminResultVo.data="fail"。
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/11/9 20:11
     */
    LayUiAdminResultVo update(MonitorConfigPageFormVo monitorConfigPageFormVo) throws NetException, SigarException;

}
