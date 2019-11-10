package com.hyr.disruptor.demo.review;

import java.util.Queue;
import java.util.concurrent.*;

/*
 * @author hyr
 * @date 19-11-9-下午8:24
 * */
public class Main {
    public static void main(String[] args) {
        ConcurrentHashMap concurrentHashMap = new ConcurrentHashMap();
        CopyOnWriteArrayList list = new CopyOnWriteArrayList();
        ArrayBlockingQueue arrayBlockingQueue = new ArrayBlockingQueue(10);
        Executors.newFixedThreadPool(10);

        //自定义线程池
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                Runtime.getRuntime().availableProcessors() * 2,
                60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(100),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("order-thread");
                        //守护
                        if (thread.isDaemon()) {
                            thread.setDaemon(false);
                        }
                        //优先级
                        if (Thread.NORM_PRIORITY != thread.getPriority()) {
                            thread.setPriority(Thread.NORM_PRIORITY);
                        }
                        return thread;
                    }
                }, new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                System.out.println("拒绝策略");
            }
        }
        );
        /*
        * IO密集型：CPU核数/(1-0.9阻塞系数)
        * 计算密集型： CPU核数+1（CPU核数×2）
        * */
    }
}
