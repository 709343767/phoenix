package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorHttpDao;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorHttpHistoryDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorHttp;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorHttpHistory;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorHttpService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorHttpVo;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * HTTP信息服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022-04-11
 */
@Service
public class MonitorHttpServiceImpl extends ServiceImpl<IMonitorHttpDao, MonitorHttp> implements IMonitorHttpService {

    /**
     * HTTP信息历史记录数据访问对象
     */
    @Autowired
    private IMonitorHttpHistoryDao monitorHttpHistoryDao;

    /**
     * <p>
     * 获取HTTP列表
     * </p>
     *
     * @param current        当前页
     * @param size           每页显示条数
     * @param hostnameSource 主机名（来源）
     * @param urlTarget      URL地址（目的地）
     * @param method         请求方法
     * @param status         状态
     * @return layUiAdmin响应对象
     * @author 皮锋
     * @custom.date 2022/4/11 10:51
     */
    @Override
    public Page<MonitorHttpVo> getMonitorHttpList(Long current, Long size, String hostnameSource, String urlTarget, String method, Integer status) {
        // 查询数据库
        IPage<MonitorHttp> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorHttp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(hostnameSource)) {
            lambdaQueryWrapper.like(MonitorHttp::getHostnameSource, hostnameSource);
        }
        if (StringUtils.isNotBlank(urlTarget)) {
            lambdaQueryWrapper.like(MonitorHttp::getUrlTarget, urlTarget);
        }
        if (StringUtils.isNotBlank(method)) {
            lambdaQueryWrapper.eq(MonitorHttp::getMethod, method);
        }
        if (null != status) {
            // -1 用来表示状态未知
            if (-1 == status) {
                // 状态为 null 或 空字符串
                lambdaQueryWrapper.and(wrapper -> wrapper.isNull(MonitorHttp::getStatus));
            }
            // -2 表示其它
            else if (-2 == status) {
                lambdaQueryWrapper.and(wrapper -> wrapper
                        .ne(MonitorHttp::getStatus, 200)
                        .ne(MonitorHttp::getStatus, 500)
                        .ne(MonitorHttp::getStatus, 404)
                        .ne(MonitorHttp::getStatus, 403)
                        .ne(MonitorHttp::getStatus, 400)
                        .isNotNull(MonitorHttp::getStatus));
            } else {
                lambdaQueryWrapper.eq(MonitorHttp::getStatus, status);
            }
        }
        IPage<MonitorHttp> monitorHttpPage = this.baseMapper.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorHttp> monitorHttps = monitorHttpPage.getRecords();
        // 转换成HTTP信息表现层对象
        List<MonitorHttpVo> monitorHttpVos = Lists.newLinkedList();
        for (MonitorHttp monitorHttp : monitorHttps) {
            MonitorHttpVo monitorHttpVo = MonitorHttpVo.builder().build().convertFor(monitorHttp);
            monitorHttpVos.add(monitorHttpVo);
        }
        // 设置返回对象
        Page<MonitorHttpVo> monitorHttpVoPage = new Page<>();
        monitorHttpVoPage.setRecords(monitorHttpVos);
        monitorHttpVoPage.setTotal(monitorHttpPage.getTotal());
        return monitorHttpVoPage;
    }

    /**
     * <p>
     * 删除HTTP
     * </p>
     *
     * @param monitorHttpVos HTTP信息
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 9:44
     */
    @Transactional(rollbackFor = Throwable.class, isolation = Isolation.READ_COMMITTED)
    @Override
    @Retryable
    public LayUiAdminResultVo deleteMonitorHttp(List<MonitorHttpVo> monitorHttpVos) {
        List<Long> ids = Lists.newArrayList();
        for (MonitorHttpVo monitorHttpVo : monitorHttpVos) {
            ids.add(monitorHttpVo.getId());
        }
        // 删除HTTP历史记录表
        LambdaUpdateWrapper<MonitorHttpHistory> monitorHttpHistoryLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorHttpHistoryLambdaUpdateWrapper.in(MonitorHttpHistory::getHttpId, ids);
        this.monitorHttpHistoryDao.delete(monitorHttpHistoryLambdaUpdateWrapper);
        // 删除HTTP信息表
        LambdaUpdateWrapper<MonitorHttp> monitorHttpLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        monitorHttpLambdaUpdateWrapper.in(MonitorHttp::getId, ids);
        this.baseMapper.delete(monitorHttpLambdaUpdateWrapper);
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

    /**
     * <p>
     * 添加HTTP信息
     * </p>
     *
     * @param monitorHttpVo HTTP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 10:16
     */
    @Override
    public LayUiAdminResultVo addMonitorHttp(MonitorHttpVo monitorHttpVo) {
        // 根据目标url，查询数据库中是否已经存在此目标url的记录
        LambdaQueryWrapper<MonitorHttp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorHttp::getUrlTarget, monitorHttpVo.getUrlTarget());
        MonitorHttp dbMonitorHttp = this.baseMapper.selectOne(lambdaQueryWrapper);
        if (dbMonitorHttp != null) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        MonitorHttp monitorHttp = monitorHttpVo.convertTo();
        monitorHttp.setInsertTime(new Date());
        monitorHttp.setOfflineCount(0);
        int result = this.baseMapper.insert(monitorHttp);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

    /**
     * <p>
     * 编辑HTTP信息
     * </p>
     *
     * @param monitorHttpVo HTTP信息
     * @return layUiAdmin响应对象：如果数据库中已经存在，LayUiAdminResultVo.data="exist"；
     * 如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2022/1/11 12:26
     */
    @Override
    public LayUiAdminResultVo editMonitorHttp(MonitorHttpVo monitorHttpVo) {
        // 根据目标目标url，查询数据库中是否已经存在此目标url的记录
        LambdaQueryWrapper<MonitorHttp> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 去掉它自己这条记录
        lambdaQueryWrapper.ne(MonitorHttp::getId, monitorHttpVo.getId());
        lambdaQueryWrapper.eq(MonitorHttp::getUrlTarget, monitorHttpVo.getUrlTarget());
        MonitorHttp dbMonitorHttp = this.baseMapper.selectOne(lambdaQueryWrapper);
        if (dbMonitorHttp != null) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        MonitorHttp monitorHttp = monitorHttpVo.convertTo();
        monitorHttp.setUpdateTime(new Date());
        int result = this.baseMapper.updateById(monitorHttp);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }
}
