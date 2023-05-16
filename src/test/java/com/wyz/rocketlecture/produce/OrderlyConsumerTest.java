package com.wyz.rocketlecture.produce;

import com.wyz.rocketlecture.MqConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Description: 顺序消费，默认是并发模式，只能保证单个的MessageQueue是顺序的，也就是局部有序
 * @Author: WangYouzheng
 * @Date: 2023/5/16 16:32
 * @Version: V1.0
 */
public class OrderlyConsumerTest {

    /**
     * 默认情况下 一个topic有4个mq队列，100条数据会被轮循分布到四个队列，这样其实就是无序了！！
     * @throws Exception
     */
    @Test
    public void noOrderlyProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("test-orderly-producer");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        // 启动生产者
        producer.start();

        Message message = null;
        for (int i = 0; i < 10; i++) {
            message = new Message("test-orderly-producer-topic", ("AAAAAAAA我是第" + i +" 个消息昂~").getBytes());
            producer.send(message);
        }
        producer.shutdown();
    }

    /**
     * 顺序生产，指定queue，类似于kafka的分区
     *
     * @throws Exception
     */
    public void orderlyProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("test-orderly-producer");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        // 启动生产者
        producer.start();

        Message message = null;
        for (int i = 0; i < 10; i++) {
            message = new Message("test-orderly-producer-topic", ("AAAAAAAA我是第" + i +" 个消息昂~").getBytes());
            producer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    // mqs是从注册中心拿到的当前topic下面的mq
                    // 一般是用取模算法进行负载均衡，相同的业务字段 负载到一个队列中~ 这样保证了业务有序
                    return mqs.get(0);
                }
            }, "识别码 分区码~");
        }
        producer.shutdown();
    }

    @Test
    public void orderlyConsumer() throws Exception  {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-orderly-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("test-orderly-producer-topic", "*");

        /*
        Concurrently就是并发模式的消费，多线程的，只能保证同一个messageQueue是有序的
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("我接到了消息！" + new String(msgs.get(0).getBody()));
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });*/

        // 顺序消费模式
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                System.out.println("我接到了消息！" + new String(msgs.get(0).getBody()));
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }
}
