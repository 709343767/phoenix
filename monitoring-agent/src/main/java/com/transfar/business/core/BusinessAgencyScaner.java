package com.transfar.business.core;

import java.lang.reflect.Method;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import com.transfar.business.annotation.TargetInf;
import com.transfar.business.annotation.TargetMethod;

import lombok.extern.slf4j.Slf4j;

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
		Class<?>[] interfaces = clazz.getInterfaces();
		if (interfaces != null && interfaces.length > 0) {
			// 扫描类的所有接口父类
			for (Class<?> interFace : interfaces) {
				// 判断是否为代理执行业务的接口类
				TargetInf vService = interFace.getAnnotation(TargetInf.class);
				if (vService == null) {
					continue;
				}
				// 找出命令方法
				Method[] methods = interFace.getMethods();
				if (methods.length > 0) {
					for (Method method : methods) {
						TargetMethod vMethod = method.getAnnotation(TargetMethod.class);
						if (vMethod == null) {
							continue;
						}
						// 接口
						Class<?> clz = vService.inf();
						// 方法
						String mtd = vMethod.method();
						Invoker invoker = Invoker.valueOf(method, bean);
						if (InvokerHolder.getInvoker(clz, mtd) == null) {
							InvokerHolder.addInvoker(clz, mtd, invoker);
						} else {
							log.error("重复注册执行器！");
						}
					}
				}
			}
		}
		return bean;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}