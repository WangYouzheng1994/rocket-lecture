package com.wyz.rocketlecture;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Description: 延迟消息，定时消息，先推入队列，后面到了定时以后这个消息才可以被消费到
 * @Author: WangYouzheng
 * @Date: 2023/5/16 14:29
 * @Version: V1.0
 */
public class DMsTest {
    @Test
    public void delayMessageProduceTest() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("delay-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();

        Message message = new Message("test_delay_topic", "你好我是延迟消息，主要用于定时触发场景，延迟10s aaabbbb".getBytes());
        // 给消息设置延迟时间 https://rocketmq.apache.org/docs/4.x/producer/04message3
        message.setDelayTimeLevel(3); // 延迟10S

        producer.send(message);
        producer.shutdown();
    }

    /**
     * 延迟消息消费
     * @throws Exception
     */
    @Test
    public void delayMessageConsumeTest() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-delay-consumer-group");
        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("test_delay_topic", "*");
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("我是消费者");
                System.out.println(msgs.get(0).toString());
                System.out.println("上下文" + context);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();

        System.in.read();
    }
}
