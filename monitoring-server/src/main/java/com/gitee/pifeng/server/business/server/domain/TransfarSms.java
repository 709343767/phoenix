package com.gitee.pifeng.server.business.server.domain;

import com.gitee.pifeng.common.abs.AbstractSuperBean;
import lombok.*;

/**
 * <p>
 * 创发短信实体对象
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
public class TransfarSms extends AbstractSuperBean {

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

    /**
     * <p>
     * 传入手机号码数组，返回手机号码字符串，号码之间用英文分号分隔
     * </p>
     *
     * @param phones 手机号码数组
     * @return 手机号码字符串
     * @author 皮锋
     * @custom.date 2020年3月10日 下午3:07:14
     */
    public static String getPhones(String[] phones) {
        StringBuilder buffer = new StringBuilder();
        for (String s : phones) {
            buffer.append(s).append(";");
        }
        String str = buffer.toString();
        return str.substring(0, str.length() - 1);
    }

}
