package com.gitee.pifeng.monitoring.server.business.server.service;

import com.gitee.pifeng.monitoring.common.domain.Result;
import com.gitee.pifeng.monitoring.common.dto.JvmPackage;

import java.util.Date;

/**
 * <p>
 * java虚拟机信息服务层接口
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/27 17:41
 */
public interface IJvmService {

    /**
     * <p>
     * 处理java虚拟机信息包
     * </p>
     *
     * @param jvmPackage java虚拟机信息包
     * @return {@link Result}
     * @author 皮锋
     * @custom.date 2020/8/27 17:45
     */
    Result dealJvmPackage(JvmPackage jvmPackage);

    /**
     * <p>
     * 清理JVM历史记录
     * </p>
     *
     * @param historyTime 时间点，清理这个时间点以前的数据
     * @return 清理记录数
     * @author 皮锋
     * @custom.date 2021/12/9 20:46
     */
    int clearHistoryData(Date historyTime);
}
