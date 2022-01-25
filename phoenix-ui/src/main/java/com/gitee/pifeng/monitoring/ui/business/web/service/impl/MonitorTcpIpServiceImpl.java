package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.ZeroOrOneConstants;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorTcpIpDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorTcpIp;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorTcpIpService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorTcpIpVo;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * TCP/IP信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-01-10
 */
@Service
public class MonitorTcpIpServiceImpl extends ServiceImpl<IMonitorTcpIpDao, MonitorTcpIp> implements IMonitorTcpIpService {

    /**
     * TCP/IP信息数据访问对象
     */
    @Autowired
    private IMonitorTcpIpDao monitorTcpIpDao;

    /**
     * <p>
     * 获取TCP/IP列表
     * </p>
     *
     * @param current    当前页
     * @param size       每页显示条数
     * @param ipSource   IP地址（来源）
     * @param ipTarget   IP地址（目的地）
     * @param portTarget 目标端口
     * @param protocol   协议
     * @param status     状态（0：网络不通，1：网络正常）
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2022/1/11 9:33
     */
    @Override
    public Page<MonitorTcpIpVo> getMonitorTcpIpList(Long current, Long size, String ipSource, String ipTarget, Integer portTarget, String protocol, String status) {
        // 查询数据库
        IPage<MonitorTcpIp> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorTcpIp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(ipSource)) {
            lambdaQueryWrapper.like(MonitorTcpIp::getIpSource, ipSource);
        }
        if (StringUtils.isNotBlank(ipTarget)) {
            lambdaQueryWrapper.like(MonitorTcpIp::getIpTarget, ipTarget);
        }
        if (portTarget != null) {
            lambdaQueryWrapper.eq(MonitorTcpIp::getPortTarget, portTarget);
        }
        if (StringUtils.isNotBlank(protocol)) {
            lambdaQueryWrapper.eq(MonitorTcpIp::getProtocol, protocol);
        }
        if (StringUtils.isNotBlank(status)) {
            // -1 用来表示状态未知
            if (StringUtils.equals(status, ZeroOrOneConstants.MINUS_ONE)) {
                // 状态为 null 或 空字符串
                lambdaQueryWrapper.and(wrapper -> wrapper.isNull(MonitorTcpIp::getStatus).or().eq(MonitorTcpIp::getStatus, ""));
            } else {
                lambdaQueryWrapper.eq(MonitorTcpIp::getStatus, status);
            }
        }
        IPage<MonitorTcpIp> monitorTcpIpPage = this.monitorTcpIpDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorTcpIp> monitorTcpIps = monitorTcpIpPage.getRecords();
        // 转换成TCP/IP信息表现层对象
        List<MonitorTcpIpVo> monitorTcpIpVos = Lists.newLinkedList();
        for (MonitorTcpIp monitorTcpIp : monitorTcpIps) {
            MonitorTcpIpVo monitorTcpIpVo = MonitorTcpIpVo.builder().build().convertFor(monitorTcpIp);
            monitorTcpIpVos.add(monitorTcpIpVo);
        }
        // 设置返回对象
        Page<MonitorTcpIpVo> monitorTcpIpVoPage = new Page<>();
        monitorTcpIpVoPage.setRecords(monitorTcpIpVos);
        monitorTcpIpVoPage.setTotal(monitorTcpIpPage.getTotal());
        return monitorTcpIpVoPage;
    }

    /**
     * <p>
     * 删除TCP
     * </p>
     *
     * @param monitorTcpIpVos TCP/IP信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 9:45
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public LayUiAdminResultVo deleteMonitorTcpIp(List<MonitorTcpIpVo> monitorTcpIpVos) {
        List<Long> ids = Lists.newArrayList();
        for (MonitorTcpIpVo monitorTcpIpVo : monitorTcpIpVos) {
            ids.add(monitorTcpIpVo.getId());
        }
        LambdaUpdateWrapper<MonitorTcpIp> monitorTcpIpLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorTcpIpLambdaUpdateWrapper.in(MonitorTcpIp::getId, ids);
        this.monitorTcpIpDao.delete(monitorTcpIpLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 添加TCP/IP信息
     * </p>
     *
     * @param monitorTcpIpVo TCP/IP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 10:17
     */
    @Override
    public LayUiAdminResultVo addMonitorTcpIp(MonitorTcpIpVo monitorTcpIpVo) {
        // 根据目标IP和目标端口号，查询数据库中是否已经存在此目标IP和目标端口号的记录
        LambdaQueryWrapper<MonitorTcpIp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorTcpIp::getIpTarget, monitorTcpIpVo.getIpTarget());
        lambdaQueryWrapper.eq(MonitorTcpIp::getPortTarget, monitorTcpIpVo.getPortTarget());
        MonitorTcpIp dbMonitorTcpIp = this.monitorTcpIpDao.selectOne(lambdaQueryWrapper);
        if (dbMonitorTcpIp != null) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        MonitorTcpIp monitorTcpIp = monitorTcpIpVo.convertTo();
        monitorTcpIp.setInsertTime(new Date());
        monitorTcpIp.setOfflineCount(0);
        int result = this.monitorTcpIpDao.insert(monitorTcpIp);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

    /**
     * <p>
     * 编辑TCP/IP信息
     * </p>
     *
     * @param monitorTcpIpVo TCP/IP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 14:10
     */
    @Override
    public LayUiAdminResultVo editMonitorTcpIp(MonitorTcpIpVo monitorTcpIpVo) {
        // 根据目标IP和目标端口号，查询数据库中是否已经存在此目标IP和目标端口号的记录
        LambdaQueryWrapper<MonitorTcpIp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 去掉它自己这条记录
        lambdaQueryWrapper.ne(MonitorTcpIp::getId, monitorTcpIpVo.getId());
        lambdaQueryWrapper.eq(MonitorTcpIp::getIpTarget, monitorTcpIpVo.getIpTarget());
        lambdaQueryWrapper.eq(MonitorTcpIp::getPortTarget, monitorTcpIpVo.getPortTarget());
        MonitorTcpIp dbMonitorTcpIp = this.monitorTcpIpDao.selectOne(lambdaQueryWrapper);
        if (dbMonitorTcpIp != null) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        MonitorTcpIp monitorTcpIp = monitorTcpIpVo.convertTo();
        monitorTcpIp.setUpdateTime(new Date());
        int result = this.monitorTcpIpDao.updateById(monitorTcpIp);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

}
