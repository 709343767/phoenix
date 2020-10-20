package com.imby.server.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imby.server.business.web.entity.MonitorServerCpu;
import com.imby.server.business.web.vo.ServerDetailPageServerCpuVo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务器CPU数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
public interface IMonitorServerCpuDao extends BaseMapper<MonitorServerCpu> {

    /**
     * <p>
     * 获取服务器详情页面服务器CPU信息
     * </p>
     *
     * @param params 请求参数
     * @return 服务器详情页面服务器CPU信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/20 12:42
     */
    List<ServerDetailPageServerCpuVo> getServerDetailPageServerCpu(Map<String, Object> params);
}
