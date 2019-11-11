package com.hyr.nettyclient;

import com.hyr.nettyclient.consumer.MessageConsumerImpl4Client;
import com.hyr.nettycom.disruptor.MessageConsumer;
import com.hyr.nettycom.disruptor.RingBufferWorkerPoolFactory;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.ProducerType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NettyClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyClientApplication.class, args);
        //构建disruptor
        MessageConsumer[] consumers = new MessageConsumer[4];
        for (int i=0;i<consumers.length;i++){
            MessageConsumer messageConsumer = new MessageConsumerImpl4Client("code:sessionId:00"+i);
            consumers[i] = messageConsumer;
        }
        RingBufferWorkerPoolFactory.getInstance().initAndStart(ProducerType.MULTI,
                1024*1024,
                new YieldingWaitStrategy(),
                consumers);
        new NettyClient().sendData(null);
    }

}
