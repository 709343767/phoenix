package com.imby.server.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.imby.server.business.web.entity.MonitorJvmMemory;
import com.imby.server.business.web.vo.InstanceDetailPageJvmMemoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * java虚拟机内存信息数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
public interface IMonitorJvmMemoryDao extends BaseMapper<MonitorJvmMemory> {

    /**
     * <p>
     * 获取jvm内存类型
     * </p>
     *
     * @param instanceId 应用实例ID
     * @return jvm内存类型
     * @author 皮锋
     * @custom.date 2020/10/15 10:02
     */
    List<String> getJvmMemoryTypes(@Param("instanceId") String instanceId);

    /**
     * <p>
     * 获取应用实例详情页面java虚拟机内存信息
     * </p>
     *
     * @param
     * @return 应用实例详情页面java虚拟机内存信息表现层对象
     * @author 皮锋
     * @custom.date 2020/10/16 13:48
     */
    List<InstanceDetailPageJvmMemoryVo> getInstanceDetailPageJvmMemory(@Param("params") Map<String, Object> params);
}
