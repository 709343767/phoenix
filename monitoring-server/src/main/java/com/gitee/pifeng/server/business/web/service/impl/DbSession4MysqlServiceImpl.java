package com.gitee.pifeng.server.business.web.service.impl;

import cn.hutool.core.date.BetweenFormater;
import cn.hutool.core.date.DateUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.server.business.web.dao.IMonitorDbDao;
import com.gitee.pifeng.server.business.web.entity.MonitorDb;
import com.gitee.pifeng.server.business.web.service.IDbSession4MysqlService;
import com.gitee.pifeng.server.business.web.vo.DbSession4MysqlVo;
import com.gitee.pifeng.server.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.server.constant.WebResponseConstants;
import com.gitee.pifeng.server.constant.sql.MySql;
import com.google.common.collect.Lists;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

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
        List<DbSession4MysqlVo> dbSession4MysqlVos = Lists.newArrayList();
        for (Entity entity : entityList) {
            Long sessionId = entity.getLong("Id");
            String user = entity.getStr("User", StandardCharsets.UTF_8);
            String host = entity.getStr("Host", StandardCharsets.UTF_8);
            String db = entity.getStr("db", StandardCharsets.UTF_8);
            String command = entity.getStr("Command", StandardCharsets.UTF_8);
            Integer time = entity.getInt("Time");
            String state = entity.getStr("State", StandardCharsets.UTF_8);
            String info = entity.getStr("Info", StandardCharsets.UTF_8);
            DbSession4MysqlVo dbSession4MysqlVo = DbSession4MysqlVo.builder()
                    .id(sessionId)
                    .user(user)
                    .host(host)
                    .db(db)
                    .command(command)
                    .time(DateUtil.formatBetween(time * 1000L, BetweenFormater.Level.SECOND))
                    .state(state)
                    .info(info)
                    .build();
            dbSession4MysqlVos.add(dbSession4MysqlVo);
        }
        // 设置返回对象
        Page<DbSession4MysqlVo> dbSession4MysqlVoPage = new Page<>();
        dbSession4MysqlVoPage.setRecords(dbSession4MysqlVos);
        dbSession4MysqlVoPage.setTotal(dbSession4MysqlVos.size());
        dbSession4MysqlVoPage.setCurrent(current);
        dbSession4MysqlVoPage.setSize(size);
        return dbSession4MysqlVoPage;
    }

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
    @Override
    public LayUiAdminResultVo destroySession(List<DbSession4MysqlVo> dbSession4MysqlVos, Long id) throws SQLException {
        List<Long> ids = dbSession4MysqlVos.stream().map(DbSession4MysqlVo::getId).collect(Collectors.toList());
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
        for (Long sessionId : ids) {
            try {
                SqlExecutor.execute(connection, MySql.KILL_SESSION, sessionId);
            } catch (SQLException e) {
                log.error("结束会话异常！", e);
            }
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

}
