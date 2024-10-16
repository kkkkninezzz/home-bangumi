package top.rainine.homebangumi.core.common.aync;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * @author rainine
 * @description hb的异步线程池配置
 * @date 2024/4/24 15:34:09
 */
@Configuration
@Slf4j
public class HbAsyncConfigurer implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor threadPool = new ThreadPoolTaskExecutor();
        threadPool.setCorePoolSize(1);
        threadPool.setMaxPoolSize(2);
        threadPool.setThreadNamePrefix("hb-async-"); // 设置线程名称前缀
        threadPool.setAwaitTerminationSeconds(60); // 设置终止等待时间
        threadPool.setWaitForTasksToCompleteOnShutdown(true);
        // 设置处理拒绝执行的任务异常
        threadPool.setRejectedExecutionHandler((r, _) -> log.error("[HbAsyncConfigurer]Task rejected, task: {}", r));
        threadPool.initialize();
        return threadPool;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new HbAsyncExceptionHandler();
    }

    /**
     * 自定义异常处理类
     * @author hry
     *
     */
    static class HbAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(@NotNull Throwable throwable, Method method, @NotNull Object... obj) {
            log.info("[HbAsyncConfigurer]async method invoke failed, method name: {}", method.getName(), throwable);
        }

    }

}
