package com.gitee.pifeng.server.business.server.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * <p>
 * java虚拟机GC信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/28 10:31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_JVM_GARBAGE_COLLECTOR")
public class MonitorJvmGarbageCollector {

    /**
     * 主键ID
     */
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    /**
     * 应用实例ID
     */
    @TableField("INSTANCE_ID")
    private String instanceId;

    /**
     * 内存管理器序号
     */
    @TableField("GARBAGE_COLLECTOR_NO")
    private Integer garbageCollectorNo;

    /**
     * 内存管理器名称
     */
    @TableField("GARBAGE_COLLECTOR_NAME")
    private String garbageCollectorName;

    /**
     * GC总次数
     */
    @TableField("COLLECTION_COUNT")
    private String collectionCount;

    /**
     * GC总时间（毫秒）
     */
    @TableField("COLLECTION_TIME")
    private String collectionTime;

    /**
     * 新增时间
     */
    @TableField("INSERT_TIME")
    private Date insertTime;

    /**
     * 更新时间
     */
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
