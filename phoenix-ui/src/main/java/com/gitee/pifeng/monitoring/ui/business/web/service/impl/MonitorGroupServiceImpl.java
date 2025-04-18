package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.common.constant.MonitorTypeEnums;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorGroupDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorGroup;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorGroupService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorGroupVo;
import com.gitee.pifeng.monitoring.ui.constant.WebResponseConstants;
import com.gitee.pifeng.monitoring.ui.util.SpringSecurityUtils;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

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
     * <p>
     * 获取监控分组列表
     * </p>
     *
     * @param current   当前页
     * @param size      每页显示条数
     * @param groupType 分组类型
     * @param groupName 分组名称
     * @param groupDesc 分组描述
     * @return 简单分页模型
     * @author 皮锋
     * @custom.date 2021/12/24 14:32
     */
    @Override
    public Page<MonitorGroupVo> getMonitorGroupList(Long current, Long size, String groupType, String groupName, String groupDesc) {
        // 查询数据库
        IPage<MonitorGroup> ipage = new Page<>(current, size);
        LambdaQueryWrapper<MonitorGroup> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(groupType)) {
            lambdaQueryWrapper.eq(MonitorGroup::getGroupType, groupType);
        }
        if (StringUtils.isNotBlank(groupName)) {
            lambdaQueryWrapper.like(MonitorGroup::getGroupName, groupName);
        }
        if (StringUtils.isNotBlank(groupDesc)) {
            lambdaQueryWrapper.like(MonitorGroup::getGroupDesc, groupDesc);
        }
        IPage<MonitorGroup> monitorGroupPage = this.baseMapper.selectPage(ipage, lambdaQueryWrapper);
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
     * 获取监控分组列表
     * </p>
     *
     * @param monitorTypeEnum 监控类型
     * @return 监控分组列表
     * @author 皮锋
     * @custom.date 2025/4/18 11:44
     */
    @Override
    public List<MonitorGroup> getMonitorGroupList(MonitorTypeEnums monitorTypeEnum) {
        LambdaQueryWrapper<MonitorGroup> monitorGroupQueryWrapper = new LambdaQueryWrapper<>();
        if (monitorTypeEnum != null) {
            monitorGroupQueryWrapper.and(wrapper -> wrapper
                    .eq(MonitorGroup::getGroupType, monitorTypeEnum.name())
                    .or()
                    .eq(MonitorGroup::getGroupType, "")
                    .or()
                    .isNull(MonitorGroup::getGroupType));
        }
        List<MonitorGroup> monitorGroups = this.baseMapper.selectList(monitorGroupQueryWrapper);
        if (CollectionUtils.isEmpty(monitorGroups)) {
            return Lists.newArrayList();
        }
        return monitorGroups;
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
        Integer count = this.baseMapper.selectCount(lambdaQueryWrapper);
        if (count != null && count > 0) {
            return LayUiAdminResultVo.ok(WebResponseConstants.EXIST);
        }
        // 转成监控分组实体对象
        MonitorGroup monitorGroup = monitorGroupVo.convertTo();
        monitorGroup.setInsertTime(new Date());
        monitorGroup.setCreateAccount(Objects.requireNonNull(SpringSecurityUtils.getCurrentMonitorUserRealm()).getUsername());
        int result = this.baseMapper.insert(monitorGroup);
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
        monitorGroup.setUpdateAccount(Objects.requireNonNull(SpringSecurityUtils.getCurrentMonitorUserRealm()).getUsername());
        int update = this.baseMapper.updateById(monitorGroup);
        if (update == 1) {
            return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.FAIL);
    }

    /**
     * <p>
     * 删除分组信息
     * </p>
     *
     * @param ids 主键ID集合
     * @return layUiAdmin响应对象：如果删除成功，LayUiAdminResultVo.data="success"，否则LayUiAdminResultVo.data="fail"。
     * @author 皮锋
     * @custom.date 2021/12/27 12:28
     */
    @Override
    public LayUiAdminResultVo deleteMonitorGroup(List<Long> ids) {
        LambdaUpdateWrapper<MonitorGroup> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.in(MonitorGroup::getId, ids);
        try {
            this.baseMapper.delete(lambdaUpdateWrapper);
        } catch (DataIntegrityViolationException e) {
            // 违反数据完整性约束，因为数据被引用
            return LayUiAdminResultVo.ok(WebResponseConstants.DATA_INTEGRITY_VIOLATION);
        }
        return LayUiAdminResultVo.ok(WebResponseConstants.SUCCESS);
    }

}
