package com.hyr.disruptor.demo.heigh;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
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
        ExecutorService executorService2 = Executors.newFixedThreadPool(5);

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
//        disruptor.handleEventsWith(new EventHandler1(),new EventHandler2(),new EventHandler3());
//        disruptor.handleEventsWith(new EventHandler1());
//        disruptor.handleEventsWith(new EventHandler2());

        //2.3菱形操作
//        disruptor.handleEventsWith(new EventHandler1(),new EventHandler2()).
//                handleEventsWith(new EventHandler3());

        //2.3菱形操作
//        EventHandlerGroup<Trade> tradeEventHandlerGroup = disruptor.handleEventsWith(new EventHandler1(), new EventHandler2());
//        tradeEventHandlerGroup.then(new EventHandler3());


        //2.4六边行
        EventHandler1 eventHandler1 = new EventHandler1();
        EventHandler2 eventHandler2 = new EventHandler2();
        EventHandler3 eventHandler3 = new EventHandler3();
        EventHandler4 eventHandler4 = new EventHandler4();
        EventHandler5 eventHandler5 = new EventHandler5();
        disruptor.handleEventsWith(eventHandler1,eventHandler4);
        disruptor.after(eventHandler1).handleEventsWith(eventHandler2);
        disruptor.after(eventHandler4).handleEventsWith(eventHandler5);
        disruptor.after(eventHandler2,eventHandler5).handleEventsWith(eventHandler3);
        //3.启动disruptor
        long begin = System.currentTimeMillis();
        RingBuffer<Trade> ringBuffer = disruptor.start();

        CountDownLatch latch = new CountDownLatch(1);

        executorService.submit(new TradePublish(latch,disruptor));

        latch.await();
        System.out.println("总耗时："+(System.currentTimeMillis()-begin));
        disruptor.shutdown();
        executorService.shutdown();
        executorService2.shutdown();
    }
}
