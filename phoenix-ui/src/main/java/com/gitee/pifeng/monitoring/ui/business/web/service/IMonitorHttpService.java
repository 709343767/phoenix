package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorHttp;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorHttpVo;

/**
 * <p>
 * HTTP信息服务类
 * </p>
 *
 * @author 皮锋
 * @since 2022-04-11
 */
public interface IMonitorHttpService extends IService<MonitorHttp> {

    /**
     * <p>
     * 获取HTTP列表
     * </p>
     *
     * @param current        当前页
     * @param size           每页显示条数
     * @param hostnameSource 主机名（来源）
     * @param urlTarget      URL地址（目的地）
     * @param method         请求方法
     * @param status         状态
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/4/11 10:51
     */
    Page<MonitorHttpVo> getMonitorHttpList(Long current, Long size, String hostnameSource, String urlTarget, String method, Integer status);
}
