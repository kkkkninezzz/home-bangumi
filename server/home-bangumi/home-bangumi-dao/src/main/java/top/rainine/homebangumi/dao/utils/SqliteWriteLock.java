package top.rainine.homebangumi.dao.utils;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author rainine
 * @description sqlite的写入lock
 * @date 2025/2/21 18:13:33
 */
@Getter
@Component
public class SqliteWriteLock {

    private final ReentrantLock lock = new ReentrantLock();

    /**
     * 执行写入的相关逻辑
     * */
    public void execute(Runnable task) {
        lock.lock();
        try {
            task.run();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 执行写入并返回
     * */
    public <T> T execute(Callable<T> task) {
        lock.lock();
        try {
            return task.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
