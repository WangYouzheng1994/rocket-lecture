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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class RocketLectureApplicationTests {

    @Test
    void contextLoads() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("test-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);

        // 启动
        producer.start();

        Message message = new Message("testRocketTopic", "我是第一个简单消息".getBytes());
        producer.send(message);
        producer.shutdown();
    }

    @Test
    void simpleConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer-group");

        consumer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        consumer.subscribe("testRocketTopic", "*");
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
