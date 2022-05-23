package com.zyq.javaexperiment.threadlock;

import com.zyq.javaexperiment.common.ZyqThreadPoolExecutor;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @program :CountDownLathLock.java
 * @author  :yanqingzhou
 * @date    :2022/5/20 4:00 下午
 * @description :
 * @version :V 1.0
 */
@SuppressWarnings("all")
public class CountDownLathLock {

    CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
    CyclicBarrier cyclicBarrierCommand = new CyclicBarrier(5, ()->{
        System.out.println("there is command");
    });

    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        CountDownLathLock cyclicBarrierLock = new CountDownLathLock();
    }


    /**
     *  case  公交车等待十个乘客上车； 所有乘客上车后， 发动车辆
     */
    public void caseCountDownLath(){
        ZyqThreadPoolExecutor zyqThreadPoolExecutor = new ZyqThreadPoolExecutor();
        zyqThreadPoolExecutor.shutdown();
        for (int i = 0; i < 10; i++) {
            zyqThreadPoolExecutor.submit(()-> {
                System.out.println();
            });
        }
        System.out.println("发动汽车");
    }
}
