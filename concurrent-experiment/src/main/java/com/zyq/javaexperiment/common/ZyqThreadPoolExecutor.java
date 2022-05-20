package com.zyq.javaexperiment.common;

import java.util.concurrent.*;

/**
 * @program :javaexperiment
 * @author  :zhouyanqing
 * @date    :CREATE IN 2022/5/18 10:39 上午
 * @description :线程池
 * @version :V 1.0
 */
@SuppressWarnings("all")
public class ZyqThreadPoolExecutor extends ThreadPoolExecutor {

    public ZyqThreadPoolExecutor() {
        super(5, 5, 0, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20),
                (r) -> {
                    Thread thread = new Thread(r);
                    return thread;
                },
                new DiscardOldestPolicy());
    }

    public ZyqThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public ZyqThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public ZyqThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public ZyqThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    @Override
    public Future<?> submit(Runnable task) {
        return super.submit(task);
    }
}
