package top.rainine.homebangumi.core.utils;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @authoer rainine
 * @date 2024/3/18 00:22
 * @desc
 */
@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext ac;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        ac = applicationContext;
    }

    /**
     * 获取一个bean
     * */
    public static <T> T getBean(Class<T> clazz) {
        return ac.getBean(clazz);
    }

    /**
     * 根据类型获取一个bean列表
     * */
    public static <T> List<T> getBeansByType(Class<T> clazz) {
        return new ArrayList<>(ac.getBeansOfType(clazz).values());
    }

}
