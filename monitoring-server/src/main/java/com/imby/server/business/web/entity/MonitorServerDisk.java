package com.imby.server.business.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("MONITOR_SERVER_DISK")
@ApiModel(value = "MonitorServerDisk对象", description = "服务器磁盘表")
public class MonitorServerDisk implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "IP地址")
    @TableField("IP")
    private String ip;

    @ApiModelProperty(value = "磁盘序号")
    @TableField("DISK_NO")
    private Integer diskNo;

    @ApiModelProperty(value = "分区的盘符名称")
    @TableField("DEV_NAME")
    private String devName;

    @ApiModelProperty(value = "分区的盘符路径")
    @TableField("DIR_NAME")
    private String dirName;

    @ApiModelProperty(value = "磁盘类型名，比如本地硬盘、光驱、网络文件系统等")
    @TableField("TYPE_NAME")
    private String typeName;

    @ApiModelProperty(value = "磁盘类型，比如 FAT32、NTFS")
    @TableField("SYS_TYPE_NAME")
    private String sysTypeName;

    @ApiModelProperty(value = "磁盘总大小")
    @TableField("TOTAL")
    private String total;

    @ApiModelProperty(value = "磁盘剩余大小")
    @TableField("FREE")
    private String free;

    @ApiModelProperty(value = "磁盘已用大小")
    @TableField("USED")
    private String used;

    @ApiModelProperty(value = "磁盘可用大小")
    @TableField("AVAIL")
    private String avail;

    @ApiModelProperty(value = "磁盘资源的利用率")
    @TableField("USE_PERCENT")
    private String usePercent;

    @ApiModelProperty(value = "新增时间")
    @TableField("INSERT_TIME")
    private Date insertTime;

    @ApiModelProperty(value = "更新时间")
    @TableField("UPDATE_TIME")
    private Date updateTime;

}
