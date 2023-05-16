package com.wyz.rocketlecture.produce;

import com.wyz.rocketlecture.MqConstant;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.jupiter.api.Test;

/**
 * @Description: 异步消息
 * @Author: WangYouzheng
 * @Date: 2023/5/15 21:14
 * @Version: V1.0
 */
public class AsyncTest {
    @Test
    public void asyncProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("test-async-producer-group");
        producer.setNamesrvAddr(MqConstant.NAME_SRV_ADDR);
        producer.start();

        Message message = new Message("test_async_topic", "我是一个异步的消息".getBytes());
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("发送异常了哈");
            }
        });
        System.out.println("这是个异步的例子，如果不卡主，那么回调打印不出来~");

        // 卡住~
        System.in.read();
    }

}
