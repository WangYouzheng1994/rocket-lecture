package com.wyz.rocketlecture;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.jupiter.api.Test;

/**
 * @Description:
 * @Author: WangYouzheng
 * @Date: 2023/5/15 21:14
 * @Version: V1.0
 */
public class AsyncTest {
    @Test
    public void asyncProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer();
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();

        Message message = new Message();
    }

}
