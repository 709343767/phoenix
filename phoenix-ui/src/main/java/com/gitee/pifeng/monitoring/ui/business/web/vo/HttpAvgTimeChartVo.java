package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * HTTP访问耗时图表信息表现层对象
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/3/17 21:42
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "HTTP访问耗时图表信息表现层对象")
public class HttpAvgTimeChartVo implements ISuperBean {

    @Schema(description = "所有列表")
    private List<All> allList;

    @Schema(description = "异常列表")
    private List<Exc> excList;

    /**
     * <p>
     * 所有
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/3/19 15:45
     */
    @Schema(description = "所有")
    @Data
    public static class All {

        @JsonSerialize(using = ToStringSerializer.class)
        @Schema(description = "平均响应时间（毫秒）")
        private Long avgTime;

        @Schema(description = "状态码")
        private Integer status;

        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
        @Schema(description = "新增时间")
        private Date insertTime;
    }

    /**
     * <p>
     * 异常
     * </p>
     *
     * @author 皮锋
     * @custom.date 2022/3/19 15:46
     */
    @Schema(description = "异常")
    @Data
    public static class Exc {

        @JsonSerialize(using = ToStringSerializer.class)
        @Schema(description = "平均响应时间（毫秒）")
        private Long avgTime;

        @Schema(description = "状态码")
        private Integer status;

        @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
        @Schema(description = "新增时间")
        private Date insertTime;
    }

}
