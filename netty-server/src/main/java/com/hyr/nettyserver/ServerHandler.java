package com.hyr.nettyserver;

import com.hyr.nettycom.disruptor.MessageProducer;
import com.hyr.nettycom.disruptor.RingBufferWorkerPoolFactory;
import com.hyr.nettycom.entity.TranslatorData;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

/*
 * @author hyr
 * @date 19-11-10-下午8:46
 * */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        /*TranslatorData request = (TranslatorData) msg;
        System.out.println("server端:id="+request.getId()+
                ",name="+request.getName()+
                ",message="+request.getMessage());
        //数据库持久化操作IO读写-》交给线程池 去异步调用执行
        TranslatorData response = new TranslatorData();
        response.setId("resp:"+request.getId());
        response.setName("resp:"+request.getName());
        response.setMessage("resp:"+request.getMessage());
        //返回响应
        ctx.channel().writeAndFlush(response);*/

        TranslatorData request = (TranslatorData) msg;
        //应用服务生成规则
        String producerId = "code:sessionId:001";
        MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
        messageProducer.onData(request,ctx);
    }
}
