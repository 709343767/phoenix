package com.gitee.pifeng.monitoring.server.business.server.domain;

import com.gitee.pifeng.monitoring.common.abs.AbstractSuperBean;
import lombok.*;

/**
 * <p>
 * kacper短信实体对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月9日 下午9:43:03
 */
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class KacperSms extends AbstractSuperBean {

    /**
     * 手机号码，多个手机号码用英文分号隔开
     */
    private String phone;

    /**
     * 短信类型
     */
    private String type;

    /**
     * 短信内容
     */
    private String content;

    /**
     * 身份
     */
    private String identity;

}
