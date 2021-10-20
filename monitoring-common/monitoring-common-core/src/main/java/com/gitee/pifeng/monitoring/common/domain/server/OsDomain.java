package com.gitee.pifeng.monitoring.common.domain.server;

import com.gitee.pifeng.monitoring.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 操作系统信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 下午2:08:48
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class OsDomain extends AbstractSuperBean {

    /**
     * 计算机名
     */
    private String computerName;

    /**
     * 操作系统名称
     */
    private String osName;

    /**
     * 操作系统架构
     */
    private String osArch;

    /**
     * 操作系统版本
     */
    private String osVersion;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户主目录
     */
    private String userHome;
    /**
     * 操作系统时区
     */
    private String osTimeZone;

}
