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

* 重复消费问题
- 同一消费者组，可以避免重复消费（和kafka一样），一个组多个消费者，会进行负载均衡（kafka
做的是同一个组的不同的消费者独占同一个topic的不同的partition分区）


### messageId (kafka目标是做partition，有key就分区，否则用轮循)
rocketMQ 中的msgid是给消息指定一个唯一索引标记。可以客户端去重以及后续的进行查询检索
1. 默认有一个messageid
2. 可以手动指定key

RocketMQ是At least once的模式，没有一定一直

RocketMQ会有一种消息重复的情况，生产者多次投递了以后，紧接着消费者由扩容了，这样这个消费者组的两个消费者可能存在跑两次的情况。

RocketMQ官方建议的幂等方式在消费端借助msgId来处理。
