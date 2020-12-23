package com.gitee.pifeng.server.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gitee.pifeng.server.business.web.entity.MonitorDb;
import com.gitee.pifeng.server.business.web.vo.HomeDbVo;
import com.gitee.pifeng.server.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.server.business.web.vo.MonitorDbVo;

import java.util.List;

/**
 * <p>
 * 数据库表服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020-12-19
 */
public interface IMonitorDbService extends IService<MonitorDb> {

    /**
     * <p>
     * 获取数据库列表
     * </p>
     *
     * @param current  当前页
     * @param size     每页显示条数
     * @param connName 数据库连接名
     * @param url      数据库URL
     * @param dbType   数据库类型
     * @param isOnline 数据库状态
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2020/12/19 17:37
     */
    Page<MonitorDbVo> getMonitorDbList(Long current, Long size, String connName, String url, String dbType, String isOnline);

    /**
     * <p>
     * 编辑数据库信息
     * </p>
     *
     * @param monitorDbVo 数据库信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/12/19 19:56
     */
    LayUiAdminResultVo editMonitorDb(MonitorDbVo monitorDbVo);

    /**
     * <p>
     * 添加数据库信息
     * </p>
     *
     * @param monitorDbVo 数据库信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/12/19 20:05
     */
    LayUiAdminResultVo addMonitorDb(MonitorDbVo monitorDbVo);

    /**
     * <p>
     * 删除数据库信息
     * </p>
     *
     * @param monitorDbVos 数据库信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/12/19 20:59
     */
    LayUiAdminResultVo deleteMonitorDb(List<MonitorDbVo> monitorDbVos);

    /**
     * <p>
     * 获取home页的数据库信息
     * </p>
     *
     * @return home页的数据库信息表现层对象
     * @author 皮锋
     * @custom.date 2020/12/19 21:43
     */
    HomeDbVo getHomeDbInfo();

}
