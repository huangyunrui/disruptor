package com.hyr.disruptor.demo.heigh.mulit;

import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;

/*
 * @author hyr
 * @date 19-11-8-下午4:56
 * */
public class Main {
    public static void main(String[] args) throws InterruptedException {
        //1.创建RingBuffer
        RingBuffer<Order> ringBuffer = RingBuffer.create(ProducerType.MULTI,
                new EventFactory<Order>() {
                    @Override
                    public Order newInstance() {
                        return new Order();
                    }
                },1024 * 1024,
                new YieldingWaitStrategy());

        //2.ringBuffer，创建一个屏障
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //3.构建多消费者数组
        Consumer[] consumers = new Consumer[10];
        for (int i=0;i<consumers.length;i++){
            consumers[i] = new Consumer("C"+i);
        }
        //4.构建多消费者工作池
        WorkerPool<Order> consumerWorkerPool = new WorkerPool<Order>(
                ringBuffer,
                sequenceBarrier,
                new EventExceptionHandler(),
                consumers);
        //5.设置多消费者的sequence序号，用于单独设计消费进度
        ringBuffer.addGatingSequences(consumerWorkerPool.getWorkerSequences());

        //6.启动workerpool
        consumerWorkerPool.start(Executors.newFixedThreadPool(10));

        //7.生产中生产消息
        CountDownLatch latch =new CountDownLatch(1);
        for (int i=0;i<100;i++){
            Producer producer = new Producer(ringBuffer);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    for (int j=0;j<100;j++){
                        producer.sendData(UUID.randomUUID().toString());
                    }
                }
            }).start();
        }
        Thread.sleep(2000);
        System.out.println("线程创建完毕，开始生产数据");
        latch.countDown();

        Thread.sleep(4000);
        System.out.println(consumers[2].getCount());
    }

    static class EventExceptionHandler implements ExceptionHandler<Order>{

        @Override
        public void handleEventException(Throwable throwable, long l, Order order) {

        }

        @Override
        public void handleOnStartException(Throwable throwable) {

        }

        @Override
        public void handleOnShutdownException(Throwable throwable) {

        }
    }
}
