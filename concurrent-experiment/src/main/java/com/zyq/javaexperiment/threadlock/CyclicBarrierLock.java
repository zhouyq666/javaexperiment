package com.zyq.javaexperiment.threadlock;

import com.zyq.javaexperiment.common.ZyqThreadPoolExecutor;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
/**
 * @program :javaexperiment
 * @author  :zhouyanqing
 * @date    :CREATE IN 2022/5/18 10:07 上午
 * @description :栅栏锁
 * @version :V 1.0
 */
@SuppressWarnings("all")
public class CyclicBarrierLock {

    CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
    CyclicBarrier cyclicBarrierCommand = new CyclicBarrier(5, ()->{
        System.out.println("there is command");
    });

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        CyclicBarrierLock cyclicBarrierLock = new CyclicBarrierLock();
//        cyclicBarrierLock.caseSingalAll();
        cyclicBarrierLock.caseCommandSingalAll();
    }


    /**
     *  总线程等待 多个线程执行完毕
     */
    public void caseSingalAll() throws BrokenBarrierException, InterruptedException {
        ZyqThreadPoolExecutor zyqThreadPoolExecutor = new ZyqThreadPoolExecutor();
        for (int i = 0; i < 4; i++) {
            zyqThreadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(String.format("Thread ID:  %s  STATUS: %s", Thread.currentThread(), "NEW"));
                    try {
                        cyclicBarrier.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    System.out.println(String.format("Thread ID:  %s  STATUS: %s", Thread.currentThread(), "END"));
                }
            });
        }
        zyqThreadPoolExecutor.shutdown();
    }

    /**
     *  总线程等待 多个线程执行完毕
     */
    public void caseCommandSingalAll() throws BrokenBarrierException, InterruptedException {
        ZyqThreadPoolExecutor zyqThreadPoolExecutor = new ZyqThreadPoolExecutor();
        for (int i = 0; i < 6; i++) {
            zyqThreadPoolExecutor.submit(new Runnable() {
                @Override
                public void run() {
                    System.out.println(String.format("Thread ID:  %s  STATUS: %s", Thread.currentThread(), "NEW"));
                    try {
                        cyclicBarrierCommand.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                    System.out.println(String.format("Thread ID:  %s  STATUS: %s", Thread.currentThread(), "END"));
                }
            });
        }
        zyqThreadPoolExecutor.shutdown();
    }
}
