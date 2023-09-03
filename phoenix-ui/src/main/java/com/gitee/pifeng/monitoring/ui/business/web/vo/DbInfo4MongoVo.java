package com.gitee.pifeng.monitoring.ui.business.web.vo;

import com.gitee.pifeng.monitoring.common.inf.ISuperBean;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * <p>
 * Mongo数据库信息表现层对象
 * https://docs.mongodb.com/manual/reference/command/dbStats/#std-label-dbstats-output
 * </p>
 *
 * @author 皮锋
 * @custom.date 2022/1/20 14:32
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Mongo数据库信息表现层对象")
public class DbInfo4MongoVo implements ISuperBean {

    /**
     * 平均每个对象的大小, 通过 dataSize/Objects 得到
     */
    private String avgObjSize;
    /**
     * 数据库中的集合数
     */
    private Integer collections;
    /**
     * 数据库中保存的未压缩数据的总大小
     */
    private String dataSize;
    /**
     * 数据库的名称
     */
    private String db;
    /**
     * MongoDB存储数据的文件系统上所有磁盘容量的总大小
     */
    private String fsTotalSize;
    /**
     * MongoDB存储数据的文件系统上正在使用的所有磁盘空间的总大小
     */
    private String fsUsedSize;
    /**
     * 索引占有磁盘大小
     */
    private String indexSize;
    /**
     * 索引数
     */
    private Integer indexes;
    /**
     * 数据库中所有集合中的对象（特别是文档）的数量
     */
    private Integer objects;
    /**
     * ok
     */
    private BigDecimal ok;
    /**
     * scale 命令使用的值
     */
    private BigDecimal scaleFactor;
    /**
     * 分配给数据库中用于文档存储的所有集合的空间总和，包括可用空间。
     */
    private String storageSize;
    /**
     * 为数据库中所有集合中的文档和索引分配的空间总和
     */
    private String totalSize;
    /**
     * 数据库中的视图数
     */
    private Integer views;
}
