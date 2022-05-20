# 栅栏锁 CyclicBarrier

#### 概述

> CyclicBarrier 也叫栅栏锁，可以用于多线程协调。
> CyclicBarrier 主要使用 ReentrantLock, Condition 实现核心方法在 dowait()
>

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
