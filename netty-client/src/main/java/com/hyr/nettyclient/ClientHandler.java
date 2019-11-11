package com.hyr.nettyclient;

import com.hyr.nettycom.disruptor.MessageProducer;
import com.hyr.nettycom.disruptor.RingBufferWorkerPoolFactory;
import com.hyr.nettycom.entity.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/*
 * @author hyr
 * @date 19-11-10-下午9:16
 * */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*try {
            TranslatorData response = (TranslatorData) msg;
            System.out.println("client端:id="+response.getId()+
                    ",name="+response.getName()+
                    ",message="+response.getMessage());
        }finally {
            //缓存要释放
            ReferenceCountUtil.release(msg);
        }*/

        TranslatorData response = (TranslatorData) msg;
        String producerId = "code:sessionId:002";
        MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
        messageProducer.onData(response,ctx);
    }
}
