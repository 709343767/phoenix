package com.gitee.pifeng.server.util;

import com.gitee.pifeng.common.exception.NetException;
import com.gitee.pifeng.common.util.Md5Utils;
import com.gitee.pifeng.common.util.server.NetUtils;
import lombok.extern.slf4j.Slf4j;
import org.hyperic.sigar.SigarException;

/**
 * <p>
 * 应用实例ID工具类
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午10:41:27
 */
@Slf4j
public class InstanceUtils {

    /**
     * 实例名字
     */
    private static final String INSTANCE_NAME = "monitoring-server";

    /**
     * 实例描述
     */
    private static final String INSTANCE_DESC = "监控服务端程序";

    /**
     * <p>
     * 应用实例ID
     * </p>
     * ThreadLocal是JDK包提供的，它提供线程本地变量，如果创建一个ThreadLocal变量，
     * 那么访问这个变量的每个线程都会有这个变量的一个副本，在实际多线程操作的时候，
     * 操作的是自己本地内存中的变量，从而规避了线程安全问题。
     */
    private static final ThreadLocal<String> INSTANCE_ID_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * <p>
     * 私有化构造方法
     * </p>
     *
     * @author 皮锋
     * @custom.date 2021/1/4 9:54
     */
    private InstanceUtils() {
    }

    /**
     * <p>
     * 获取应用实例ID
     * </p>
     *
     * @return 应用实例ID
     * @throws NetException   获取网络信息异常
     * @throws SigarException Sigar异常
     * @author 皮锋
     * @custom.date 2020年3月4日 下午11:12:46
     */
    public static String getInstanceId() throws NetException, SigarException {
        String instanceId = INSTANCE_ID_THREAD_LOCAL.get();
        if (instanceId != null) {
            return instanceId;
        }
        String mac = NetUtils.getLocalMac();
        String ip = NetUtils.getLocalIp();
        instanceId = Md5Utils.encrypt(mac + ip + INSTANCE_NAME);
        INSTANCE_ID_THREAD_LOCAL.set(instanceId);
        return instanceId;
    }

    /**
     * <p>
     * 获取应用实例名称
     * </p>
     *
     * @return 应用实例名称
     * @author 皮锋
     * @custom.date 2020年3月8日 下午3:35:21
     */
    public static String getInstanceName() {
        return INSTANCE_NAME;
    }

    /**
     * <p>
     * 获取应用实例描述
     * </p>
     *
     * @return 应用实例描述
     * @author 皮锋
     * @custom.date 2020/7/31 21:24
     */
    public static String getInstanceDesc() {
        return INSTANCE_DESC;
    }

}
