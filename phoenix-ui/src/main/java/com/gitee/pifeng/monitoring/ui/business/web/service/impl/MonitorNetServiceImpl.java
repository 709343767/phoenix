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
import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.BaseRequestPackage;
import com.gitee.pifeng.monitoring.common.dto.BaseResponsePackage;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.plug.core.Sender;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorNetDao;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorNetHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorNet;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorNetHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorNetService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.HomeNetVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorNetVo;
import com.gitee.pifeng.monitoring.ui.constant.UrlConstants;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.core.PackageConstructor;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hyperic.sigar.SigarException;
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
 * 网络信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/1 15:09
 */
@Service
public class MonitorNetServiceImpl extends ServiceImpl<IMonitorNetDao, MonitorNet> implements IMonitorNetService {

    /**
     * 网络信息数据访问对象
     */
    @Autowired
    private IMonitorNetDao monitorNetDao;

    /**
     * 网络信息历史记录数据访问对象
     */
    @Autowired
    private IMonitorNetHistoryDao monitorNetHistoryDao;

    /**
     * <p>
     * 获取home页的网络信息
     * </p>
     *
     * @return home页的网络信息表现层对象
     * @author 皮锋
     * @custom.date 2020/9/1 15:20
     */
    @Override
    public HomeNetVo getHomeNetInfo() {
        // 网络正常率统计
        Map<String, Object> map = this.monitorNetDao.getNetNormalRateStatistics();
        return HomeNetVo.builder()
                .netSum(NumberUtil.parseInt(map.get("netSum").toString()))
                .netConnectSum(NumberUtil.parseInt(map.get("netConnectSum").toString()))
                .netDisconnectSum(NumberUtil.parseInt(map.get("netDisconnectSum").toString()))
                .netUnsentSum(NumberUtil.parseInt(map.get("netUnsentSum").toString()))
                .netConnectRate(NumberUtil.round(map.get("netConnectRate").toString(), 2).toString())
                .build();
    }

