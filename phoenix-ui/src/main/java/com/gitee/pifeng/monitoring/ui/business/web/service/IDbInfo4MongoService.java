package com.gitee.pifeng.monitoring.ui.business.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gitee.pifeng.monitoring.ui.business.web.vo.DbInfo4MongoVo;
import org.hyperic.sigar.SigarException;

import java.io.IOException;

/**
 * <p>
 * Mongo数据库信息服务类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/20 14:23
 */
public interface IDbInfo4MongoService {

    /**
     * <p>
     * 获取Mongo信息列表
     * </p>
     *
     * @param current 当前页
     * @param size    每页显示条数
     * @param id      数据库ID
     * @return 简单分页模型
     * @throws IOException    IO异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2022/1/20 14:33
     */
    Page<DbInfo4MongoVo> getMongoInfoList(Long current, Long size, Long id) throws IOException, SigarException;
}
