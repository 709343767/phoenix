package com.gitee.pifeng.monitoring.common.dto;

import com.gitee.pifeng.monitoring.common.domain.Jvm;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * Java虚拟机信息包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/14 20:56
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class JvmPackage extends BaseRequestPackage {

    /**
     * Java虚拟机信息
     */
    private Jvm jvm;

    /**
     * 传输频率
     */
    private long rate;

}
