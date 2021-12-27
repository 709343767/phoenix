package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorEnvDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorEnv;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorEnvService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorEnvVo;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.util.SpringSecurityUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 监控环境服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-12-23
 */
@Service
public class MonitorEnvServiceImpl extends ServiceImpl<IMonitorEnvDao, MonitorEnv> implements IMonitorEnvService {

    /**
     * 监控环境表数据访问对象
     */
    @Autowired
    private IMonitorEnvDao monitorEnvDao;

    /**
     * <p>
     * 获取监控环境列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param envName 环境名称
     * @param envDesc 环境描述
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2021/12/24 9:41
     */
    @Override
    public Page<MonitorEnvVo> getMonitorEnvList(Long current, Long size, String envName, String envDesc) {
        // 查询数据库
        IPage<MonitorEnv> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorEnv> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(envName)) {
            lambdaQueryWrapper.like(MonitorEnv::getEnvName, envName);
        }
        if (StringUtils.isNotBlank(envDesc)) {
            lambdaQueryWrapper.like(MonitorEnv::getEnvDesc, envDesc);
        }
        IPage<MonitorEnv> monitorEnvPage = this.monitorEnvDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorEnv> monitorEnvs = monitorEnvPage.getRecords();
        // 转换成监控环境信息表现层对象
        List<MonitorEnvVo> monitorEnvVos = Lists.newLinkedList();
        for (MonitorEnv monitorEnv : monitorEnvs) {
            MonitorEnvVo monitorEnvVo = MonitorEnvVo.builder().build().convertFor(monitorEnv);
            monitorEnvVos.add(monitorEnvVo);
        }
        // 设置返回对象
        Page<MonitorEnvVo> monitorEnvVoPage = new Page<>();
        monitorEnvVoPage.setRecords(monitorEnvVos);
        monitorEnvVoPage.setTotal(monitorEnvPage.getTotal());
        return monitorEnvVoPage;
    }

    /**
     * <p>
     * 添加环境信息
     * </p>
     *
     * @param monitorEnvVo 监控环境信息表现层对象
     * @return layUiAdmin响应对象：如果已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/24 10:31
     */
    @Override
    public LayUiAdminResultVo saveMonitorEnv(MonitorEnvVo monitorEnvVo) {
        // 判断环境是否已经存在
        LambdaQueryWrapper<MonitorEnv> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorEnv::getEnvName, monitorEnvVo.getEnvName());
        Integer count = this.monitorEnvDao.selectCount(lambdaQueryWrapper);
        if (count != null && count > 0) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        // 转成监控环境实体对象
        MonitorEnv monitorEnv = monitorEnvVo.convertTo();
        monitorEnv.setInsertTime(new Date());
        monitorEnv.setCreateAccount(SpringSecurityUtils.getCurrentMonitorUserRealm().getUsername());
        int result = this.monitorEnvDao.insert(monitorEnv);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

    /**
     * <p>
     * 编辑环境信息
     * </p>
     *
     * @param monitorEnvVo 监控环境信息表现层对象
     * @return layUiAdmin响应对象：如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/24 11:11
     */
    @Override
    public LayUiAdminResultVo editMonitorEnv(MonitorEnvVo monitorEnvVo) {
        MonitorEnv monitorEnv = monitorEnvVo.convertTo();
        monitorEnv.setUpdateTime(new Date());
        monitorEnv.setUpdateAccount(SpringSecurityUtils.getCurrentMonitorUserRealm().getUsername());
        int update = this.monitorEnvDao.updateById(monitorEnv);
        if (update == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

    /**
     * <p>
     * 删除环境信息
     * </p>
     *
     * @param monitorEnvVos 监控环境信息表现层对象
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/27 10:11
     */
    @Override
    public LayUiAdminResultVo deleteMonitorEnv(List<MonitorEnvVo> monitorEnvVos) {
        List<Long> ids = Lists.newArrayList();
        for (MonitorEnvVo monitorEnvVo : monitorEnvVos) {
            ids.add(monitorEnvVo.getId());
        }
        LambdaUpdateWrapper<MonitorEnv> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.in(MonitorEnv::getId, ids);
        try {
            this.monitorEnvDao.delete(lambdaUpdateWrapper);
        } catch (DataIntegrityViolationException e) {
            // 违反数据完整性约束，因为数据被引用
            return LayUiAdminResultVo.ok(WebResponseConstants.DATA_INTEGRITY_VIOLATION);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }
}
