package com.gitee.pifeng.monitoring.common.util.server.oshi;

import cn.hutool.core.date.BetweenFormatter;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.gitee.pifeng.monitoring.common.domain.server.ProcessDomain;
import com.gitee.pifeng.monitoring.common.init.InitOshi;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OperatingSystem.ProcessFiltering;
import oshi.software.os.OperatingSystem.ProcessSorting;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.gitee.pifeng.monitoring.common.util.CollectionUtils.distinctByKey;

/**
 * <p>
 * 进程工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2021/9/11 12:26
 */
public class ProcessUtils extends InitOshi {

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/9/11 12:27
     */
    private ProcessUtils() {
    }

    /**
     * <p>
     * 获取进程信息。
     * </p>
     * 进程信息 = CPU占用率排名前10进程信息 + 内存占用率排名前10进程信息；总数小于等于20个进程信息，因为做了去重操作。
     *
     * @return {@link ProcessDomain}
     * @author 皮锋
     * @custom.date 2021/9/11 22:26
     */
    public static ProcessDomain getProcessInfo() {
        // 构建返回值
        ProcessDomain processDomain = ProcessDomain.builder().build();
        List<ProcessDomain.ProcessInfoDomain> processInfoList = Lists.newArrayList();
        // 获取占用CPU排名前十的进程信息
        List<ProcessDomain.ProcessInfoDomain> processOccupyCpuTop10Info = getProcessOccupyCpuTop10Info();
        // 获取占用内存排名前十的进程信息
        List<ProcessDomain.ProcessInfoDomain> processOccupyMemoryTop10Info = getProcessOccupyMemoryTop10Info();
        processInfoList.addAll(processOccupyCpuTop10Info);
        processInfoList.addAll(processOccupyMemoryTop10Info);
        if (CollectionUtils.isNotEmpty(processInfoList)) {
            processInfoList = processInfoList.stream()
                    // 根据进程ID去重
                    .filter(distinctByKey(ProcessDomain.ProcessInfoDomain::getProcessId))
                    // 根据内存大小降序排列
                    .sorted(Comparator.comparing(ProcessDomain.ProcessInfoDomain::getMemorySize).reversed())
                    .collect(Collectors.toList());
        }
        // 进程信息
        processDomain.setProcessInfoList(processInfoList);
        // 正在运行的进程数
        processDomain.setProcessNum(getTotalProcess());
        return processDomain;
    }

    /**
     * <p>
     * 获取占用CPU排名前十的进程信息
     * </p>
     *
     * @return {@link ProcessDomain.ProcessInfoDomain}
     * @author 皮锋
     * @custom.date 2021/9/11 13:17
     */
    public static List<ProcessDomain.ProcessInfoDomain> getProcessOccupyCpuTop10Info() {
        OperatingSystem os = SYSTEM_INFO.getOperatingSystem();
        // 获取进程列表
        List<OSProcess> processes = os.getProcesses(ProcessFiltering.ALL_PROCESSES, ProcessSorting.CPU_DESC, 10);
        // 构建返回值
        return wrapProcessInfoDomainList(processes);
    }

    /**
     * <p>
     * 获取占用内存排名前十的进程信息
     * </p>
     *
     * @return {@link ProcessDomain}
     * @author 皮锋
     * @custom.date 2021/9/11 22:17
     */
    public static List<ProcessDomain.ProcessInfoDomain> getProcessOccupyMemoryTop10Info() {
        OperatingSystem os = SYSTEM_INFO.getOperatingSystem();
        // 获取进程列表
        List<OSProcess> processes = os.getProcesses(ProcessFiltering.ALL_PROCESSES, ProcessSorting.RSS_DESC, 10);
        // 构建返回值
        return wrapProcessInfoDomainList(processes);
    }

    /**
     * <p>
     * 封装进程列表信息
     * </p>
     *
     * @param processes 进程列表
     * @return {@link ProcessDomain.ProcessInfoDomain}
     * @author 皮锋
     * @custom.date 2021/9/11 22:31
     */
    private static List<ProcessDomain.ProcessInfoDomain> wrapProcessInfoDomainList(List<OSProcess> processes) {
        List<ProcessDomain.ProcessInfoDomain> processInfoList = Lists.newArrayList();
        for (OSProcess process : processes) {
            // 进程ID
            int processId = process.getProcessID();
            // 进程名
            String name = process.getName();
            // 执行进程的完整路径
            String path = process.getPath();
            // 进程命令行
            String commandLine = process.getCommandLine();
            // 进程当前的工作目录
            String currentWorkingDirectory = process.getCurrentWorkingDirectory();
            // 用户名
            String user = process.getUser();
            // 进程执行状态
            String state = process.getState().name();
            // 进程已启动的毫秒数
            String upTime = DateUtil.formatBetween(process.getUpTime(), BetweenFormatter.Level.MILLISECOND);
            // 进程的开始时间
            Date startTime = DateUtil.date(process.getStartTime()).toJdkDate();
            // 进程的累积CPU使用率
            double cpuLoadCumulative = NumberUtil.round(process.getProcessCpuLoadCumulative(), 2).doubleValue();
            // 进程的位数
            String bitness = process.getBitness() > 0 ? String.valueOf(process.getBitness()) : "未知";
            // 占用内存大小（单位：byte），RSS
            long memorySize = process.getResidentSetSize();
            // 封装数据
            ProcessDomain.ProcessInfoDomain processInfoDomain = new ProcessDomain.ProcessInfoDomain();
            processInfoDomain.setProcessId(processId);
            processInfoDomain.setName(name);
            processInfoDomain.setPath(path);
            processInfoDomain.setCommandLine(commandLine);
            processInfoDomain.setCurrentWorkingDirectory(currentWorkingDirectory);
            processInfoDomain.setUser(user);
            processInfoDomain.setState(state);
            processInfoDomain.setUpTime(upTime);
            processInfoDomain.setStartTime(startTime);
            processInfoDomain.setCpuLoadCumulative(cpuLoadCumulative);
            processInfoDomain.setBitness(bitness);
            processInfoDomain.setMemorySize(memorySize);
            processInfoList.add(processInfoDomain);
        }
        return processInfoList;
    }

    /**
     * <p>
     * 获取正在运行的进程数
     * </p>
     *
     * @return 正在运行的进程数
     * @author 皮锋
     * @custom.date 2021/9/11 22:16
     */
    public static int getTotalProcess() {
        OperatingSystem os = SYSTEM_INFO.getOperatingSystem();
        return os.getProcessCount();
    }

}
