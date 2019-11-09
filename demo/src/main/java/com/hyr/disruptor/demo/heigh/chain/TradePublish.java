package com.hyr.disruptor.demo.heigh.chain;

import com.lmax.disruptor.EventTranslator;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/*
 * @author hyr
 * @date 19-11-8-下午3:22
 * */
public class TradePublish implements Runnable {
    private CountDownLatch latch;
    private Disruptor<Trade> disruptor;
    public static int PUBLIS_COUNT = 1;
    public TradePublish(CountDownLatch latch, Disruptor<Trade> disruptor) {
        this.disruptor = disruptor;
        this.latch = latch;
    }

    @Override
    public void run() {
        TradeEventTranslator tradeEventTranslator = new TradeEventTranslator();
        //提交任务方式
        for (int i=0;i<PUBLIS_COUNT;i++) {
            disruptor.publishEvent(tradeEventTranslator);
        }
        latch.countDown();
    }

}
class TradeEventTranslator implements EventTranslator<Trade>{

    private Random random = new Random();

    @Override
    public void translateTo(Trade trade, long l) {
        this.generrageTrade(trade);
    }

    private void generrageTrade(Trade trade) {
        trade.setPrice(random.nextDouble() * 9999);
    }
}
