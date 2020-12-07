package com.gitee.pifeng.common.domain;

import com.gitee.pifeng.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 返回结果
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年4月5日 下午1:27:44
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public final class Result extends AbstractSuperBean {

    /**
     * 是否成功
     */
    private boolean isSuccess;

    /**
     * 结果信息
     */
    private String msg;

}
