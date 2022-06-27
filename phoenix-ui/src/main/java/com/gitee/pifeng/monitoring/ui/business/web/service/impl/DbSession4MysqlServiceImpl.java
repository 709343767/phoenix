package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.db.Entity;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.plug.core.Sender;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorDbDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorDb;
import com.gitee.pifeng.monitoring.ui.business.web.service.IDbSession4MysqlService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbSession4MysqlVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.constant.UrlConstants;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.core.PackageConstructor;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/12/24 16:55
     */
    @Override
    public Page<DbSession4MysqlVo> getSessionList(Long current, Long size, Long id) throws NetException, SigarException, IOException {
        // 根据ID查询到此数据库信息
        MonitorDb monitorDb = this.monitorDbDao.selectById(id);
        // url
        String url = monitorDb.getUrl();
        //用户名
        String username = monitorDb.getUsername();
        // 密码
        String password = monitorDb.getPassword();
        // 封装请求数据
        JSONObject extraMsg = new JSONObject();
        extraMsg.put("url", url);
        extraMsg.put("username", username);
        extraMsg.put("password", password);
        BaseRequestPackage baseRequestPackage = new PackageConstructor().structureBaseRequestPackage(extraMsg);
        // 从服务端获取数据
        String resultStr = Sender.send(UrlConstants.MYSQL_GET_SESSION_LIST, baseRequestPackage.toJsonString());
        BaseResponsePackage baseResponsePackage = JSON.parseObject(resultStr, BaseResponsePackage.class);
        Result result = baseResponsePackage.getResult();
        String msg = result.getMsg();
        List<Entity> entityList = JSON.parseArray(msg, Entity.class);
        // 解析数据
        List<DbSession4MysqlVo> dbSession4MysqlVos = Lists.newArrayList();
        for (Entity entity : entityList) {
            Long sessionId = entity.getLong("Id");
            String user = entity.getStr("User", StandardCharsets.UTF_8);
            String host = entity.getStr("Host", StandardCharsets.UTF_8);
            String db = entity.getStr("db", StandardCharsets.UTF_8);
            String command = entity.getStr("Command", StandardCharsets.UTF_8);
            Long time = entity.getLong("Time");
            String state = entity.getStr("State", StandardCharsets.UTF_8);
            String info = entity.getStr("Info", StandardCharsets.UTF_8);
            DbSession4MysqlVo dbSession4MysqlVo = DbSession4MysqlVo.builder()
                    .id(sessionId)
                    .user(user)
                    .host(host)
                    .db(db)
                    .command(command)
                    .time(DateUtil.formatBetween(time * 1000L, BetweenFormatter.Level.SECOND))
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
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/12/25 17:05
     */
    @Override
    public LayUiAdminResultVo destroySession(List<DbSession4MysqlVo> dbSession4MysqlVos, Long id) throws NetException, SigarException, IOException {
        List<Long> sessionIds = dbSession4MysqlVos.stream().map(DbSession4MysqlVo::getId).collect(Collectors.toList());
        // 根据ID查询到此数据库信息
        MonitorDb monitorDb = this.monitorDbDao.selectById(id);
        // url
        String url = monitorDb.getUrl();
        // 用户名
        String username = monitorDb.getUsername();
        // 密码
        String password = monitorDb.getPassword();
        // 封装请求数据
        JSONObject extraMsg = new JSONObject();
        extraMsg.put("url", url);
        extraMsg.put("username", username);
        extraMsg.put("password", password);
        extraMsg.put("sessionIds", sessionIds);
        BaseRequestPackage baseRequestPackage = new PackageConstructor().structureBaseRequestPackage(extraMsg);
        // 从服务端获取数据
        String resultStr = Sender.send(UrlConstants.MYSQL_DESTROY_SESSION, baseRequestPackage.toJsonString());
        BaseResponsePackage baseResponsePackage = JSON.parseObject(resultStr, BaseResponsePackage.class);
        Result result = baseResponsePackage.getResult();
        boolean b = result.isSuccess();
        if (b) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

}
