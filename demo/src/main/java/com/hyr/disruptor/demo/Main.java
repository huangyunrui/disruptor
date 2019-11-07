package com.hyr.disruptor.demo;

import com.hyr.disruptor.demo.entity.OrderEvent;
import com.hyr.disruptor.demo.factory.OrderEventFactory;
import com.hyr.disruptor.demo.handler.OrderEventHandler;
import com.hyr.disruptor.demo.producer.OrderEventProducer;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;

/*
 * @author hyr
 * @date 19-11-3-下午7:59
 * */
public class Main {
    public static void main(String[] args) {
        //参数准备
        OrderEventFactory eventFactory = new OrderEventFactory();
        int ringBufferSize = 1024 * 1024;
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        /*
        * 1. eventFactory 消息(event)工厂对象
        * 2. ringBufferSize： 容器长度
        * 3. executor： 线程池（自定义线程池）RejectedExecutionHandler 拒绝策略
        * 4. producerType： 单生产者 或者 多生产者
        * 5. waitStrategy: 等待策略
        * */

        //1.实例化disruptor对象
        Disruptor<OrderEvent> disruptor = new Disruptor<OrderEvent>(
                eventFactory,
                ringBufferSize,
//                DaemonThreadFactory.INSTANCE,
                executor,
                ProducerType.SINGLE,
                new BlockingWaitStrategy());


        //2. 添加消费者监听(disruptor 与消费者进行关联)
        disruptor.handleEventsWith(new OrderEventHandler());

        //3. 启动disruptor
        disruptor.start();

        //4. 获取实际存储数据的容器，RingBuffer
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();
        OrderEventProducer  producer = new OrderEventProducer(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long i=0;i<100;i++){
            bb.putLong(0,i);
            producer.sendData(bb);
        }

        disruptor.shutdown();
        executor.shutdown();
    }
}
