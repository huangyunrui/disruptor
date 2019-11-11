package com.hyr.nettyserver.consumer;

import com.hyr.nettycom.disruptor.MessageConsumer;
import com.hyr.nettycom.entity.TranslatorData;
import com.hyr.nettycom.entity.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;

/*
 * @author hyr
 * @date 19-11-11-下午2:45
 * */
public class MessageConsumerImpl4Server extends MessageConsumer {
    public MessageConsumerImpl4Server(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWapper event) throws Exception {
        TranslatorData request = event.getData();
        ChannelHandlerContext context = event.getContext();
        //1.业务处理
        System.out.println("server端:id="+request.getId()+
                ",name="+request.getName()+
                ",message="+request.getMessage());
        //2.返回数据
        TranslatorData response = new TranslatorData();
        response.setId("resp:"+request.getId());
        response.setName("resp:"+request.getName());
        response.setMessage("resp:"+request.getMessage());
        //返回响应
        context.channel().writeAndFlush(response);
    }
}
