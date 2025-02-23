package top.rainine.homebangumi.dao.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import top.rainine.homebangumi.dao.utils.SqliteWriteLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @authoer rainine
 * @date 2025/2/23 22:05
 * @desc 对crud 的save方法进行加锁
 */
@Aspect
@Component
@RequiredArgsConstructor
public class CrudRepositorySaveLockAspect {

    private final SqliteWriteLock sqliteWriteLock;



    /**
     * 拦截所有 CrudRepository 的 save() 方法
     * 表达式解释：拦截任意返回值、任意参数的 save 方法，且该方法定义在 CrudRepository 接口中
     */
    @Around("execution(* org.springframework.data.repository.CrudRepository+.save(..))")
    public Object aroundSave(ProceedingJoinPoint joinPoint) throws Throwable {
        ReentrantLock lock = sqliteWriteLock.getLock();
        lock.lock();
        try {
            // 执行原 save() 方法
            return joinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 拦截所有 CrudRepository 的 saveAll() 方法
     * 表达式解释：拦截任意返回值、任意参数的 save 方法，且该方法定义在 CrudRepository 接口中
     */
    @Around("execution(* org.springframework.data.repository.CrudRepository+.saveAll(..))")
    public Object aroundSaveAll(ProceedingJoinPoint joinPoint) throws Throwable {
        ReentrantLock lock = sqliteWriteLock.getLock();
        lock.lock();
        try {
            // 执行原 save() 方法
            return joinPoint.proceed();
        } finally {
            lock.unlock();
        }
    }
}
