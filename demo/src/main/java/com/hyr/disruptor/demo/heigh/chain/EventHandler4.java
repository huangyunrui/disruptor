package com.hyr.disruptor.demo.heigh.chain;

import com.lmax.disruptor.EventHandler;

/*
 * @author hyr
 * @date 19-11-3-下午7:55
 * @desc 消费者逻辑
 * */
public class EventHandler4 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade trade, long sequence, boolean endOfBatch) throws Exception {
        System.err.println("Handler4："+trade.getPrice());
        trade.setPrice(10.0);
    }
}
