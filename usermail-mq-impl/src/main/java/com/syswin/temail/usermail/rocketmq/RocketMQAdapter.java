package com.syswin.temail.usermail.rocketmq;

import com.syswin.temail.usermail.core.IMqAdapter;
import com.syswin.temail.usermail.core.exception.UserMailException;
import java.util.UUID;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StopWatch;

public class RocketMQAdapter implements IMqAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(RocketMQAdapter.class);

  //生产者的组名
  private DefaultMQProducer producer;

  RocketMQAdapter(String productGroup, String namesrvAddr) {
    producer = new DefaultMQProducer(productGroup);
    //指定NameServer地址，多个地址以 ; 隔开
    producer.setNamesrvAddr(namesrvAddr);
    producer.setInstanceName(UUID.randomUUID().toString());
  }

  /**
   * Producer对象在使用之前必须要调用start初始化，初始化一次即可 注意：切记不可以在每次发送消息时，都调用start方法
   */
  @Override
  public void init() {
    try {
      producer.start();
      LOGGER.info("RocketMQAdapter started");
    } catch (MQClientException e) {
      throw new UserMailException("rocketmq start error:", e);
    }
  }

  @Override
  public void destroy() {
    if (producer != null) {
      producer.shutdown();
      LOGGER.info("RocketMQAdapter shutdowned");
    }
  }

  @Override
  public boolean sendMessage(String topic, String tag, String message) {
    LOGGER.info("RocketMQAdapter send message topic=[{}], tag=[{}], message=[{}]", topic, tag, message);
    //创建一个消息实例，包含 topic、tag 和 消息体
    //如下：topic 为 "TopicTest"，tag 为 "push"
    Message mqMessage = new Message(topic, tag, (message).getBytes());
    StopWatch stop = new StopWatch();
    try {
      stop.start();
      SendResult result;
      if (tag == null) {
        result = producer.send(mqMessage);
      } else {
        result = producer.send(mqMessage, (mqs, msg, arg) -> {
          int index = Math.abs(arg.hashCode() % mqs.size());
          return mqs.get(index);
        }, tag);
      }
      LOGGER.info("MQ: send result: {}", result);
      if (result.getSendStatus().equals(SendStatus.SEND_OK)) {
        return true;
      } else {
        LOGGER.error("mq send message FAILURE,topic=[{}],message=[{}]", topic, message);
        return false;
      }
    } catch (Exception e) {
      LOGGER.error("mq send message error,topic=[{}],message=[{}]", topic, message, e);
      throw new UserMailException(e);
    } finally {
      stop.stop();
    }
  }
}
