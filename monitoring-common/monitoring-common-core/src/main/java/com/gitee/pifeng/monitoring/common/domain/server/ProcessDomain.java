package com.gitee.pifeng.monitoring.common.domain.server;

import com.gitee.pifeng.monitoring.common.abs.AbstractSuperBean;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 进程信息
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/9/11 14:26
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ProcessDomain extends AbstractSuperBean {

    /**
     * 正在运行的进程数
     */
    private int processNum;

    /**
     * 进程信息
     */
    private List<ProcessInfoDomain> processInfoList;

    @Data
    @ToString
    @NoArgsConstructor
    @Accessors(chain = true)
    @EqualsAndHashCode(callSuper = true)
    public static class ProcessInfoDomain extends AbstractSuperBean {
        /**
         * 进程ID
         */
        private int processId;
        /**
         * 进程名
         */
        private String name;
        /**
         * 执行进程的完整路径
         */
        private String path;
        /**
         * 进程命令行
         */
        private String commandLine;
        /**
         * 进程当前的工作目录
         */
        private String currentWorkingDirectory;
        /**
         * 用户名
         */
        private String user;
        /**
         * 进程执行状态
         */
        private String state;
        /**
         * 进程已执行的毫秒数
         */
        private String kernelTime;
        /**
         * 进程已启动的毫秒数
         */
        private String upTime;
        /**
         * 进程的开始时间
         */
        private Date startTime;
        /**
         * 进程的累积CPU使用率
         */
        private double cpuLoadCumulative;
        /**
         * 进程的位数
         */
        private String bitness;
        /**
         * 占用内存大小（单位：byte）
         */
        private long memorySize;
    }
}
