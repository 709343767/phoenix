package com.gitee.pifeng.server.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.server.business.web.vo.DbSession4OracleVo;
import com.gitee.pifeng.server.business.web.vo.LayUiAdminResultVo;

import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * Oracle数据库会话服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/30 12:39
 */
public interface IDbSession4OracleService {

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
     * @custom.date 2020/12/30 12:46
     */
    Page<DbSession4OracleVo> getSessionList(Long current, Long size, Long id) throws SQLException;

    /**
     * <p>
     * 结束会话
     * </p>
     *
     * @param dbSession4OracleVos Oracle数据库会话
     * @param id                  数据库ID
     * @return LayUiAdmin响应对象
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/30 15:22
     */
    LayUiAdminResultVo destroySession(List<DbSession4OracleVo> dbSession4OracleVos, Long id) throws SQLException;

}
