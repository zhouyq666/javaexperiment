
# 文档解析

#### 运行流程
![img.png](img.png)

####参数解析：

>public ThreadPoolExecutor(int corePoolSize,
int maximumPoolSize,
long keepAliveTime,
TimeUnit unit,
BlockingQueue<Runnable> workQueue,
ThreadFactory threadFactory,
RejectedExecutionHandler handler)

- 1 param：corePoolSize     线程池核心线程大小      影响活跃线程数量
- 2 param：maximumPoolSize  最大运行线程数量        超出时，会出现拒绝策略
- 3 param：keepAliveTime    线程保持时间           当线程数量到达核心线程数之后， 新创建的线程等待新任务的时间
- 4 param：timeunit         单位
- 5 param: workQueue        队列长度              创建一个队列，并且默认初始化长度
- 6 param: threadFactory    线程创建工厂
- 7 param: rejectHandle     拒绝策略

#### 拒绝策略有四种默认拒绝策略，
- 1 抛出reject异常
public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
throw new RejectedExecutionException("Task " + r.toString() +
" rejected from " +
e.toString());
}
- 2 重试当前线程
public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
if (!e.isShutdown()) {
r.run();
}
}
- 3 不做任何操作，并且当前线程会被丢弃

- 4 重试当前线程， 并丢弃队列其他线程（丢弃未执行线程）
public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
if (!e.isShutdown()) {
e.getQueue().poll();
e.execute(r);
}
}

#### threadPool 扩展：
- 1 execute(before/after) 代理，  需要使用子类继承， 并且在结尾处要调用 execute（），通过重写execute可以对线程池运行状态，等监控，统计
    - beforeExecute(Thread t, Runnable r) 在线程池执行之前执行需要先调用 super.beforeExecute(Thread t, Runnable r);
    - afterExecute(Thread t, Runnable r) 在线程池执行之前执行需要先调用 super.afterExecute(r, t);
    - afterExecute(Runnable r, Throwable t) 异常捕获方法
      ```java 
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
      ```
      