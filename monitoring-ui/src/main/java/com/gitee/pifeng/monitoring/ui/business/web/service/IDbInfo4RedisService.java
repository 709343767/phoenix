package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.gitee.pifeng.monitoring.common.exception.NetException;
import org.hyperic.sigar.SigarException;

import java.io.IOException;

/**
 * <p>
 * Redis数据库信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/10/16 20:46
 */
public interface IDbInfo4RedisService {

    /**
     * <p>
     * 获取Redis信息
     * </p>
     *
     * @param id 数据库ID
     * @return Redis信息
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2021/10/16 20:48
     */
    String getRedisInfo(Long id) throws SigarException, IOException;
}
