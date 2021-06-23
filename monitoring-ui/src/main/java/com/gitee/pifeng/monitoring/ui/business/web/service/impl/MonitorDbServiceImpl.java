package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.db.dialect.DriverUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorDbDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorDb;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorDbService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeDbVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorDbVo;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * 数据库表数据访问对象
     */
    @Autowired
    private IMonitorDbDao monitorDbDao;

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
    @Override
    public Page<MonitorDbVo> getMonitorDbList(Long current, Long size, String connName, String url, String dbType, String isOnline) {
        // 查询数据库
        IPage<MonitorDb> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorDb> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 只查询部分字段
        lambdaQueryWrapper.select(MonitorDb::getId, MonitorDb::getConnName, MonitorDb::getUrl,
                MonitorDb::getDbType, MonitorDb::getIsOnline, MonitorDb::getDbDesc,
                MonitorDb::getInsertTime, MonitorDb::getUpdateTime);
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
        if (StringUtils.isBlank(monitorDb.getPassword())) {
            // mybatis-plus不会更新值为null字段
            monitorDb.setPassword(null);
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
     * @param monitorDbVos 数据库信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/12/19 20:59
     */
    @Override
    public LayUiAdminResultVo deleteMonitorDb(List<MonitorDbVo> monitorDbVos) {
        List<Long> ids = Lists.newArrayList();
        for (MonitorDbVo monitorDbVo : monitorDbVos) {
            ids.add(monitorDbVo.getId());
        }
        this.monitorDbDao.deleteBatchIds(ids);
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
                .dbConnectRate(map.get("dbConnectRate").toString())
                .build();
    }

}
