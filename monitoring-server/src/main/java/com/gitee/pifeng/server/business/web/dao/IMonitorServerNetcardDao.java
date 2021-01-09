package com.gitee.pifeng.server.business.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gitee.pifeng.server.business.web.entity.MonitorServerNetcard;
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
     * 获取服务器详情页面服务器网卡信息
     * </p>
     *
     * @param ip 服务器IP地址
     * @return 服务器网卡
     * @author 皮锋
     * @custom.date 2021/1/9 20:23
     */
    List<MonitorServerNetcard> getServerDetailPageServerNetcardInfo(@Param("ip") String ip);

}
