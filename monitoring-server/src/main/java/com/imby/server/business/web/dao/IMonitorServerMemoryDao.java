package com.imby.server.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imby.server.business.web.entity.MonitorServerMemory;
import com.imby.server.business.web.vo.ServerDetailPageServerMemoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务器内存数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
public interface IMonitorServerMemoryDao extends BaseMapper<MonitorServerMemory> {

    /**
     * <p>
     * 获取服务器详情页面服务器内存信息
     * </p>
     *
     * @param params 请求参数
     * @return 服务器详情页面服务器内存信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/21 12:45
     */
    List<ServerDetailPageServerMemoryVo> getServerDetailPageServerMemory(@Param("params") Map<String, Object> params);

}
