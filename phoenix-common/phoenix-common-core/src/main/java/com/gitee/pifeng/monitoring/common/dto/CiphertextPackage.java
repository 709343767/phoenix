package com.gitee.pifeng.monitoring.common.dto;

import com.gitee.pifeng.monitoring.common.abs.AbstractSuperBean;
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

    /**
     * 是否需要进行UnGzip（先压缩再加密，先解密再解压）
     */
    private boolean isUnGzipEnabled;

}
