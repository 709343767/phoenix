package com.gitee.pifeng.server.business.web.service.impl;

import cn.hutool.core.io.unit.DataSizeUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.server.business.web.dao.IMonitorDbDao;
import com.gitee.pifeng.server.business.web.entity.MonitorDb;
import com.gitee.pifeng.server.business.web.service.IDbTableSpace4OracleService;
import com.gitee.pifeng.server.business.web.vo.DbTableSpace4OracleVo;
import com.gitee.pifeng.server.constant.sql.Oracle;
import com.gitee.pifeng.server.util.DbUtils;
import com.google.common.collect.Lists;
import lombok.Cleanup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * <p>
 * Oracle数据库表空间服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/31 16:11
 */
@Service
public class DbTableSpace4OracleServiceImpl implements IDbTableSpace4OracleService {

    /**
     * 数据库表数据访问对象
     */
    @Autowired
    private IMonitorDbDao monitorDbDao;

    /**
     * <p>
     * 获取表空间列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param id      数据库ID
     * @return 简单分页模型
     * @throws SQLException SQL异常
     * @author 皮锋
     * @custom.date 2020/12/31 16:15
     */
    @Override
    public Page<DbTableSpace4OracleVo> getTableSpaceList(Long current, Long size, Long id) throws SQLException {
        // 根据ID查询到此数据库信息
        MonitorDb monitorDb = this.monitorDbDao.selectById(id);
        // url
        String url = monitorDb.getUrl();
        //用户名
        String username = monitorDb.getUsername();
        // 密码
        String password = monitorDb.getPassword();
        @Cleanup
        Connection connection = DbUtils.getConnection(url, username, password);
        List<Entity> entityList = SqlExecutor.query(connection, Oracle.TABLE_SPACE_SELECT, new EntityListHandler());
        List<DbTableSpace4OracleVo> dbTableSpace4OracleVos = Lists.newArrayList();
        for (Entity entity : entityList) {
            Long fileId = entity.getLong("FILEID");
            String fileName = entity.getStr("FILENAME", StandardCharsets.UTF_8);
            String tablespaceName = entity.getStr("TABLESPACENAME", StandardCharsets.UTF_8);
            String total = DataSizeUtil.format(entity.getLong("TOTAL"));
            String used = DataSizeUtil.format(entity.getLong("USED"));
            String free = DataSizeUtil.format(entity.getLong("FREE"));
            double freePer = NumberUtil.round(entity.getDouble("FREEPER"), 4).doubleValue();
            double usedPer = NumberUtil.round(100D - freePer, 4).doubleValue();
            DbTableSpace4OracleVo dbTableSpace4OracleVo = DbTableSpace4OracleVo.builder()
                    .fileId(fileId)
                    .fileName(fileName)
                    .tablespaceName(tablespaceName)
                    .total(total)
                    .used(used)
                    .free(free)
                    .usedPer(usedPer)
                    .freePer(freePer)
                    .build();
            dbTableSpace4OracleVos.add(dbTableSpace4OracleVo);
        }
        // 设置返回对象
        Page<DbTableSpace4OracleVo> dbTableSpace4OracleVoPage = new Page<>();
        dbTableSpace4OracleVoPage.setRecords(dbTableSpace4OracleVos);
        dbTableSpace4OracleVoPage.setTotal(dbTableSpace4OracleVos.size());
        dbTableSpace4OracleVoPage.setCurrent(current);
        dbTableSpace4OracleVoPage.setSize(size);
        return dbTableSpace4OracleVoPage;
    }

}
