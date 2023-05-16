### 生产者示例
1. 普通消息 SimpleMQ
2. 异步消息发送 AsyncTest
3. 单向消息 oneWay 无需确认消息的响应状态
4. 延迟消费消息 Delay message

### 消费模式
> RocketMQ是 pull 拉模式。

* 并发消费
默认情况下 保证队列是局部有序，默认会重试16次，如果一直不给success，那么会进入死信队列
* 顺序消费，保证topic全局有序，无限重试（Integer.MAX）
