package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbSession4MysqlVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import org.hyperic.sigar.SigarException;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * MySQL数据库会话服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/24 16:49
 */
public interface IDbSession4MysqlService {

    /**
     * <p>
     * 获取会话列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param id      数据库ID
     * @return 简单分页模型
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/12/24 16:55
     */
    Page<DbSession4MysqlVo> getSessionList(Long current, Long size, Long id) throws NetException, SigarException, IOException;

    /**
     * <p>
     * 结束会话
     * </p>
     *
     * @param sessionIds MySQL数据库会话ID集合
     * @param id         数据库ID
     * @return LayUiAdmin响应对象
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/12/25 17:05
     */
    LayUiAdminResultVo destroySession(List<Long> sessionIds, Long id) throws NetException, SigarException, IOException;

}
