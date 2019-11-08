package com.hyr.disruptor.demo.heigh;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * @author hyr
 * @date 19-11-8-下午3:12
 * */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        ExecutorService executorService2 = Executors.newFixedThreadPool(8);

        //1.构建disruptor
        Disruptor<Trade> disruptor = new Disruptor<>(
                new EventFactory<Trade>() {
                    @Override
                    public Trade newInstance() {
                        return new Trade();
                    }
                },  1024*1024,
                executorService2,
                ProducerType.SINGLE,
                new BusySpinWaitStrategy()
        );

        //2.设置消费者
        //2.1 串行操作
       /* disruptor.handleEventsWith(new EventHandler1())
                .handleEventsWith(new EventHandler2())
                .handleEventsWith(new EventHandler3());*/
        //2.2 并操作
        disruptor.handleEventsWith(new EventHandler1(),new EventHandler2(),new EventHandler3());
        //3.启动disruptor
        long begin = System.currentTimeMillis();
        RingBuffer<Trade> ringBuffer = disruptor.start();

        CountDownLatch latch = new CountDownLatch(0);

        executorService.submit(new TradePublish(latch,disruptor));

        latch.wait();
        System.out.println("总耗时："+(System.currentTimeMillis()-begin));
        disruptor.shutdown();
        executorService.shutdown();
        executorService2.shutdown();
    }
}
