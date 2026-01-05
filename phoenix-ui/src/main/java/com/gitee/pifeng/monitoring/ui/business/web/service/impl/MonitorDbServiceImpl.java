package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.db.dialect.DriverUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.DbEnums;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.plug.core.Sender;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorDbDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorDb;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorDbService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorRealtimeMonitoringService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeDbVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorDbVo;
import com.gitee.pifeng.monitoring.ui.constant.DbDriverClassConstants;
import com.gitee.pifeng.monitoring.ui.constant.UrlConstants;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.core.UiPackageConstructor;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 数据库表服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020-12-19
 */
@Service
public class MonitorDbServiceImpl extends ServiceImpl<IMonitorDbDao, MonitorDb> implements IMonitorDbService {

    /**
     * UI端包构造器
     */
    @Autowired
    private UiPackageConstructor uiPackageConstructor;

    /**
     * 实时监控服务类
     */
    @Autowired
    private IMonitorRealtimeMonitoringService monitorRealtimeMonitoringService;

    /**
     * 数据库表数据访问对象
     */
    @Autowired
    private IMonitorDbDao monitorDbDao;

    /**
     * <p>
     * 获取数据库列表
     * </p>
     *
     * @param current         当前页
     * @param size            每页显示条数
     * @param connName        数据库连接名
     * @param url             数据库URL
     * @param dbType          数据库类型
     * @param isOnline        数据库状态
     * @param monitorEnv      监控环境
     * @param monitorGroup    监控分组
     * @param dbDesc          描述
     * @param isEnableMonitor 是否开启监控（0：不开启监控；1：开启监控）
     * @param isEnableAlarm   是否开启告警（0：不开启告警；1：开启告警）
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2020/12/19 17:37
     */
    @Override
    public Page<MonitorDbVo> getMonitorDbList(Long current, Long size, String connName, String url, String dbType,
                                              String isOnline, String monitorEnv, String monitorGroup, String dbDesc,
                                              String isEnableMonitor, String isEnableAlarm) {
        // 查询数据库
        IPage<MonitorDb> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorDb> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 只查询部分字段
        lambdaQueryWrapper.select(MonitorDb::getId, MonitorDb::getConnName, MonitorDb::getUrl,
                MonitorDb::getDbType, MonitorDb::getIsOnline, MonitorDb::getOfflineCount,
                MonitorDb::getDbDesc, MonitorDb::getInsertTime, MonitorDb::getUpdateTime,
                MonitorDb::getMonitorEnv, MonitorDb::getMonitorGroup, MonitorDb::getIsEnableMonitor,
                MonitorDb::getIsEnableAlarm);
        if (StringUtils.isNotBlank(connName)) {
            lambdaQueryWrapper.like(MonitorDb::getConnName, connName);
        }
        if (StringUtils.isNotBlank(url)) {
            lambdaQueryWrapper.like(MonitorDb::getUrl, url);
        }
        if (StringUtils.isNotBlank(dbType)) {
            lambdaQueryWrapper.like(MonitorDb::getDbType, dbType);
        }
        if (StringUtils.isNotBlank(isOnline)) {
            // -1 用来表示状态未知
            if (StringUtils.equals(isOnline, ZeroOrOneConstants.MINUS_ONE)) {
                // 状态为 null 或 空字符串
                lambdaQueryWrapper.and(wrapper -> wrapper.isNull(MonitorDb::getIsOnline).or().eq(MonitorDb::getIsOnline, ""));
            } else {
                lambdaQueryWrapper.eq(MonitorDb::getIsOnline, isOnline);
            }
        }
        if (StringUtils.isNotBlank(monitorEnv)) {
            lambdaQueryWrapper.eq(MonitorDb::getMonitorEnv, monitorEnv);
        }
        if (StringUtils.isNotBlank(monitorGroup)) {
            lambdaQueryWrapper.eq(MonitorDb::getMonitorGroup, monitorGroup);
        }
        if (StringUtils.isNotBlank(dbDesc)) {
            lambdaQueryWrapper.like(MonitorDb::getDbDesc, dbDesc);
        }
        if (StringUtils.isNotBlank(isEnableMonitor)) {
            lambdaQueryWrapper.eq(MonitorDb::getIsEnableMonitor, isEnableMonitor);
        }
        if (StringUtils.isNotBlank(isEnableAlarm)) {
            lambdaQueryWrapper.eq(MonitorDb::getIsEnableAlarm, isEnableAlarm);
        }
        IPage<MonitorDb> monitorDbPage = this.monitorDbDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorDb> monitorDbs = monitorDbPage.getRecords();
        // 转换成数据库信息表现层对象
        List<MonitorDbVo> monitorDbVos = Lists.newLinkedList();
        for (MonitorDb monitorDb : monitorDbs) {
            MonitorDbVo monitorDbVo = MonitorDbVo.builder().build().convertFor(monitorDb);
            monitorDbVos.add(monitorDbVo);
        }
        // 设置返回对象
        Page<MonitorDbVo> monitorDbVoPage = new Page<>();
        monitorDbVoPage.setRecords(monitorDbVos);
        monitorDbVoPage.setTotal(monitorDbPage.getTotal());
        return monitorDbVoPage;
    }

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
    @Override
    public LayUiAdminResultVo editMonitorDb(MonitorDbVo monitorDbVo) {
        // 去掉无用的空格
        monitorDbVo.setUrl(StringUtils.trim(monitorDbVo.getUrl()));
        // 根据url，查询数据库中是否已经存在此url的记录
        LambdaQueryWrapper<MonitorDb> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 去掉它自己这条记录
        lambdaQueryWrapper.ne(MonitorDb::getId, monitorDbVo.getId());
        lambdaQueryWrapper.eq(MonitorDb::getUrl, monitorDbVo.getUrl());
        Integer count = this.monitorDbDao.selectCount(lambdaQueryWrapper);
        // 已经存在
        if (count != null && count > 0) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        // 转成数据库表实体对象
        MonitorDb monitorDb = monitorDbVo.convertTo();
        // 数据库类型
        String dbType = monitorDb.getDbType();
        monitorDb.setDriverClass(DriverUtil.identifyDriver(dbType));
        // Redis数据库
        if (StringUtils.equalsIgnoreCase(DbEnums.Redis.name(), dbType)) {
            monitorDb.setDriverClass(DbDriverClassConstants.REDIS_DRIVER_CLASS);
        }
        // mongo数据库
        if (StringUtils.equalsIgnoreCase(DbEnums.Mongo.name(), dbType)) {
            monitorDb.setDriverClass(DbDriverClassConstants.MONGO_DRIVER_CLASS);
        }
        if (StringUtils.isBlank(monitorDb.getPassword())) {
            // mybatis-plus不会更新值为null字段
            monitorDb.setPassword(null);
        }
        if (StringUtils.isBlank(monitorDbVo.getMonitorEnv())) {
            monitorDb.setMonitorEnv(null);
        }
        if (StringUtils.isBlank(monitorDbVo.getMonitorGroup())) {
            monitorDb.setMonitorGroup(null);
        }
        if (StringUtils.isBlank(monitorDbVo.getIsEnableMonitor())) {
            monitorDb.setIsEnableMonitor(ZeroOrOneConstants.ZERO);
        }
        if (StringUtils.isBlank(monitorDbVo.getIsEnableAlarm())) {
            monitorDb.setIsEnableAlarm(ZeroOrOneConstants.ZERO);
        }
        monitorDb.setUpdateTime(new Date());
        // 更新
        int result = this.monitorDbDao.updateById(monitorDb);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

    /**
     * <p>
     * 设置是否开启监控（0：不开启监控；1：开启监控）
     * </p>
     *
     * @param id              主键ID
     * @param isEnableMonitor 是否开启监控（0：不开启监控；1：开启监控）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:20
     */
    @Override
    public LayUiAdminResultVo setIsEnableMonitor(Long id, String isEnableMonitor) {
        LambdaUpdateWrapper<MonitorDb> dbLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        // 设置更新条件
        dbLambdaUpdateWrapper.eq(MonitorDb::getId, id);
        // 设置更新字段
        dbLambdaUpdateWrapper.set(MonitorDb::getIsEnableMonitor, isEnableMonitor);
        // 如果不监控，数据库状态设置为未知
        if (StringUtils.equals(isEnableMonitor, ZeroOrOneConstants.ZERO)) {
            dbLambdaUpdateWrapper.set(MonitorDb::getIsOnline, null);
        }
        this.monitorDbDao.update(null, dbLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 设置是否开启告警（0：不开启告警；1：开启告警）
     * </p>
     *
     * @param id            主键ID
     * @param isEnableAlarm 是否开启告警（0：不开启告警；1：开启告警）
     * @return 如果设置成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2024/12/10 21:37
     */
    @Override
    public LayUiAdminResultVo setIsEnableAlarm(Long id, String isEnableAlarm) {
        LambdaUpdateWrapper<MonitorDb> dbLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        // 设置更新条件
        dbLambdaUpdateWrapper.eq(MonitorDb::getId, id);
        // 设置更新字段
        dbLambdaUpdateWrapper.set(MonitorDb::getIsEnableAlarm, isEnableAlarm);
        this.monitorDbDao.update(null, dbLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

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
    @Override
    public LayUiAdminResultVo addMonitorDb(MonitorDbVo monitorDbVo) {
        // 去掉无用的空格
        monitorDbVo.setUrl(StringUtils.trim(monitorDbVo.getUrl()));
        // 根据url，查询数据库中是否已经存在此url的记录
        LambdaQueryWrapper<MonitorDb> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorDb::getUrl, monitorDbVo.getUrl());
        Integer count = this.monitorDbDao.selectCount(lambdaQueryWrapper);
        // 已经存在
        if (count != null && count > 0) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        // 转成数据库表实体对象
        MonitorDb monitorDb = monitorDbVo.convertTo();
        // 数据库类型
        String dbType = monitorDb.getDbType();
        monitorDb.setDriverClass(DriverUtil.identifyDriver(dbType));
        // Redis数据库
        if (StringUtils.equalsIgnoreCase(DbEnums.Redis.name(), dbType)) {
            monitorDb.setDriverClass(DbDriverClassConstants.REDIS_DRIVER_CLASS);
        }
        // mongo数据库
        if (StringUtils.equalsIgnoreCase(DbEnums.Mongo.name(), dbType)) {
            monitorDb.setDriverClass(DbDriverClassConstants.MONGO_DRIVER_CLASS);
        }
        if (StringUtils.isBlank(monitorDbVo.getMonitorEnv())) {
            monitorDb.setMonitorEnv(null);
        }
        if (StringUtils.isBlank(monitorDbVo.getMonitorGroup())) {
            monitorDb.setMonitorGroup(null);
        }
        if (StringUtils.isBlank(monitorDbVo.getIsEnableMonitor())) {
            monitorDb.setIsEnableMonitor(ZeroOrOneConstants.ZERO);
        }
        if (StringUtils.isBlank(monitorDbVo.getIsEnableAlarm())) {
            monitorDb.setIsEnableAlarm(ZeroOrOneConstants.ZERO);
        }
        monitorDb.setOfflineCount(0);
        monitorDb.setInsertTime(new Date());
        int result = this.monitorDbDao.insert(monitorDb);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

    /**
     * <p>
     * 删除数据库信息
     * </p>
     *
     * @param ids 数据库主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/12/19 20:59
     */
    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public LayUiAdminResultVo deleteMonitorDb(List<Long> ids) {
        this.monitorDbDao.deleteBatchIds(ids);
        // 注意：删除实时监控信息，这个不要忘记了
        this.monitorRealtimeMonitoringService.delete(MonitorTypeEnums.DATABASE, null, ids);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 获取home页的数据库信息
     * </p>
     *
     * @return home页的数据库信息表现层对象
     * @author 皮锋
     * @custom.date 2020/12/19 21:43
     */
    @Override
    public HomeDbVo getHomeDbInfo() {
        // 数据库正常率统计
        Map<String, Object> map = this.monitorDbDao.getDbNormalRateStatistics();
        return HomeDbVo.builder()
                .dbSum(NumberUtil.parseInt(map.get("dbSum").toString()))
                .dbConnectSum(NumberUtil.parseInt(map.get("dbConnectSum").toString()))
                .dbDisconnectSum(NumberUtil.parseInt(map.get("dbDisconnectSum").toString()))
                .dbUnsentSum(NumberUtil.parseInt(map.get("dbUnsentSum").toString()))
                .dbConnectRate(NumberUtil.round(map.get("dbConnectRate").toString(), 2).toString())
                .build();
    }

    /**
     * <p>
     * 测试数据库连通性
     * </p>
     *
     * @param monitorDbVo 数据库信息
     * @return layUiAdmin响应对象：网络连通性
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2022/10/9 10:16
     */
    @Override
    public LayUiAdminResultVo testMonitorDb(MonitorDbVo monitorDbVo) throws IOException {
        String password = monitorDbVo.getPassword();
        // 如果密码为空，则从数据库查询
        if (StringUtils.isBlank(password)) {
            MonitorDb monitorDb = this.monitorDbDao.selectById(monitorDbVo.getId());
            if (monitorDb != null) {
                password = monitorDb.getPassword();
            }
        }
        // 封装请求数据
        JSONObject extraMsg = new JSONObject();
        extraMsg.put("url", StringUtils.trim(monitorDbVo.getUrl()));
        extraMsg.put("dbType", monitorDbVo.getDbType());
        extraMsg.put("username", monitorDbVo.getUsername());
        extraMsg.put("password", password);
        BaseRequestPackage baseRequestPackage = this.uiPackageConstructor.structureBaseRequestPackage(extraMsg);
        // 从服务端获取数据
        String resultStr = Sender.send(UrlConstants.TEST_MONITOR_DB_URL, baseRequestPackage.toJsonString());
        BaseResponsePackage baseResponsePackage = JSON.parseObject(resultStr, BaseResponsePackage.class);
        Result result = baseResponsePackage.getResult();
        String msg = result.getMsg();
        boolean isConnected = Boolean.parseBoolean(msg);
        if (isConnected) {
            msg = WebResponseConstants.SUCCESS;
        } else {
            msg = WebResponseConstants.FAIL;
        }
        return LayUiAdminResultVo.ok(msg);
    }

}
