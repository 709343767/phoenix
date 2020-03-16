package com.transfar.common.util;

import com.google.common.collect.Lists;
import com.transfar.common.domain.server.*;

import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.jni.ArchNotSupportedException;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.*;

import java.io.*;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

/**
 * <p>
 * Sigar工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月3日 上午11:55:09
 */
@Slf4j
public final class SigarUtils {

    /**
     * 系统属性
     */
    private static Properties props = System.getProperties();

    static {
        try {
            initSigar();
        } catch (Exception e) {
            log.error("初始化Sigar异常！", e);
        }
    }

    /**
     * 创建Sigar对象
     */
    private static Sigar sigar = new Sigar();

    /**
     * 存储单位转M
     */
    private static double dividend = 1024 * 1024;

    /**
     * <p>
     * 屏蔽共有构造方法
     * </p>
     *
     * @author 皮锋
     */
    private SigarUtils() {
    }

    /**
     * <p>
     * 初始化sigar的配置文件
     * </p>
     *
     * @throws ArchNotSupportedException 架构不支持异常
     * @throws IOException               IO异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午12:56:14
     */
    public static void initSigar() throws ArchNotSupportedException, IOException {
        SigarLoader loader = new SigarLoader(Sigar.class);
        String lib = loader.getLibraryName();
        log.info("初始化Sigar库文件：{}", lib);
        @Cleanup
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("sigar.so/" + lib);
        // 当前文件夹路径
        String currentDir = props.getProperty("user.dir");
        File tempDir = new File(
                currentDir + File.separator + "tmp" + File.separator + "sigar" + File.separator + "lib");
        if (!tempDir.exists()) {
            boolean isMkdirs = tempDir.mkdirs();
            log.debug("创建Sigar库文件夹：{}", isMkdirs);
        }
        @Cleanup
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(tempDir, lib), false));
        int lentgh;
        assert is != null;
        while ((lentgh = is.read()) != -1) {
            os.write(lentgh);
        }
        System.setProperty("org.hyperic.sigar.path", tempDir.getCanonicalPath());
        log.info("org.hyperic.sigar.path：{}", System.getProperty("org.hyperic.sigar.path"));
    }

    /*
     * <p> 获取应用服务器信息 </p>
     *
     * @return AppServerDomain
     *
     * @author 皮锋
     *
     * @custom.date 2020年3月3日 下午1:49:16
     */
    /*
     * public static AppServerDomain getAppServerInfo() { try { InetAddress address
     * = InetAddress.getLocalHost(); HttpServletRequest request =
     * ServletContextUtils.getRequest(); ServletContext application =
     * request.getSession().getServletContext(); // ip String ip =
     * address.getHostAddress(); // url String url = request.getScheme() // 请求头 +
     * "://" + address.getHostAddress() // 服务器地址 + ":" + request.getServerPort() //
     * 端口号 + request.getContextPath(); // 项目名称
     *
     * // 服务器类型 String serverType = application.getServerInfo(); // 服务器时间 Date
     * serverTime = new Date(); return
     * AppServerDomain.builder().serverIP(ip).serverURL(url).serverType(serverType).
     * serverTime(serverTime) .build(); } catch (Exception e) { // 没有应用服务器，返回空对象
     * return AppServerDomain.builder().build(); } }
     */

    /**
     * <p>
     * 获取操作系统信息
     * </p>
     *
     * @return OsDomain
     * @author 皮锋
     * @custom.date 2020年3月3日 下午2:15:45
     */
    public static OsDomain getOsInfo() {
        Calendar cal = Calendar.getInstance();
        TimeZone timeZone = cal.getTimeZone();
        return new OsDomain()//
                .setOsName(props.getProperty("os.name") + " " + props.getProperty("os.arch"))//
                .setOsVersion(props.getProperty("os.version"))//
                .setUserName(props.getProperty("user.name"))//
                .setUserHome(props.getProperty("user.home"))//
                .setOsTimeZone(timeZone.getDisplayName());
    }

    /**
     * <p>
     * 获取内存信息
     * </p>
     *
     * @return MemoryDomain
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午2:24:41
     */
    public static MemoryDomain getMemoryInfo() throws SigarException {
        Mem mem = sigar.getMem();
        return new MemoryDomain()//
                .setMemTotal(String.format("%.2f", mem.getTotal() / dividend) + "M")//
                .setMemUsed(String.format("%.2f", mem.getUsed() / dividend) + "M")//
                .setMemFree(String.format("%.2f", mem.getFree() / dividend) + "M")//
                .setMenUsedPercent(String.format("%.2f", mem.getUsedPercent()) + "%");
    }

    /**
     * <p>
     * 获取Cpu信息
     * </p>
     *
     * @return CpuDomain
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午1:04:04
     */
    public static CpuDomain getCpuInfo() throws SigarException {
        CpuDomain cpuDomain = new CpuDomain();

        CpuInfo[] cpuInfos = sigar.getCpuInfoList();
        CpuPerc[] cpuList = sigar.getCpuPercList();

        List<CpuDomain.CpuInfoDomain> cpuInfoDomains = Lists.newArrayList();
        for (int m = 0; m < cpuList.length; m++) {
            // 设置每一个cpu的信息
            CpuDomain.CpuInfoDomain cpuInfoDomain = new CpuDomain.CpuInfoDomain();

            cpuInfoDomain.setCpuMhz(cpuInfos[m].getMhz() + "MHz");
            String cpuIdle = String.format("%.2f", cpuList[m].getIdle() * 100) + "%";
            cpuInfoDomain.setCpuIdle(cpuIdle);
            String cpuCombined = String.format("%.2f", cpuList[m].getCombined() * 100) + "%";
            cpuInfoDomain.setCpuCombined(cpuCombined);
            cpuInfoDomains.add(cpuInfoDomain);
        }
        cpuDomain.setCpuNum(cpuInfoDomains.size()).setCpuList(cpuInfoDomains);
        return cpuDomain;
    }

    /**
     * <p>
     * 获取网卡信息
     * </p>
     *
     * @return NetDomain
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午3:33:08
     */
    public static NetDomain getNetInfo() throws SigarException {
        NetDomain netDomain = new NetDomain();

        String[] netInfos = sigar.getNetInterfaceList();

        List<NetDomain.NetInterfaceConfigDomain> netInterfaceConfigDomains = Lists.newArrayList();
        for (String info : netInfos) {
            NetInterfaceConfig netInfo = sigar.getNetInterfaceConfig(info);
            if ((NetFlags.LOOPBACK_ADDRESS.equals(netInfo.getAddress())) // 127.0.0.1
                    || (netInfo.getFlags() == 0) // 标识为0
                    || (NetFlags.NULL_HWADDR.equals(netInfo.getHwaddr())) // MAC地址不存在
                    || (NetFlags.ANY_ADDR.equals(netInfo.getAddress())) // 0.0.0.0
            ) {
                continue;
            }
            NetDomain.NetInterfaceConfigDomain netInterfaceConfigDomain = new NetDomain.NetInterfaceConfigDomain();
            netInterfaceConfigDomain.setName(netInfo.getName())//
                    .setType(netInfo.getType())//
                    .setAddress(netInfo.getAddress())//
                    .setMask(netInfo.getNetmask())//
                    .setBroadcast(netInfo.getBroadcast());
            netInterfaceConfigDomains.add(netInterfaceConfigDomain);
        }
        netDomain.setNetNum(netInterfaceConfigDomains.size()).setNetList(netInterfaceConfigDomains);
        return netDomain;
    }

    /**
     * <p>
     * 获取java虚拟机信息
     * </p>
     *
     * @return JvmDomain
     * @author 皮锋
     * @custom.date 2020年3月3日 下午4:17:55
     */
    public static JvmDomain getJvmInfo() {
        Runtime runTime = Runtime.getRuntime();
        return new JvmDomain()//
                .setJavaPath(props.getProperty("java.home"))//
                .setJavaVendor(props.getProperty("java.vendor"))//
                .setJavaVersion(props.getProperty("java.version"))//
                .setJavaName(props.getProperty("java.specification.name"))//
                .setJvmVersion(props.getProperty("java.vm.version"))//
                .setJvmTotalMemory(String.format("%.2f", runTime.totalMemory() / dividend) + "M")//
                .setJvmFreeMemory(String.format("%.2f", runTime.freeMemory() / dividend) + "M");
    }

    /**
     * <p>
     * 获取磁盘信息
     * </p>
     *
     * @return DiskDomain
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020/3/8 21:46
     */
    public static DiskDomain getDiskInfo() throws SigarException {
        DiskDomain diskDomain = new DiskDomain();
        List<DiskDomain.DiskInfoDomain> diskInfoDomains = Lists.newArrayList();

        FileSystem[] fileSystemArray = sigar.getFileSystemList();
        for (FileSystem fileSystem : fileSystemArray) {
            DiskDomain.DiskInfoDomain diskInfoDomain = new DiskDomain.DiskInfoDomain();

            diskInfoDomain.setDevName(fileSystem.getDevName());
            diskInfoDomain.setDirName(fileSystem.getDirName());
            diskInfoDomain.setTypeName(fileSystem.getTypeName());
            diskInfoDomain.setSysTypeName(fileSystem.getSysTypeName());

            FileSystemUsage fileSystemUsage;
            try {
                fileSystemUsage = sigar.getFileSystemUsage(fileSystem.getDirName());
            }
            //当fileSystem.getType()为5时会出现该异常——此时文件系统类型为光驱
            catch (SigarException e) {
                // 本地硬盘
                if (fileSystem.getType() == 2) {
                    throw e;
                }
                continue;
            }
            switch (fileSystem.getType()) {
                case 0: // TYPE_UNKNOWN ：未知
                    break;
                case 1: // TYPE_NONE
                    break;
                case 2: // TYPE_LOCAL_DISK : 本地硬盘
                    diskInfoDomain.setTotal(String.format("%.2f", fileSystemUsage.getTotal() / dividend) + "M");
                    diskInfoDomain.setFree(String.format("%.2f", fileSystemUsage.getFree() / dividend) + "M");
                    diskInfoDomain.setUsed(String.format("%.2f", fileSystemUsage.getUsed() / dividend) + "M");
                    diskInfoDomain.setAvail(String.format("%.2f", fileSystemUsage.getAvail() / dividend) + "M");
                    diskInfoDomain.setUsePercent(String.format("%.2f", fileSystemUsage.getUsePercent() * 100) + "%");
                    break;
                case 3:// TYPE_NETWORK ：网络
                    break;
                case 4:// TYPE_RAM_DISK ：闪存
                    break;
                case 5:// TYPE_CDROM ：光驱
                    break;
                case 6:// TYPE_SWAP ：页面交换
                    break;
            }
            diskInfoDomains.add(diskInfoDomain);
        }
        diskDomain.setDiskInfoList(diskInfoDomains);
        diskDomain.setDiskNum(diskInfoDomains.size());
        return diskDomain;
    }

    /**
     * <p>
     * 获取服务器信息
     * </p>
     *
     * @return ServerDomain
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月3日 下午4:25:30
     */
    public static ServerDomain getServerInfo() throws SigarException {
        return new ServerDomain()//
                // .appServerDomain(getAppServerInfo())//
                .setCpuDomain(getCpuInfo())//
                .setJvmDomain(getJvmInfo())//
                .setMemoryDomain(getMemoryInfo())//
                .setNetDomain(getNetInfo())//
                .setOsDomain(getOsInfo())//
                .setDiskDomain(getDiskInfo());
    }

}
