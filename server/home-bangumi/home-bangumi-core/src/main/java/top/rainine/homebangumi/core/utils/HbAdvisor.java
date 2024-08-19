package top.rainine.homebangumi.core.utils;

/**
 * @author rainine
 * @description 用于简化在当前类获取被spring增强后的自己
 * @date 2024/8/16 10:49:34
 */
public interface HbAdvisor<T> {

    @SuppressWarnings("unchecked")
    default T self() {
        return (T) SpringContextUtils.getBean(this.getClass());
    }
}
