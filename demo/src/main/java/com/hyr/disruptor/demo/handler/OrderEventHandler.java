package com.hyr.disruptor.demo.handler;

import com.hyr.disruptor.demo.entity.OrderEvent;
import com.lmax.disruptor.EventHandler;

/*
 * @author hyr
 * @date 19-11-3-下午7:55
 * @desc 消费者逻辑
 * */
public class OrderEventHandler implements EventHandler<OrderEvent> {
    @Override
    public void onEvent(OrderEvent orderEvent, long sequence, boolean endOfBatch) throws Exception {
        System.err.println("消费者："+orderEvent.getValue());
    }
}
