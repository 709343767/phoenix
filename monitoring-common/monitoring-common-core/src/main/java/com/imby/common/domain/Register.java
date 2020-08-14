package com.imby.common.domain;

import com.imby.common.abs.AbstractSuperBean;
import com.imby.common.domain.server.OsDomain;
import com.imby.common.inf.INeed2Register;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 注册信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/14 17:07
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class Register extends AbstractSuperBean implements INeed2Register {

    /**
     * 操作系统信息
     */
    private OsDomain osDomain;


}
