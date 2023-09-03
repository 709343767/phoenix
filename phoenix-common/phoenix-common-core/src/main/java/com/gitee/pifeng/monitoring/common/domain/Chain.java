package com.gitee.pifeng.monitoring.common.domain;

import com.gitee.pifeng.monitoring.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.LinkedHashSet;

/**
 * <p>
 * 链路信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2023年5月21日 上午9:06:35
 */
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Chain extends AbstractSuperBean {

    /**
     * 网络链路
     */
    private LinkedHashSet<String> networkChain;

    /**
     * 应用链路
     */
    private LinkedHashSet<String> instanceChain;

    /**
     * 时间链路
     */
    private LinkedHashSet<String> timeChain;

}
