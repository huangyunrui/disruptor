package com.hyr.nettycom.disruptor;

import com.hyr.nettycom.entity.TranslatorData;
import com.hyr.nettycom.entity.TranslatorDataWapper;
import com.lmax.disruptor.RingBuffer;
import io.netty.channel.ChannelHandlerContext;

/*
 * @author hyr
 * @date 19-11-10-下午10:47
 * */
public class MessageProducer {
    private RingBuffer<TranslatorDataWapper> ringBuffer;
    private String producerId;
    public MessageProducer(String producerId,RingBuffer<TranslatorDataWapper> ringBuffer) {
        this.ringBuffer = ringBuffer;
        this.producerId = producerId;
    }

    public void onData(TranslatorData data, ChannelHandlerContext context) {
        long sequence = ringBuffer.next();
        try {
            TranslatorDataWapper event =  ringBuffer.get(sequence);
            event.setContext(context);
            event.setData(data);
        }finally {
            ringBuffer.publish(sequence);
        }
    }
}
