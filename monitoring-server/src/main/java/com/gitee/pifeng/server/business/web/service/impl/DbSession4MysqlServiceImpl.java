package com.gitee.pifeng.server.business.web.service.impl;

import cn.hutool.db.Entity;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.server.business.web.dao.IMonitorDbDao;
import com.gitee.pifeng.server.business.web.entity.MonitorDb;
import com.gitee.pifeng.server.business.web.service.IDbSession4MysqlService;
import com.gitee.pifeng.server.business.web.vo.DbSession4MysqlVo;
import com.gitee.pifeng.server.constant.sql.MySql;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;

/**
 * <p>
 * MySQL数据库会话服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/24 16:49
 */
@Slf4j
@Service
public class DbSession4MysqlServiceImpl implements IDbSession4MysqlService {

    /**
     * 数据库表数据访问对象
     */
    @Autowired
    private IMonitorDbDao monitorDbDao;

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
    @Override
    public Page<DbSession4MysqlVo> getSessionList(Long current, Long size, Long id) throws SQLException {
        // 根据ID查询到此数据库信息
        MonitorDb monitorDb = this.monitorDbDao.selectById(id);
        // url
        String url = monitorDb.getUrl();
        // 用户名
        String username = monitorDb.getUsername();
        // 密码
        String password = new String(Base64.getDecoder().decode(monitorDb.getPassword()), StandardCharsets.UTF_8);
        // 数据源
        @Cleanup
        SimpleDataSource ds = new SimpleDataSource(url, username, password);
        @Cleanup
        Connection connection = ds.getConnection();
        List<Entity> entityList = SqlExecutor.query(connection, MySql.SESSION_LIST, new EntityListHandler());

        return null;
    }

}