    /**
     * <p>
     * 获取网络列表
     * </p>
     *
     * @param current      当前页
     * @param size         每页显示条数
     * @param ipSource     IP地址（来源）
     * @param ipTarget     IP地址（目的地）
     * @param status       状态（0：网络不通，1：网络正常）
     * @param monitorEnv   监控环境
     * @param monitorGroup 监控分组
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2020/9/26 13:28
     */
    @Override
    public Page<MonitorNetVo> getMonitorNetList(Long current, Long size, String ipSource, String ipTarget, String status, String monitorEnv, String monitorGroup) {
        // 查询数据库
        IPage<MonitorNet> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorNet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(ipSource)) {
            lambdaQueryWrapper.like(MonitorNet::getIpSource, ipSource);
        }
        if (StringUtils.isNotBlank(ipTarget)) {
            lambdaQueryWrapper.like(MonitorNet::getIpTarget, ipTarget);
        }
        if (StringUtils.isNotBlank(status)) {
            // -1 用来表示状态未知
            if (StringUtils.equals(status, ZeroOrOneConstants.MINUS_ONE)) {
                // 状态为 null 或 空字符串
                lambdaQueryWrapper.and(wrapper -> wrapper.isNull(MonitorNet::getStatus).or().eq(MonitorNet::getStatus, ""));
            } else {
                lambdaQueryWrapper.eq(MonitorNet::getStatus, status);
            }
        }
        if (StringUtils.isNotBlank(monitorEnv)) {
            lambdaQueryWrapper.eq(MonitorNet::getMonitorEnv, monitorEnv);
        }
        if (StringUtils.isNotBlank(monitorGroup)) {
            lambdaQueryWrapper.eq(MonitorNet::getMonitorGroup, monitorGroup);
        }
        IPage<MonitorNet> monitorNetPage = this.monitorNetDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorNet> monitorNets = monitorNetPage.getRecords();
        // 转换成网络信息表现层对象
        List<MonitorNetVo> monitorNetVos = Lists.newLinkedList();
        for (MonitorNet monitorNet : monitorNets) {
            MonitorNetVo monitorNetVo = MonitorNetVo.builder().build().convertFor(monitorNet);
            monitorNetVos.add(monitorNetVo);
        }
        // 设置返回对象
        Page<MonitorNetVo> monitorNetVoPage = new Page<>();
        monitorNetVoPage.setRecords(monitorNetVos);
        monitorNetVoPage.setTotal(monitorNetPage.getTotal());
        return monitorNetVoPage;
    }

    /**
     * <p>
     * 删除网络
     * </p>
     *
     * @param monitorNetVos 网络信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/26 14:02
     */
    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    @Override
    @Retryable
    public LayUiAdminResultVo deleteMonitorNet(List<MonitorNetVo> monitorNetVos) {
        List<Long> ids = Lists.newArrayList();
        for (MonitorNetVo monitorNetVo : monitorNetVos) {
            ids.add(monitorNetVo.getId());
        }
        // 删除历史记录
        LambdaUpdateWrapper<MonitorNetHistory> monitorNetHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorNetHistoryLambdaUpdateWrapper.in(MonitorNetHistory::getNetId, ids);
        this.monitorNetHistoryDao.delete(monitorNetHistoryLambdaUpdateWrapper);
        // 删除网络信息
        LambdaUpdateWrapper<MonitorNet> monitorNetLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorNetLambdaUpdateWrapper.in(MonitorNet::getId, ids);
        this.monitorNetDao.delete(monitorNetLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 编辑网络信息
     * </p>
     *
     * @param monitorNetVo 网络信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/11/20 13:58
     */
    @Override
    public LayUiAdminResultVo editMonitorNetwork(MonitorNetVo monitorNetVo) {
        // 根据目标IP，查询数据库中是否已经存在此目标IP的记录
        LambdaQueryWrapper<MonitorNet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 去掉它自己这条记录
        lambdaQueryWrapper.ne(MonitorNet::getId, monitorNetVo.getId());
        lambdaQueryWrapper.eq(MonitorNet::getIpTarget, monitorNetVo.getIpTarget());
        MonitorNet dbMonitorNet = this.monitorNetDao.selectOne(lambdaQueryWrapper);
        if (dbMonitorNet != null) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        MonitorNet monitorNet = monitorNetVo.convertTo();
        monitorNet.setIpSource(this.getSourceIp());
        monitorNet.setUpdateTime(new Date());
        if (StringUtils.isBlank(monitorNetVo.getMonitorEnv())) {
            monitorNet.setMonitorEnv(null);
        }
        if (StringUtils.isBlank(monitorNetVo.getMonitorGroup())) {
            monitorNet.setMonitorGroup(null);
        }
        int result = this.monitorNetDao.updateById(monitorNet);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

    /**
     * <p>
     * 添加网络信息
     * </p>
     *
     * @param monitorNetVo 网络信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @throws NetException 自定义获取网络信息异常
     * @author 皮锋
     * @custom.date 2020/11/20 15:30
     */
    @Override
    public LayUiAdminResultVo addMonitorNetwork(MonitorNetVo monitorNetVo) throws NetException {
        // 根据目标IP，查询数据库中是否已经存在此目标IP的记录
        LambdaQueryWrapper<MonitorNet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorNet::getIpTarget, monitorNetVo.getIpTarget());
        MonitorNet dbMonitorNet = this.monitorNetDao.selectOne(lambdaQueryWrapper);
        if (dbMonitorNet != null) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        MonitorNet monitorNet = monitorNetVo.convertTo();
        monitorNet.setIpSource(this.getSourceIp());
        monitorNet.setInsertTime(new Date());
        monitorNet.setOfflineCount(0);
        if (StringUtils.isBlank(monitorNetVo.getMonitorEnv())) {
            monitorNet.setMonitorEnv(null);
        }
        if (StringUtils.isBlank(monitorNetVo.getMonitorGroup())) {
            monitorNet.setMonitorGroup(null);
        }
        int result = this.monitorNetDao.insert(monitorNet);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

    /**
     * <p>
     * 获取被监控网络源IP地址，获取失败则返回null。
     * </p>
     *
     * @return 被监控网络源IP地址
     * @author 皮锋
     * @custom.date 2021/10/6 22:19
     */
    @Override
    public String getSourceIp() {
        try {
            BaseRequestPackage baseRequestPackage = new PackageConstructor().structureBaseRequestPackage(null);
            String resultStr = Sender.send(UrlConstants.GET_SOURCE_IP, baseRequestPackage.toJsonString());
            BaseResponsePackage baseResponsePackage = JSON.parseObject(resultStr, BaseResponsePackage.class);
            Result result = baseResponsePackage.getResult();
            // 是否成功
            boolean isSuccess = result.isSuccess();
            if (isSuccess) {
                return result.getMsg();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * <p>
     * 测试网络连通性
     * </p>
     *
     * @param monitorNetVo 网络信息
     * @return layUiAdmin响应对象：网络连通性
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2022/10/9 10:16
     */
    @Override
    public LayUiAdminResultVo testMonitorNetwork(MonitorNetVo monitorNetVo) throws SigarException, IOException {
        // 封装请求数据
        JSONObject extraMsg = new JSONObject();
        extraMsg.put("ipTarget", monitorNetVo.getIpTarget());
        BaseRequestPackage baseRequestPackage = new PackageConstructor().structureBaseRequestPackage(extraMsg);
        // 从服务端获取数据
        String resultStr = Sender.send(UrlConstants.TEST_MONITOR_NETWORK_URL, baseRequestPackage.toJsonString());
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
