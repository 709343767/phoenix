package com.transfar.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 唯一的bean名字生成器
 * </p>
 * <ul>
 * <li>spring提供两种beanName生成策略，基于注解的spring-boot默认使用的是AnnotationBeanNameGenerator，它生成beanName的策略就是，取当前类名（不是全限定类名）作为beanName。</li>
 * <li>由此，如果出现不同包结构下同样的类名称，肯定会出现冲突。所以自己覆盖spring提供的默认beanName生成策略。</li>
 * </ul>
 *
 * @author 皮锋
 * @custom.date 2020/2/13 15:45
 */
public class UniqueBeanNameGenerator extends AnnotationBeanNameGenerator {

    /**
     * <p>
     * 生成Bean名字
     * </p>
     *
     * @param definition Bean定义
     * @param registry   Bean定义注册处，该类的作用主要是向注册表中注册BeanDefinition实例
     * @return Bean名字
     * @author 皮锋
     * @custom.date 2020/2/15 13:46
     */
    @Override
    public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
        // 全限定类名
        String beanName = definition.getBeanClassName();
        if (StringUtils.hasText(beanName)) {
            return beanName;
        }
        return super.buildDefaultBeanName(definition, registry);
    }

}
