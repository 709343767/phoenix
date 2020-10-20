package com.imby.server.business.server.domain;

import com.imby.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * <p>
 * 服务器磁盘
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/3/30 11:54
 */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class Disk extends AbstractSuperBean {


    /**
     * IP地址
     */
    private String ip;

    /**
     * 计算机名
     */
    private String computerName;

    /**
     * 服务器磁盘分区
     */
    private Map<String, Subregion> subregionMap;

    /**
     * <p>
     * 服务器磁盘分区
     * </p>
     *
     * @author 皮锋
     * @custom.date 2020/3/30 12:13
     */
    @Getter
    @Setter
    @ToString
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class Subregion extends AbstractSuperBean {

        /**
         * 是否已经发送分区盘符过载告警信息
         */
        private boolean isAlarm;

        /**
         * 分区的盘符资源是否过载
         */
        private boolean isOverLoad;

        /**
         * 分区的盘符资源利用率
         */
        private double usePercent;

        /**
         * 分区的盘符名称
         */
        private String devName;
    }

}
