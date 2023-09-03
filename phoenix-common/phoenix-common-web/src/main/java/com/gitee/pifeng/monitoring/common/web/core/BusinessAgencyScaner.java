package com.gitee.pifeng.monitoring.common.web.core;

import com.gitee.pifeng.monitoring.common.web.annotation.TargetInf;
import com.gitee.pifeng.monitoring.common.web.annotation.TargetMethod;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * <p>
 * 业务代理扫描器，获得代理执行业务的方法，添加到命令执行器管理器
 * </p>
 *
 * @author 皮锋
 * @custom.date 2020年3月4日 下午1:56:33
 */
@Slf4j
@Component
public class BusinessAgencyScaner implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        // 类名
        String clazzName = clazz.getName();
        // 如果名字中有“$”，说明存在内部类，需要解析出每一个类
        // 后面想到更好的方式再优化
        String dollar = "$";
        if (StringUtils.contains(clazzName, dollar)) {
            String[] clazzNames = StringUtils.split(clazzName, dollar);
            for (String name : clazzNames) {
                try {
                    Class<?> tempClazz = Class.forName(name);
                    // 注册执行器
                    this.register(bean, tempClazz);
                } catch (ClassNotFoundException ignored) {
                    // 异常直接放弃
                }
            }
        } else {
            // 注册执行器
            this.register(bean, clazz);
        }
        return bean;
    }

    /**
     * <p>
     * 注册执行器
     * </p>
     *
     * @param bean  spring bean
     * @param clazz {@link Class}
     * @author 皮锋
     * @custom.date 2023年4月5日 下午3:13:20
     */
    private void register(Object bean, Class<?> clazz) {
        // 拿到这个类所实现的接口
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            // 扫描类的所有接口父类
            for (Class<?> interFace : interfaces) {
                // 判断是否为代理执行业务的接口
                TargetInf targetInf = interFace.getAnnotation(TargetInf.class);
                if (targetInf == null) {
                    continue;
                }
                // 找出命令方法
                Method[] methods = interFace.getMethods();
                if (methods.length > 0) {
                    for (Method method : methods) {
                        TargetMethod targetInfMethod = method.getAnnotation(TargetMethod.class);
                        if (targetInfMethod == null) {
                            continue;
                        }
                        Invoker invoker = Invoker.valueOf(bean, method);
                        if (InvokerHolder.getInvoker(interFace, method.getName()) == null) {
                            InvokerHolder.addInvoker(interFace, method.getName(), invoker);
                        } else {
                            log.error("重复注册执行器！");
                        }
                    }
                }
            }
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}