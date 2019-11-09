package com.hyr.disruptor.demo.heigh.mulit;

import com.lmax.disruptor.WorkHandler;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/*
 * @author hyr
 * @date 19-11-8-下午5:02
 * */
public class Consumer implements WorkHandler<Order> {

    private String consumerId;
    private  AtomicInteger count = new AtomicInteger(0);

    private Random random = new Random();
    public Consumer(String consumerId){
        this.consumerId = consumerId;
    }
    @Override
    public void onEvent(Order event) throws Exception {
        System.out.println("当前消费者："+this.consumerId+",消费信息："+event.getId());
        count.incrementAndGet();
    }

    public String getConsumerId() {
        return consumerId;
    }


    public  AtomicInteger getCount() {
        return count;
    }

    public  void setCount(AtomicInteger count) {
        this.count = count;
    }
}
