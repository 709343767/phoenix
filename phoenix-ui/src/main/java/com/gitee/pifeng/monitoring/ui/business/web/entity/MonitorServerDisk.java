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
 * 服务器磁盘表
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020/9/4 20:50
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_SERVER_DISK")
@Schema(description = "MonitorServerDisk对象")
public class MonitorServerDisk implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @Schema(description = "IP地址")
    @TableField("IP")
    private String ip;

    @Schema(description = "磁盘序号")
    @TableField("DISK_NO")
    private Integer diskNo;

    @Schema(description = "分区的盘符名称")
    @TableField("DEV_NAME")
    private String devName;

    @Schema(description = "分区的盘符路径")
    @TableField("DIR_NAME")
    private String dirName;

    @Schema(description = "磁盘类型名，比如本地硬盘、光驱、网络文件系统等")
    @TableField("TYPE_NAME")
    private String typeName;

    @Schema(description = "磁盘类型，比如 FAT32、NTFS")
    @TableField("SYS_TYPE_NAME")
    private String sysTypeName;

    @Schema(description = "磁盘总大小（单位：byte）")
    @TableField("TOTAL")
    private Long total;

    @Schema(description = "磁盘剩余大小（单位：byte）")
    @TableField("FREE")
    private Long free;

    @Schema(description = "磁盘已用大小（单位：byte）")
    @TableField("USED")
    private Long used;

    @Schema(description = "磁盘可用大小（单位：byte）")
    @TableField("AVAIL")
    private Long avail;

    @Schema(description = "磁盘资源的利用率")
    @TableField("USE_PERCENT")
    private Double usePercent;

    @Schema(description = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @Schema(description = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
