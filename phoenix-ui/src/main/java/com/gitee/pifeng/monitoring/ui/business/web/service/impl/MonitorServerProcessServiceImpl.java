package com.gitee.pifeng.monitoring.ui.business.web.service.impl;

import cn.hutool.core.io.unit.DataSizeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gitee.pifeng.monitoring.ui.business.web.dao.IMonitorServerProcessDao;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerProcess;
import com.gitee.pifeng.monitoring.ui.business.web.service.IMonitorServerProcessService;
import com.gitee.pifeng.monitoring.ui.business.web.vo.MonitorServerProcessVo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务器进程服务实现类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021-09-15
 */
@Service
public class MonitorServerProcessServiceImpl extends ServiceImpl<IMonitorServerProcessDao, MonitorServerProcess> implements IMonitorServerProcessService {

    /**
     * 服务器进程信息数据访问对象
     */
    @Autowired
    private IMonitorServerProcessDao monitorServerProcessDao;

    /**
     * <p>
     * 获取服务器详情页面服务器进程信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器进程表现层对象
     * @author 皮锋
     * @custom.date 2021/9/18 15:56
     */
    @Override
    public List<MonitorServerProcessVo> getServerDetailPageServerProcessInfo(String ip) {
        LambdaQueryWrapper<MonitorServerProcess> monitorServerProcessLambdaQueryWrapper = new LambdaQueryWrapper<>();
        monitorServerProcessLambdaQueryWrapper.eq(MonitorServerProcess::getIp, ip);
        List<MonitorServerProcess> monitorServerProcesses = this.monitorServerProcessDao.selectList(monitorServerProcessLambdaQueryWrapper);
        // 设置返回值
        List<MonitorServerProcessVo> monitorServerProcessVos = Lists.newArrayList();
        for (MonitorServerProcess monitorServerProcess : monitorServerProcesses) {
            MonitorServerProcessVo monitorServerProcessVo = MonitorServerProcessVo.builder().build().convertFor(monitorServerProcess);
            // 占用内存大小（智能转换单位后的大小）
            monitorServerProcessVo.setMemorySizeStr(DataSizeUtil.format(monitorServerProcessVo.getMemorySize()));
            monitorServerProcessVo.setPorts(StringUtils.replace(monitorServerProcessVo.getPorts(), ",", "、"));
            monitorServerProcessVos.add(monitorServerProcessVo);
        }
        return monitorServerProcessVos;
    }

}
