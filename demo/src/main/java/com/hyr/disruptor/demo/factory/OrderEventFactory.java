package com.hyr.disruptor.demo.factory;

import com.lmax.disruptor.EventFactory;
import com.hyr.disruptor.demo.entity.OrderEvent;

/*
 * @author hyr
 * @date 19-11-3-下午7:53
 * */
public class OrderEventFactory implements EventFactory<OrderEvent> {
    @Override
    public OrderEvent newInstance() {
        //返回一个数据对象
        return new OrderEvent();
    }
}
