package com.hyr.nettycom.disruptor;

import com.hyr.nettycom.entity.TranslatorDataWapper;
import com.lmax.disruptor.*;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/*
 * @author hyr
 * @date 19-11-10-下午10:43
 * */
public class RingBufferWorkerPoolFactory {
    private static class SingletonHolder {
        static final RingBufferWorkerPoolFactory instance = new RingBufferWorkerPoolFactory();
    }

    private RingBufferWorkerPoolFactory() {

    }

    public static RingBufferWorkerPoolFactory getInstance() {
        return SingletonHolder.instance;
    }

    private Map<String, MessageConsumer> consumers = new ConcurrentHashMap<String, MessageConsumer>();

    private Map<String, MessageProducer> producers = new ConcurrentHashMap<String, MessageProducer>();

    private RingBuffer<TranslatorDataWapper> ringBuffer;
    private SequenceBarrier sequenceBarrier;
    private WorkerPool workerPool;

    public void initAndStart(ProducerType type, int ringBufferSize, WaitStrategy waitStrategy, MessageConsumer[] messageConsumers) {
        //1.构建ringBuffer对象
        this.ringBuffer = RingBuffer.create(type,
                new EventFactory<TranslatorDataWapper>() {
                    public TranslatorDataWapper newInstance() {
                        return new TranslatorDataWapper();
                    }
                }, ringBufferSize,
                waitStrategy);
        //2.设置序号栅栏
        this.sequenceBarrier = this.ringBuffer.newBarrier();
        //3.设置工作池
        this.workerPool = new WorkerPool<TranslatorDataWapper>(this.ringBuffer,
                this.sequenceBarrier,
                new EventExceptionHandler(),
                messageConsumers);
        //4.把所有消费放入池中
        for (MessageConsumer item : messageConsumers) {
            this.consumers.put(item.getConsumerId(), item);
        }
        //5.添加sequence
        ringBuffer.addGatingSequences(workerPool.getWorkerSequences());
        //6.启动工作池
        this.workerPool.start(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2));
    }

    public MessageProducer getMessageProducer(String producerId) {
        MessageProducer producer = this.producers.get(producerId);
        if (null == producer) {
            producer = new MessageProducer(producerId,this.ringBuffer);
            this.producers.put(producerId, producer);
        }
        return producer;
    }

    //静态异常类
    static class EventExceptionHandler implements ExceptionHandler<TranslatorDataWapper> {


        public void handleEventException(Throwable ex, long sequence, TranslatorDataWapper event) {

        }


        public void handleOnStartException(Throwable ex) {

        }

        public void handleOnShutdownException(Throwable ex) {

        }
    }
}

