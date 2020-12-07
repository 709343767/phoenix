package com.gitee.pifeng.server.business.server.domain;

import com.gitee.pifeng.common.abs.AbstractSuperBean;
import lombok.*;

import java.util.Map;

/**
 * <p>
 * 邮件实体对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/4/13 13:08
 */
@Data
@Builder
@ToString
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Mail extends AbstractSuperBean {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 接收人邮件地址
     */
    private String[] email;

    /**
     * 告警级别
     */
    private String level;

    /**
     * 附加，例如：文件的绝对地址、动态模板数据等
     */
    private Map<String, Object> attachment;

}
