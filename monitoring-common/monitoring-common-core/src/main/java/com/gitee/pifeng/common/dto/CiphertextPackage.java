package com.gitee.pifeng.common.dto;

import com.gitee.pifeng.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 密文数据包
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/29 12:42
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class CiphertextPackage extends AbstractSuperBean {

    /**
     * 加密后的数据
     */
    private String ciphertext;

}
