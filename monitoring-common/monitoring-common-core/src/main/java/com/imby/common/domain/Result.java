package com.imby.common.domain;

import com.imby.common.abs.AbstractSuperBean;
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
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Result extends AbstractSuperBean {

    /**
     * 是否成功
     */
    private boolean isSuccess;

    /**
     * 结果信息
     */
    private String msg;

}
