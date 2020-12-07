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
 * java虚拟机类加载信息表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/8/28 8:45
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("MONITOR_JVM_CLASS_LOADING")
public class MonitorJvmClassLoading {

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
     * 加载的类的总数
     */
    @TableField("TOTAL_LOADED_CLASS_COUNT")
    private Long totalLoadedClassCount;

    /**
     * 当前加载的类的总数
     */
    @TableField("LOADED_CLASS_COUNT")
    private Integer loadedClassCount;

    /**
     * 卸载的类总数
     */
    @TableField("UNLOADED_CLASS_COUNT")
    private Long unloadedClassCount;

    /**
     * 是否启用了类加载系统的详细输出
     */
    @TableField("IS_VERBOSE")
    private String isVerbose;

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
