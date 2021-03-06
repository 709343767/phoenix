package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * ???????????????????????????
 * </p>
 *
 * @author ??????
 * @custom.date 2020/9/1 15:09
 */
@Service
public class MonitorNetServiceImpl extends ServiceImpl<IMonitorNetDao, MonitorNet> implements IMonitorNetService {

    /**
     * ??????????????????????????????
     */
    @Autowired
    private IMonitorNetDao monitorNetDao;

    /**
     * ??????????????????????????????????????????
     */
    @Autowired
    private IMonitorNetHistoryDao monitorNetHistoryDao;

    /**
     * <p>
     * ??????home??????????????????
     * </p>
     *
     * @return home?????????????????????????????????
     * @author ??????
     * @custom.date 2020/9/1 15:20
     */
    @Override
    public HomeNetVo getHomeNetInfo() {
        // ?????????????????????
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
     * ??????????????????
     * </p>
     *
     * @param current      ?????????
     * @param size         ??????????????????
     * @param ipSource     IP??????????????????
     * @param ipTarget     IP?????????????????????
     * @param status       ?????????0??????????????????1??????????????????
     * @param monitorEnv   ????????????
     * @param monitorGroup ????????????
     * @return ??????????????????
     * @author ??????
     * @custom.date 2020/9/26 13:28
     */
    @Override
    public Page<MonitorNetVo> getMonitorNetList(Long current, Long size, String ipSource, String ipTarget, String status, String monitorEnv, String monitorGroup) {
        // ???????????????
        IPage<MonitorNet> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorNet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(ipSource)) {
            lambdaQueryWrapper.like(MonitorNet::getIpSource, ipSource);
        }
        if (StringUtils.isNotBlank(ipTarget)) {
            lambdaQueryWrapper.like(MonitorNet::getIpTarget, ipTarget);
        }
        if (StringUtils.isNotBlank(status)) {
            // -1 ????????????????????????
            if (StringUtils.equals(status, ZeroOrOneConstants.MINUS_ONE)) {
                // ????????? null ??? ????????????
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
        // ????????????????????????????????????
        List<MonitorNetVo> monitorNetVos = Lists.newLinkedList();
        for (MonitorNet monitorNet : monitorNets) {
            MonitorNetVo monitorNetVo = MonitorNetVo.builder().build().convertFor(monitorNet);
            monitorNetVos.add(monitorNetVo);
        }
        // ??????????????????
        Page<MonitorNetVo> monitorNetVoPage = new Page<>();
        monitorNetVoPage.setRecords(monitorNetVos);
        monitorNetVoPage.setTotal(monitorNetPage.getTotal());
        return monitorNetVoPage;
    }

    /**
     * <p>
     * ????????????
     * </p>
     *
     * @param monitorNetVos ????????????
     * @return layUiAdmin????????????????????????????????????LayUiAdminResultVo.data="success"?????????LayUiAdminResultVo.data="fail"???
     * @author ??????
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
        // ??????????????????
        LambdaUpdateWrapper<MonitorNetHistory> monitorNetHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorNetHistoryLambdaUpdateWrapper.in(MonitorNetHistory::getNetId, ids);
        this.monitorNetHistoryDao.delete(monitorNetHistoryLambdaUpdateWrapper);
        // ??????????????????
        LambdaUpdateWrapper<MonitorNet> monitorNetLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorNetLambdaUpdateWrapper.in(MonitorNet::getId, ids);
        this.monitorNetDao.delete(monitorNetLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * ??????????????????
     * </p>
     *
     * @param monitorNetVo ????????????
     * @return layUiAdmin????????????????????????????????????????????????LayUiAdminResultVo.data="exist"???
     * ?????????????????????LayUiAdminResultVo.data="success"?????????LayUiAdminResultVo.data="fail"???
     * @author ??????
     * @custom.date 2020/11/20 13:58
     */
    @Override
    public LayUiAdminResultVo editMonitorNetwork(MonitorNetVo monitorNetVo) {
        // ????????????IP????????????????????????????????????????????????IP?????????
        LambdaQueryWrapper<MonitorNet> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // ???????????????????????????
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
     * ??????????????????
     * </p>
     *
     * @param monitorNetVo ????????????
     * @return layUiAdmin????????????????????????????????????????????????LayUiAdminResultVo.data="exist"???
     * ?????????????????????LayUiAdminResultVo.data="success"?????????LayUiAdminResultVo.data="fail"???
     * @throws NetException ?????????????????????????????????
     * @author ??????
     * @custom.date 2020/11/20 15:30
     */
    @Override
    public LayUiAdminResultVo addMonitorNetwork(MonitorNetVo monitorNetVo) throws NetException {
        // ????????????IP????????????????????????????????????????????????IP?????????
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
     * ????????????????????????IP??????????????????????????????null???
     * </p>
     *
     * @return ??????????????????IP??????
     * @author ??????
     * @custom.date 2021/10/6 22:19
     */
    @Override
    public String getSourceIp() {
        try {
            BaseRequestPackage baseRequestPackage = new PackageConstructor().structureBaseRequestPackage(null);
            String resultStr = Sender.send(UrlConstants.GET_SOURCE_IP, baseRequestPackage.toJsonString());
            BaseResponsePackage baseResponsePackage = JSON.parseObject(resultStr, BaseResponsePackage.class);
            Result result = baseResponsePackage.getResult();
            // ????????????
            boolean isSuccess = result.isSuccess();
            if (isSuccess) {
                return result.getMsg();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
