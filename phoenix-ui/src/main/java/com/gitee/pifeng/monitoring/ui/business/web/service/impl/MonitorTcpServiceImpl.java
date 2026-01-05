package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.common.constant.monitortype.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.plug.core.Sender;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorTcpDao;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorTcpHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcp;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcpHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorRealtimeMonitoringService;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorTcpService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeTcpVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorTcpVo;
import com.gitee.pifeng.monitoring.ui.constant.UrlConstants;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.core.UiPackageConstructor;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * TCP信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-01-10
 */
@Service
public class MonitorTcpServiceImpl extends ServiceImpl<IMonitorTcpDao, MonitorTcp> implements IMonitorTcpService {

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
     * TCP信息数据访问对象
     */
    @Autowired
    private IMonitorTcpDao monitorTcpDao;

    /**
     * TCP信息历史记录数据访问对象
     */
    @Autowired
    private IMonitorTcpHistoryDao monitorTcpHistoryDao;

    /**
     * <p>
     * 获取TCP列表
     * </p>
     *
     * @param current         当前页
     * @param size            每页显示条数
     * @param hostnameSource  主机名（来源）
     * @param hostnameTarget  主机名（目的地）
     * @param portTarget      目标端口
     * @param status          状态（0：网络不通，1：网络正常）
     * @param monitorEnv      监控环境
     * @param monitorGroup    监控分组
     * @param descr           描述
     * @param isEnableMonitor 是否开启监控（0：不开启监控；1：开启监控）
     * @param isEnableAlarm   是否开启告警（0：不开启告警；1：开启告警）
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2022/1/11 9:33
     */
    @Override
    public Page<MonitorTcpVo> getMonitorTcpList(Long current, Long size, String hostnameSource, String hostnameTarget,
                                                Integer portTarget, String status, String monitorEnv, String monitorGroup,
                                                String descr, String isEnableMonitor, String isEnableAlarm) {
        // 查询数据库
        IPage<MonitorTcp> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorTcp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(hostnameSource)) {
            lambdaQueryWrapper.like(MonitorTcp::getHostnameSource, hostnameSource);
        }
        if (StringUtils.isNotBlank(hostnameTarget)) {
            lambdaQueryWrapper.like(MonitorTcp::getHostnameTarget, hostnameTarget);
        }
        if (portTarget != null) {
            lambdaQueryWrapper.eq(MonitorTcp::getPortTarget, portTarget);
        }
        if (StringUtils.isNotBlank(status)) {
            // -1 用来表示状态未知
            if (StringUtils.equals(status, ZeroOrOneConstants.MINUS_ONE)) {
                // 状态为 null 或 空字符串
                lambdaQueryWrapper.and(wrapper -> wrapper.isNull(MonitorTcp::getStatus).or().eq(MonitorTcp::getStatus, ""));
            } else {
                lambdaQueryWrapper.eq(MonitorTcp::getStatus, status);
            }
        }
        if (StringUtils.isNotBlank(monitorEnv)) {
            lambdaQueryWrapper.eq(MonitorTcp::getMonitorEnv, monitorEnv);
        }
        if (StringUtils.isNotBlank(monitorGroup)) {
            lambdaQueryWrapper.eq(MonitorTcp::getMonitorGroup, monitorGroup);
        }
        if (StringUtils.isNotBlank(descr)) {
            lambdaQueryWrapper.like(MonitorTcp::getDescr, descr);
        }
        if (StringUtils.isNotBlank(isEnableMonitor)) {
            lambdaQueryWrapper.eq(MonitorTcp::getIsEnableMonitor, isEnableMonitor);
        }
        if (StringUtils.isNotBlank(isEnableAlarm)) {
            lambdaQueryWrapper.eq(MonitorTcp::getIsEnableAlarm, isEnableAlarm);
        }
        IPage<MonitorTcp> monitorTcpPage = this.monitorTcpDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorTcp> monitorTcps = monitorTcpPage.getRecords();
        // 转换成TCP信息表现层对象
        List<MonitorTcpVo> monitorTcpVos = Lists.newLinkedList();
        for (MonitorTcp monitorTcp : monitorTcps) {
            MonitorTcpVo monitorTcpVo = MonitorTcpVo.builder().build().convertFor(monitorTcp);
            monitorTcpVos.add(monitorTcpVo);
        }
        // 设置返回对象
        Page<MonitorTcpVo> monitorTcpVoPage = new Page<>();
        monitorTcpVoPage.setRecords(monitorTcpVos);
        monitorTcpVoPage.setTotal(monitorTcpPage.getTotal());
        return monitorTcpVoPage;
    }

    /**
     * <p>
     * 删除TCP
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 9:45
     */
    @Retryable
    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    @Override
    public LayUiAdminResultVo deleteMonitorTcp(List<Long> ids) {
        // 删除TCP历史记录表
        LambdaUpdateWrapper<MonitorTcpHistory> monitorTcpHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorTcpHistoryLambdaUpdateWrapper.in(MonitorTcpHistory::getTcpId, ids);
        this.monitorTcpHistoryDao.delete(monitorTcpHistoryLambdaUpdateWrapper);
        // 删除TCP信息表
        LambdaUpdateWrapper<MonitorTcp> monitorTcpLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorTcpLambdaUpdateWrapper.in(MonitorTcp::getId, ids);
        this.monitorTcpDao.delete(monitorTcpLambdaUpdateWrapper);
        // 注意：删除实时监控信息，这个不要忘记了
        this.monitorRealtimeMonitoringService.delete(MonitorTypeEnums.TCP4SERVICE, null, ids);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 添加TCP信息
     * </p>
     *
     * @param monitorTcpVo TCP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 10:17
     */
    @Override
    public LayUiAdminResultVo addMonitorTcp(MonitorTcpVo monitorTcpVo) {
        // 根据目标主机和目标端口号，查询数据库中是否已经存在此目标主机和目标端口号的记录
        LambdaQueryWrapper<MonitorTcp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorTcp::getHostnameTarget, monitorTcpVo.getHostnameTarget());
        lambdaQueryWrapper.eq(MonitorTcp::getPortTarget, monitorTcpVo.getPortTarget());
        MonitorTcp dbMonitorTcp = this.monitorTcpDao.selectOne(lambdaQueryWrapper);
        if (dbMonitorTcp != null) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        MonitorTcp monitorTcp = monitorTcpVo.convertTo();
        monitorTcp.setInsertTime(new Date());
        monitorTcp.setOfflineCount(0);
        if (StringUtils.isBlank(monitorTcpVo.getMonitorEnv())) {
            monitorTcp.setMonitorEnv(null);
        }
        if (StringUtils.isBlank(monitorTcpVo.getMonitorGroup())) {
            monitorTcp.setMonitorGroup(null);
        }
        if (StringUtils.isBlank(monitorTcpVo.getIsEnableMonitor())) {
            monitorTcp.setIsEnableMonitor(ZeroOrOneConstants.ZERO);
        }
        if (StringUtils.isBlank(monitorTcpVo.getIsEnableAlarm())) {
            monitorTcp.setIsEnableAlarm(ZeroOrOneConstants.ZERO);
        }
        int result = this.monitorTcpDao.insert(monitorTcp);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

    /**
     * <p>
     * 编辑TCP信息
     * </p>
     *
     * @param monitorTcpVo TCP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 14:10
     */
    @Override
    public LayUiAdminResultVo editMonitorTcp(MonitorTcpVo monitorTcpVo) {
        // 根据目标主机和目标端口号，查询数据库中是否已经存在此目标主机和目标端口号的记录
        LambdaQueryWrapper<MonitorTcp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 去掉它自己这条记录
        lambdaQueryWrapper.ne(MonitorTcp::getId, monitorTcpVo.getId());
        lambdaQueryWrapper.eq(MonitorTcp::getHostnameTarget, monitorTcpVo.getHostnameTarget());
        lambdaQueryWrapper.eq(MonitorTcp::getPortTarget, monitorTcpVo.getPortTarget());
        MonitorTcp dbMonitorTcp = this.monitorTcpDao.selectOne(lambdaQueryWrapper);
        if (dbMonitorTcp != null) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        MonitorTcp monitorTcp = monitorTcpVo.convertTo();
        monitorTcp.setUpdateTime(new Date());
        if (StringUtils.isBlank(monitorTcpVo.getMonitorEnv())) {
            monitorTcp.setMonitorEnv(null);
        }
        if (StringUtils.isBlank(monitorTcpVo.getMonitorGroup())) {
            monitorTcp.setMonitorGroup(null);
        }
        if (StringUtils.isBlank(monitorTcpVo.getIsEnableMonitor())) {
            monitorTcp.setIsEnableMonitor(ZeroOrOneConstants.ZERO);
        }
        if (StringUtils.isBlank(monitorTcpVo.getIsEnableAlarm())) {
            monitorTcp.setIsEnableAlarm(ZeroOrOneConstants.ZERO);
        }
        int result = this.monitorTcpDao.updateById(monitorTcp);
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
        LambdaUpdateWrapper<MonitorTcp> tcpLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        // 设置更新条件
        tcpLambdaUpdateWrapper.eq(MonitorTcp::getId, id);
        // 设置更新字段
        tcpLambdaUpdateWrapper.set(MonitorTcp::getIsEnableMonitor, isEnableMonitor);
        // 如果不监控，状态设置为未知
        if (StringUtils.equals(isEnableMonitor, ZeroOrOneConstants.ZERO)) {
            tcpLambdaUpdateWrapper.set(MonitorTcp::getStatus, null);
        }
        this.monitorTcpDao.update(null, tcpLambdaUpdateWrapper);
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
        LambdaUpdateWrapper<MonitorTcp> tcpLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        // 设置更新条件
        tcpLambdaUpdateWrapper.eq(MonitorTcp::getId, id);
        // 设置更新字段
        tcpLambdaUpdateWrapper.set(MonitorTcp::getIsEnableAlarm, isEnableAlarm);
        this.monitorTcpDao.update(null, tcpLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 获取home页的TCP信息
     * </p>
     *
     * @return home页的TCP信息表现层对象
     * @author 皮锋
     * @custom.date 2022/1/27 10:41
     */
    @Override
    public HomeTcpVo getHomeTcpInfo() {
        // TCP正常率统计
        Map<String, Object> map = this.monitorTcpDao.getTcpNormalRateStatistics();
        return HomeTcpVo.builder()
                .tcpSum(NumberUtil.parseInt(map.get("tcpSum").toString()))
                .tcpConnectSum(NumberUtil.parseInt(map.get("tcpConnectSum").toString()))
                .tcpDisconnectSum(NumberUtil.parseInt(map.get("tcpDisconnectSum").toString()))
                .tcpUnsentSum(NumberUtil.parseInt(map.get("tcpUnsentSum").toString()))
                .tcpConnectRate(NumberUtil.round(map.get("tcpConnectRate").toString(), 2).toString())
                .build();
    }

    /**
     * <p>
     * 测试TCP连通性
     * </p>
     *
     * @param monitorTcpVo TCP信息表现层对象
     * @return layUiAdmin响应对象：TCP连通性
     * @throws IOException IO异常
     * @author 皮锋
     * @custom.date 2022/10/12 21:41
     */
    @Override
    public LayUiAdminResultVo testMonitorTcp(MonitorTcpVo monitorTcpVo) throws IOException {
        // 封装请求数据
        JSONObject extraMsg = new JSONObject();
        extraMsg.put("hostnameTarget", monitorTcpVo.getHostnameTarget());
        extraMsg.put("portTarget", monitorTcpVo.getPortTarget());
        BaseRequestPackage baseRequestPackage = this.uiPackageConstructor.structureBaseRequestPackage(extraMsg);
        // 从服务端获取数据
        String resultStr = Sender.send(UrlConstants.TEST_MONITOR_TCP_URL, baseRequestPackage.toJsonString());
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
