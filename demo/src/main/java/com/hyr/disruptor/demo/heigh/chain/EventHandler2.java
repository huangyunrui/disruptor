package com.hyr.disruptor.demo.heigh.chain;

import com.lmax.disruptor.EventHandler;

import java.util.UUID;

/*
 * @author hyr
 * @date 19-11-3-下午7:55
 * @desc 消费者逻辑
 * */
public class EventHandler2 implements EventHandler<Trade> {
    @Override
    public void onEvent(Trade trade, long sequence, boolean endOfBatch) throws Exception {
        System.err.println("设置编号：EventHandler2:"+trade.getPrice());
        trade.setId(UUID.randomUUID().toString());
        Thread.sleep(2000);
    }
}
