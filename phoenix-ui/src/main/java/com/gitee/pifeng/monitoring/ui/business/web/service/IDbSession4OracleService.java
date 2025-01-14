package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.common.exception.NetException;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbSession4OracleVo;
import com.gitee.pifeng.monitoring.ui.business.web.vo.LayUiAdminResultVo;
import org.hyperic.sigar.SigarException;

import java.io.IOException;
import java.util.List;

/**
 * <p>
 * Oracle数据库会话服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/12/30 12:39
 */
public interface IDbSession4OracleService {

    /**
     * <p>
     * 获取会话列表
     * </p>
     *
     * @param current         当前页
     * @param size            每页显示条数
     * @param id              数据库ID
     * @param usernameParam   用户
     * @param schemaNameParam 模式
     * @param stateParam      状态
     * @param machineParam    远程主机
     * @param osUserParam     远程用户
     * @param sqlParam        SQL
     * @return 简单分页模型
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/12/30 12:46
     */
    Page<DbSession4OracleVo> getSessionList(Long current, Long size, Long id, String usernameParam, String schemaNameParam,
                                            String stateParam, String machineParam, String osUserParam, String sqlParam)
            throws NetException, SigarException, IOException;

    /**
     * <p>
     * 结束会话
     * </p>
     *
     * @param dbSession4OracleVos Oracle数据库会话
     * @param id                  数据库ID
     * @return LayUiAdmin响应对象
     * @throws NetException   自定义获取网络信息异常
     * @throws SigarException Sigar异常
     * @throws IOException    IO异常
     * @author 皮锋
     * @custom.date 2020/12/30 15:22
     */
    LayUiAdminResultVo destroySession(List<DbSession4OracleVo> dbSession4OracleVos, Long id) throws IOException, NetException, SigarException;

}
