package com.hyr.disruptor.demo.producer;

import com.hyr.disruptor.demo.entity.OrderEvent;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/*
 * @author hyr
 * @date 19-11-3-下午8:30
 * */
public class OrderEventProducer {
    private RingBuffer<OrderEvent> ringBuffer;
    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendData(ByteBuffer data){
        //1. 在生产者发送消息的时候， 首先从ringBuffer得到一个可用序号
        long sequencer = ringBuffer.next();
        try {
            //2. 根据这个序号， 找到具体的OrderEvent元素,是一个没有赋值的空对象
            OrderEvent event = ringBuffer.get(sequencer);
            //3. 进行赋值处理
            event.setValue(data.getLong(0));
        }finally {
            //4. 提交操作
            ringBuffer.publish(sequencer);
        }
    }
}
