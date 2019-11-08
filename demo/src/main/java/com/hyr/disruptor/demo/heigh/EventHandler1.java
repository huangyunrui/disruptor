package com.hyr.disruptor.demo.heigh;

import com.hyr.disruptor.demo.entity.OrderEvent;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

/*
 * @author hyr
 * @date 19-11-3-下午7:55
 * @desc 消费者逻辑
 * */
public class EventHandler1 implements EventHandler<Trade>, WorkHandler<Trade> {
    @Override
    public void onEvent(Trade trade, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(trade);
    }

    @Override
    public void onEvent(Trade trade) throws Exception {
        System.err.println("设置名字："+trade.getPrice());
        trade.setName("H1");
        Thread.sleep(1000);
    }
}
