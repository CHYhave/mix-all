package org.ajhy.util.spring;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class ApplicationContextUtil implements ApplicationContextAware {
    private static ApplicationContext APPLICATION_CONTEXT;

    /**
     * 设置spring上下文  *  * @param applicationContext spring上下文  * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        APPLICATION_CONTEXT = applicationContext;
    }

    public static <T> T getBean(Class<? extends T> clazz) {
        return APPLICATION_CONTEXT.getBean(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        return (T) APPLICATION_CONTEXT.getBean(beanName);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBeanByClassName(String className) {
        String beanName = getBeanName(className);
        try {
            return (T) getBean(beanName);
        } catch (BeansException e) {
            return null;
        }
    }

    public static String registerBean(BeanDefinition beanDefinition, ClassLoader classLoader) {
        String beanClassName = beanDefinition.getBeanClassName();
        if (StringUtils.isBlank(beanClassName)) {
            throw new NullPointerException("beanDefinition.beanClassName is null");
        }
        String beanName = getBeanName(beanClassName);
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) APPLICATION_CONTEXT.getAutowireCapableBeanFactory();
        beanFactory.setBeanClassLoader(classLoader);
        beanFactory.registerBeanDefinition(beanName, beanDefinition);
        return beanName;
    }

    public static <T> Map<String, T> getBeansOfType(Class<T> clazz) {
        return APPLICATION_CONTEXT.getBeansOfType(clazz);
    }

    public static boolean existBean(String className) {
        String beanName = getBeanName(className);
        return APPLICATION_CONTEXT.containsBean(beanName);
    }

    private static String getBeanName(String className) {
        String name = className.substring(className.lastIndexOf(".") + 1);
        String start = name.substring(0, 1);
        String end = name.substring(1);
        return start.toLowerCase() + end;
    }
}