package com.gitee.pifeng.monitoring.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * <p>
 * 通信框架类型
 * </p>
 *
 * @author 皮锋
 * @custom.date 2024/3/14 15:19
 */
@Getter
@ToString
@AllArgsConstructor
public enum CommFrameworkTypeEnums {

    /**
     * Apache HttpComponents
     */
    APACHE_HTTP_COMPONENTS("apacheHttpComponents");

    /**
     * 中文名字
     */
    private final String name;

}