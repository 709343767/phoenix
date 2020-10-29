package com.imby.server.business.web.service.impl;

import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.imby.common.constant.AlarmTypeEnums;
import com.imby.server.business.web.dao.IMonitorNetDao;
import com.imby.server.business.web.entity.MonitorNet;
import com.imby.server.business.web.service.IMonitorNetService;
import com.imby.server.business.web.vo.HomeNetVo;
import com.imby.server.business.web.vo.LayUiAdminResultVo;
import com.imby.server.business.web.vo.MonitorNetVo;
import com.imby.server.constant.WebResponseConstants;
import com.imby.server.core.ThreadPool;
import com.imby.server.inf.INetMonitoringListener;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
     * 网络信息监听器
     */
    @Autowired
    private List<INetMonitoringListener> netMonitoringListeners;

    /**
     * 网络信息数据访问对象
     */
    @Autowired
    private IMonitorNetDao monitorNetDao;

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
                .netConnectRate(map.get("netConnectRate").toString())
                .build();
    }

    /**
     * <p>
     * 获取网络列表
     * </p>
     *
     * @param current  当前页
     * @param size     每页显示条数
     * @param ipSource IP地址（来源）
     * @param ipTarget IP地址（目的地）
     * @param status   状态（0：网络不通，1：网络正常）
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2020/9/26 13:28
     */
    @Override
    public Page<MonitorNetVo> getMonitorNetList(Long current, Long size, String ipSource, String ipTarget, String status) {
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
            lambdaQueryWrapper.eq(MonitorNet::getStatus, status);
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
     * @param monitorNetVos 删除网络
     * @return layUiAdmin响应对象：如果删除用户成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2020/9/26 14:02
     */
    @Transactional(rollbackFor = Throwable.class)
    @Override
    public LayUiAdminResultVo deleteMonitorNet(List<MonitorNetVo> monitorNetVos) {
        List<String> ipSources = Lists.newArrayList();
        List<String> ipTargets = Lists.newArrayList();
        for (MonitorNetVo monitorNetVo : monitorNetVos) {
            ipSources.add(monitorNetVo.getIpSource());
            ipTargets.add(monitorNetVo.getIpTarget());
        }
        LambdaUpdateWrapper<MonitorNet> monitorNetLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorNetLambdaUpdateWrapper.in(MonitorNet::getIpSource, ipSources);
        monitorNetLambdaUpdateWrapper.in(MonitorNet::getIpTarget, ipTargets);
        this.monitorNetDao.delete(monitorNetLambdaUpdateWrapper);

        // 调用监听器回调接口
        this.netMonitoringListeners.forEach(e ->
                ThreadPool.COMMON_CPU_INTENSIVE_THREAD_POOL.execute(() ->
                        e.wakeUpMonitorPool(AlarmTypeEnums.NET, ipSources)));
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

}
