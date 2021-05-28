package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbSession4MysqlVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * MySQL数据库会话服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/24 16:49
 */
public interface IDbSession4MysqlService {

    /**
     * <p>
     * 获取会话列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param id      数据库ID
     * @return 简单分页模型
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/24 16:55
     */
    Page<DbSession4MysqlVo> getSessionList(Long current, Long size, Long id) throws SQLException;

    /**
     * <p>
     * 结束会话
     * </p>
     *
     * @param dbSession4MysqlVos MySQL数据库会话
     * @param id                 数据库ID
     * @return LayUiAdmin响应对象
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/25 17:05
     */
    LayUiAdminResultVo destroySession(List<DbSession4MysqlVo> dbSession4MysqlVos, Long id) throws SQLException;

}
