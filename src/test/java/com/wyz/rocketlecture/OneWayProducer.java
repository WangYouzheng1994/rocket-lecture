package com.wyz.rocketlecture;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Description: 单向消息，用于不关心sendResult的场景：日志消息。提供了高吞吐，但是会出现msg丢失。
 * @Author: WangYouzheng
 * @Date: 2023/5/16 12:23
 * @Version: V1.0
 */
@SpringBootTest
public class OneWayProducer {
    @Test
    public void onewayProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("oneway-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();

        Message message = new Message("test_onwway_topic", "你好我是单向消息，主要用于不怕丢失需要高吞吐的场景".getBytes());
        producer.sendOneway(message);
        producer.shutdown();
    }
}
