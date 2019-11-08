package com.hyr.disruptor.demo.heigh;

import com.lmax.disruptor.EventHandler;

/*
 * @author hyr
 * @date 19-11-3-下午7:55
 * @desc 消费者逻辑
 * */
public class EventHandler5 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade trade, long sequence, boolean endOfBatch) throws Exception {
        System.err.println("Handler5：get Price"+trade.getPrice());
        trade.setPrice(trade.getPrice() + 3);
    }
}
