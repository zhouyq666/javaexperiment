# 栅栏锁 CyclicBarrier

#### 概述

> 一种同步辅助工具，允许一组线程都等待彼此到达一个共同的障碍点。cyclicbarrier在涉及固定大小的线程组的程序中很有用，
> 这些线程有时必须相互等待。该屏障被称为循环屏障，因为它可以在等待的线程释放后重新使用。 
> 并且CyclicBarrier支持一个可选的 Runnable命令，该命令在参与方中的最后一个线程到达后执行，且在释放任何线程之前执行

#### 用法详解

- CyclicBarrier(int parties)  parties: 统计总的调用wait数量
- CyclicBarrier(int parties, Runnable barrierAction)  parties: 统计总的调用wait数量 barrierAction：刷新时的钩子函数
- wait()  调用wait，在count 不为0时等待
- getParties()  barrier域值
#### 核心方法解析

```java
public class CyclicBarrier{

    /**
     * dowait是CyclicBarrier的核心方法
     * step1 
     * @param timed    超时时间
     * @param nanos    系统时间
     * @return
     */
    private int dowait(boolean timed, long nanos)
            throws InterruptedException, BrokenBarrierException,
            TimeoutException {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            final Generation g = generation;

            // 统计栅栏数量
            int index = --count;
            // 如果index 为0 触发 command
            if (index == 0) {  // tripped
                boolean ranAction = false;
                try {
                    final Runnable command = barrierCommand;
                    if (command != null) {
                        command.run();
                        ranAction = true;
                        /*
                        释放条件变量signalAll，唤醒等待线程
                        private void nextGeneration() {
                            // signal completion of last generation
                            trip.signalAll();
                            // set up next generation
                            count = parties;
                            generation = new Generation();
                        }
                        */
                        nextGeneration();
                        return 0;
                    }
                } finally {
                    if (!ranAction) {
                        breakBarrier();
                    }
                }
            }

            // loop until tripped, broken, interrupted, or timed out
            for (;;) {
                try {
                    if (!timed){
                        trip.await();}
                    else if (nanos > 0L){
                        nanos = trip.awaitNanos(nanos);}
                } catch (InterruptedException ie) {
                    if (g == generation && ! g.broken) {
                        breakBarrier();
                        throw ie;
                    } else {
                        // We're about to finish waiting even if we had not
                        // been interrupted, so this interrupt is deemed to
                        // "belong" to subsequent execution.
                        Thread.currentThread().interrupt();
                    }
                }

                if (g.broken){
                    throw new BrokenBarrierException();}

                if (g != generation){
                    return index;}

                if (timed && nanos <= 0L) {
                    breakBarrier();
                    throw new TimeoutException();
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
```
#### 总结
- CyclicBarrier 用于分解线程协调，使用reentrantLock实现， 具备reentrantLock 特性，协调部分使用condition实现
- CyclicBarrier 和 CountDownLatch 功能有相同之处