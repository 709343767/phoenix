package com.gitee.pifeng.monitoring.ui.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.monitoring.ui.business.web.entity.MonitorServerNetcard;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务器网卡数据访问对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
public interface IMonitorServerNetcardDao extends BaseMapper<MonitorServerNetcard> {

    /**
     * <p>
     * 获取服务器网卡地址
     * </p>
     *
     * @param ip 服务器IP
     * @return 网卡地址列表
     * @author 皮锋
     * @custom.date 2021/1/11 9:54
     */
    List<String> getNetcardAddress(@Param("ip") String ip);

}
