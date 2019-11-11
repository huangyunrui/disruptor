package com.hyr.nettycom.disruptor;

import com.hyr.nettycom.entity.TranslatorDataWapper;
import com.lmax.disruptor.WorkHandler;

/*
 * @author hyr
 * @date 19-11-10-下午10:47
 * */
public abstract class MessageConsumer implements WorkHandler<TranslatorDataWapper> {

    protected String consumerId;

    public MessageConsumer(String consumerId) {
        this.consumerId = consumerId;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }
}
