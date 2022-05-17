package com.zyq.javaexperiment;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @program :javaexperiment
 * @author  :zhouyanqing
 * @date    :CREATE IN 2022/5/17 2:29 下午
 * @description :线程池实验
 * @version :V 1.0
 */
public class ThreadPoolExperiment {
    private static AtomicInteger countCreatedNumber = new AtomicInteger(0);
    public static void main(String[] args) {
        caseBasicThreadPool();
//        caseAroundThreadPool();
    }

    /**
     * 线程池基本用法
     */
    public static void caseBasicThreadPool(){
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 30, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30),
                (r) -> {
                    Thread thread = new Thread(r);
                    thread.setDaemon(false);
                    thread.setName("basic thread ");
                    int countNumber = countCreatedNumber.addAndGet(1);
                    System.out.println(String.format("created thread : %s number: %d", thread, countNumber));
                    return thread;
                }
                ,
                (r, h)-> {
                });

        for (int i = 0; i < 100; i++) {
            threadPoolExecutor.submit(()-> {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread ID : "+ Thread.currentThread().getId() + " thread :" + Thread.currentThread().getName());
            });
        }

        threadPoolExecutor.shutdown();
    }

    public static void caseAroundThreadPool(){
        ThreadPoolExecutor threadPoolExecutor = new MyThreadPoolExecutor(10, 30, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(30),
                (r) -> {
                    Thread thread = new Thread(r);
                    thread.setDaemon(false);
                    thread.setName("basic thread ");
                    return thread;
                }
                ,
                (r, h)-> {
                });

        for (int i = 0; i < 5; i++) {
            threadPoolExecutor.submit(()-> {
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("thread ID : "+ Thread.currentThread().getId() + " thread :" + Thread.currentThread().getName());
                if (Thread.currentThread().getId()%2>0){
                    System.out.println("ERROR : 错误！");
                    throw new RuntimeException("自定义错误抛出！");
                }
            });
        }

        threadPoolExecutor.shutdown();

    }

    /**
     * 重写 around(before,after)execute方法
     */
    private static class MyThreadPoolExecutor extends ThreadPoolExecutor{

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        }

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
        }

        public MyThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            super.beforeExecute(t, r);
            System.out.println(String.format("beforeExecute Thread :%s    Runnable: %s ", t, r));

        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {

            super.afterExecute(r, t);
            if (t == null && r instanceof Future<?>) {
                try {
                    Object result = ((Future<?>) r).get();
                } catch (CancellationException ce) {
                    t = ce;
                } catch (ExecutionException ee) {
                    t = ee.getCause();
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt(); // ignore/reset
                }
            }
            if (t != null) {
                System.out.println(t);
            }
        }
    }
}
