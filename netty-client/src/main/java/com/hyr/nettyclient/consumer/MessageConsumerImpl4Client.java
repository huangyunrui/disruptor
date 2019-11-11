package com.hyr.nettyclient.consumer;

import com.hyr.nettycom.disruptor.MessageConsumer;
import com.hyr.nettycom.entity.TranslatorData;
import com.hyr.nettycom.entity.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/*
 * @author hyr
 * @date 19-11-11-下午2:43
 * */
public class MessageConsumerImpl4Client extends MessageConsumer {
    public MessageConsumerImpl4Client(String consumerId) {
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWapper event) throws Exception {
        TranslatorData response = event.getData();
        ChannelHandlerContext context = event.getContext();
         try {
            System.out.println("client端:id="+response.getId()+
                    ",name="+response.getName()+
                    ",message="+response.getMessage());
        }finally {
            //缓存要释放
            ReferenceCountUtil.release(response);
        }
    }
}
