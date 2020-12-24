package com.gitee.pifeng.server.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.server.business.web.vo.DbSession4MysqlVo;

import java.sql.SQLException;

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

}
