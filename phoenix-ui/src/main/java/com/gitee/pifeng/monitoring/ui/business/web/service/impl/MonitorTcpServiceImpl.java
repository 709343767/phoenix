package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorTcpDao;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorTcpHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcp;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcpHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorTcpService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeTcpVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorTcpVo;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * @param current        当前页
     * @param size           每页显示条数
     * @param hostnameSource 主机名（来源）
     * @param hostnameTarget 主机名（目的地）
     * @param portTarget     目标端口
     * @param status         状态（0：网络不通，1：网络正常）
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2022/1/11 9:33
     */
    @Override
    public Page<MonitorTcpVo> getMonitorTcpList(Long current, Long size, String hostnameSource, String hostnameTarget, Integer portTarget, String status) {
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
     * @param monitorTcpVos TCP信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 9:45
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public LayUiAdminResultVo deleteMonitorTcp(List<MonitorTcpVo> monitorTcpVos) {
        List<Long> ids = Lists.newArrayList();
        for (MonitorTcpVo monitorTcpVo : monitorTcpVos) {
            ids.add(monitorTcpVo.getId());
        }
        // 删除TCP历史记录表
        LambdaUpdateWrapper<MonitorTcpHistory> monitorTcpHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorTcpHistoryLambdaUpdateWrapper.in(MonitorTcpHistory::getTcpId, ids);
        this.monitorTcpHistoryDao.delete(monitorTcpHistoryLambdaUpdateWrapper);
        // 删除TCP信息表
        LambdaUpdateWrapper<MonitorTcp> monitorTcpLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorTcpLambdaUpdateWrapper.in(MonitorTcp::getId, ids);
        this.monitorTcpDao.delete(monitorTcpLambdaUpdateWrapper);
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
        // 根据目标主机和目标端口号，查询数据库中是否已经存在此目主机P和目标端口号的记录
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
        int result = this.monitorTcpDao.updateById(monitorTcp);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
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

}
