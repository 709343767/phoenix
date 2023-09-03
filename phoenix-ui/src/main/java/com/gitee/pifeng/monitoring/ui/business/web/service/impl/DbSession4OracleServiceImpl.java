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
import com.gitee.pifeng.monitoring.ui.business.web.service.IDbSession4OracleService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbSession4OracleVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.constant.UrlConstants;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.core.UiPackageConstructor;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * Oracle数据库会话服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/30 12:39
 */
@Slf4j
@Service
public class DbSession4OracleServiceImpl implements IDbSession4OracleService {

    /**
     * 数据库表数据访问对象
     */
    @Autowired
    private IMonitorDbDao monitorDbDao;

    /**
     * UI端包构造器
     */
    @Autowired
    private UiPackageConstructor uiPackageConstructor;

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
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/12/30 12:46
     */
    @Override
    public Page<DbSession4OracleVo> getSessionList(Long current, Long size, Long id) throws NetException, IOException {
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
        BaseRequestPackage baseRequestPackage = this.uiPackageConstructor.structureBaseRequestPackage(extraMsg);
        // 从服务端获取数据
        String resultStr = Sender.send(UrlConstants.ORACLE_GET_SESSION_LIST_URL, baseRequestPackage.toJsonString());
        BaseResponsePackage baseResponsePackage = JSON.parseObject(resultStr, BaseResponsePackage.class);
        Result result = baseResponsePackage.getResult();
        String msg = result.getMsg();
        List<Entity> entityList = JSON.parseArray(msg, Entity.class);
        List<DbSession4OracleVo> dbSession4OracleVos = Lists.newArrayList();
        for (Entity entity : entityList) {
            Long sid = entity.getLong("SID");
            Long serial = entity.getLong("SERIAL#");
            String uname = entity.getStr("USERNAME", StandardCharsets.UTF_8);
            String schemaName = entity.getStr("SCHEMANAME", StandardCharsets.UTF_8);
            String type = entity.getStr("TYPE", StandardCharsets.UTF_8);
            String state = entity.getStr("STATE", StandardCharsets.UTF_8);
            Date logonTime = new Date(entity.getLong("LOGONTIME"));
            String machine = entity.getStr("MACHINE", StandardCharsets.UTF_8);
            String osUser = entity.getStr("OSUSER", StandardCharsets.UTF_8);
            String program = entity.getStr("PROGRAM", StandardCharsets.UTF_8);
            String event = entity.getStr("EVENT", StandardCharsets.UTF_8);
            Long waitTime = entity.getLong("WAITTIME");
            String sql = entity.getStr("SQL", StandardCharsets.UTF_8);
            DbSession4OracleVo dbSession4OracleVo = DbSession4OracleVo.builder()
                    .sid(sid)
                    .serial(serial)
                    .username(uname)
                    .schemaName(schemaName)
                    .type(type)
                    .state(state)
                    .logonTime(logonTime)
                    .machine(machine)
                    .osUser(osUser)
                    .program(program)
                    .event(event)
                    .waitTime(DateUtil.formatBetween(waitTime * 1000L, BetweenFormatter.Level.SECOND))
                    .sql(sql)
                    .build();
            dbSession4OracleVos.add(dbSession4OracleVo);
        }
        // 设置返回对象
        Page<DbSession4OracleVo> dbSession4OracleVoPage = new Page<>();
        dbSession4OracleVoPage.setRecords(dbSession4OracleVos);
        dbSession4OracleVoPage.setTotal(dbSession4OracleVos.size());
        dbSession4OracleVoPage.setCurrent(current);
        dbSession4OracleVoPage.setSize(size);
        return dbSession4OracleVoPage;
    }

    /**
     * <p>
     * 结束会话
     * </p>
     *
     * @param dbSession4OracleVos Oracle数据库会话
     * @param id                  数据库ID
     * @return LayUiAdmin响应对象
     * @throws NetException   自定义获取网络信息异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/12/30 15:22
     */
    @Override
    public LayUiAdminResultVo destroySession(List<DbSession4OracleVo> dbSession4OracleVos, Long id) throws IOException, NetException {
        List<Long> sids = dbSession4OracleVos.stream().map(DbSession4OracleVo::getSid).collect(Collectors.toList());
        List<Long> serials = dbSession4OracleVos.stream().map(DbSession4OracleVo::getSerial).collect(Collectors.toList());
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
        extraMsg.put("sids", sids);
        extraMsg.put("serials", serials);
        BaseRequestPackage baseRequestPackage = this.uiPackageConstructor.structureBaseRequestPackage(extraMsg);
        // 从服务端获取数据
        String resultStr = Sender.send(UrlConstants.ORACLE_DESTROY_SESSION_URL, baseRequestPackage.toJsonString());
        BaseResponsePackage baseResponsePackage = JSON.parseObject(resultStr, BaseResponsePackage.class);
        Result result = baseResponsePackage.getResult();
        boolean b = result.isSuccess();
        if (b) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

}
