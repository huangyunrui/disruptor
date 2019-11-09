package com.hyr.disruptor.demo.heigh.mulit;

import com.lmax.disruptor.RingBuffer;

/*
 * @author hyr
 * @date 19-11-9-下午7:43
 * */
public class Producer {
    private RingBuffer<Order> ringBuffer;
    public Producer(RingBuffer<Order> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void sendData(String uuid) {
        long next = ringBuffer.next();
        try {
            Order order =  ringBuffer.get(next);
            order.setId(uuid);
        }finally {
            ringBuffer.publish(next);
        }
    }
}
