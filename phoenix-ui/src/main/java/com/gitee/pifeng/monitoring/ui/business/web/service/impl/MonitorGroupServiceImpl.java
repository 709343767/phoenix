package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorGroupDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorGroup;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorGroupService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorGroupVo;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.util.SpringSecurityUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 监控分组服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-12-23
 */
@Service
public class MonitorGroupServiceImpl extends ServiceImpl<IMonitorGroupDao, MonitorGroup> implements IMonitorGroupService {

    /**
     * 监控分组表数据访问对象
     */
    @Autowired
    private IMonitorGroupDao monitorGroupDao;

    /**
     * <p>
     * 获取监控分组列表
     * </p>
     *
     * @param current   当前页
     * @param size      每页显示条数
     * @param groupName 分组名称
     * @param groupDesc 分组描述
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2021/12/24 14:32
     */
    @Override
    public Page<MonitorGroupVo> getMonitorGroupList(Long current, Long size, String groupName, String groupDesc) {
        // 查询数据库
        IPage<MonitorGroup> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorGroup> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(groupName)) {
            lambdaQueryWrapper.like(MonitorGroup::getGroupName, groupName);
        }
        if (StringUtils.isNotBlank(groupDesc)) {
            lambdaQueryWrapper.like(MonitorGroup::getGroupDesc, groupDesc);
        }
        IPage<MonitorGroup> monitorGroupPage = this.monitorGroupDao.selectPage(ipage, lambdaQueryWrapper);
        List<MonitorGroup> monitorGroups = monitorGroupPage.getRecords();
        // 转换成监控分组信息表现层对象
        List<MonitorGroupVo> monitorGroupVos = Lists.newLinkedList();
        for (MonitorGroup monitorGroup : monitorGroups) {
            MonitorGroupVo monitorGroupVo = MonitorGroupVo.builder().build().convertFor(monitorGroup);
            monitorGroupVos.add(monitorGroupVo);
        }
        // 设置返回对象
        Page<MonitorGroupVo> monitorGroupVoPage = new Page<>();
        monitorGroupVoPage.setRecords(monitorGroupVos);
        monitorGroupVoPage.setTotal(monitorGroupPage.getTotal());
        return monitorGroupVoPage;
    }

    /**
     * <p>
     * 添加分组信息
     * </p>
     *
     * @param monitorGroupVo 监控分组信息表现层对象
     * @return layUiAdmin响应对象：如果已经存在，LayUiAdminResultVo.data="exist"；
     * 如果添加成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/24 14:33
     */
    @Override
    public LayUiAdminResultVo saveMonitorGroup(MonitorGroupVo monitorGroupVo) {
        // 判断分组是否已经存在
        LambdaQueryWrapper<MonitorGroup> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(MonitorGroup::getGroupName, monitorGroupVo.getGroupName());
        Integer count = this.monitorGroupDao.selectCount(lambdaQueryWrapper);
        if (count != null && count > 0) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        // 转成监控分组实体对象
        MonitorGroup monitorGroup = monitorGroupVo.convertTo();
        monitorGroup.setInsertTime(new Date());
        monitorGroup.setCreateAccount(SpringSecurityUtils.getCurrentMonitorUserRealm().getUsername());
        int result = this.monitorGroupDao.insert(monitorGroup);
        if (result == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

    /**
     * <p>
     * 编辑分组信息
     * </p>
     *
     * @param monitorGroupVo 监控分组信息表现层对象
     * @return layUiAdmin响应对象：如果编辑成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/24 14:34
     */
    @Override
    public LayUiAdminResultVo editMonitorGroup(MonitorGroupVo monitorGroupVo) {
        MonitorGroup monitorGroup = monitorGroupVo.convertTo();
        monitorGroup.setUpdateTime(new Date());
        monitorGroup.setUpdateAccount(SpringSecurityUtils.getCurrentMonitorUserRealm().getUsername());
        int update = this.monitorGroupDao.updateById(monitorGroup);
        if (update == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }
}
