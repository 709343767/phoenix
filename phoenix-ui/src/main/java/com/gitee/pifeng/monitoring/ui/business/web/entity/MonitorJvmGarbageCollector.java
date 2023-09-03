package com.gitee.pifeng.monitoring.ui.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * java虚拟机GC信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/6 19:59
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_JVM_GARBAGE_COLLECTOR")
@Schema(description = "MonitorJvmGarbageCollector对象")
public class MonitorJvmGarbageCollector implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "应用实例ID")
    @TableField("INSTANCE_ID")
    private String instanceId;

    @Schema(description = "内存管理器序号")
    @TableField("GARBAGE_COLLECTOR_NO")
    private Integer garbageCollectorNo;

    @Schema(description = "内存管理器名称")
    @TableField("GARBAGE_COLLECTOR_NAME")
    private String garbageCollectorName;

    @Schema(description = "GC总次数")
    @TableField("COLLECTION_COUNT")
    private String collectionCount;

    @Schema(description = "GC总时间（毫秒）")
    @TableField("COLLECTION_TIME")
    private String collectionTime;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
