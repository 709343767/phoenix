package com.gitee.pifeng.common.dto;

import com.gitee.pifeng.common.abs.AbstractInstanceBean;
import com.gitee.pifeng.common.domain.Jvm;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

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
public class JvmPackage extends AbstractInstanceBean {

    /**
     * ID
     */
    private String id;

    /**
     * Java虚拟机信息
     */
    private Jvm jvm;

    /**
     * 时间
     */
    private Date dateTime;

    /**
     * 传输频率
     */
    private long rate;

}
