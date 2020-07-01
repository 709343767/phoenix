package com.imby.common.dto;

import com.imby.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
public class CiphertextPackage extends AbstractSuperBean implements Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 4928013929736210856L;

    /**
     * 加密后的数据
     */
    private String ciphertext;

}
