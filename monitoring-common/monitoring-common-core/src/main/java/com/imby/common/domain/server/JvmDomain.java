package com.imby.common.domain.server;

import com.imby.common.abs.SuperBean;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * java虚拟机信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 下午4:09:49
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class JvmDomain extends SuperBean {
    /**
     * java安路径
     */
    private String javaPath;
    /**
     * java运行时供应商
     */
    private String javaVendor;
    /**
     * java版本
     */
    private String javaVersion;
    /**
     * java运行时名称
     */
    private String javaName;
    /**
     * java虚拟机版本
     */
    private String jvmVersion;
    /**
     * java虚拟机总内存
     */
    private String jvmTotalMemory;
    /**
     * java虚拟机剩余内存
     */
    private String jvmFreeMemory;

}
