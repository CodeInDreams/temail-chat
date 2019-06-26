/*
 * MIT License
 *
 * Copyright (c) 2019 Syswin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.syswin.temail.usermail.rocketmq;

import com.syswin.temail.usermail.core.IMqConsumer;
import com.syswin.temail.usermail.core.exception.UserMailException;
import javax.annotation.PostConstruct;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(MqClient.class);

  private String topic;
  private String tag;
  private String groupName;
  private String namesrvAddr;
  private IMqConsumer imqConsumer;
  private MessageModel messageModel;

  public MqClient(String topic, String tag, String groupName, String namesrvAddr, IMqConsumer imqConsumer, RocketMQModel mqModelType) {
    this.topic = topic;
    this.tag = tag;
    this.groupName = groupName;
    this.namesrvAddr = namesrvAddr;
    this.imqConsumer = imqConsumer;
    if (mqModelType == RocketMQModel.BROADCASTING) {
      this.messageModel = MessageModel.BROADCASTING;
    } else {
      this.messageModel = MessageModel.CLUSTERING;
    }

  }

  @PostConstruct
  public void defaultMQConsumer() {
    DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupName);
    consumer.setNamesrvAddr(namesrvAddr);
    consumer.setMessageModel(messageModel);
    consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
    try {
      consumer.subscribe(topic, tag);
      consumer.registerMessageListener(
          (MessageListenerConcurrently) (messageExts, context) -> {
            try {
              for (MessageExt messageExt : messageExts) {
                LOGGER.info("MQ: MsgId={} Topic={} Tags={} Keys={}", messageExt.getMsgId(), messageExt.getTopic(), messageExt.getTags(),
                    messageExt.getKeys());
                String message = new String(messageExt.getBody());
                imqConsumer.consumer(message);
              }
            } catch (UserMailException e) {
              LOGGER.error("MQ args execption", e);
              return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            } catch (Exception e) {
              LOGGER.error("MQ consumer execption", e);
              // 稍后再试
              return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            // 消费成功
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
          });
      consumer.start();
    } catch (MQClientException e) {
      LOGGER.error("MQ delete action init error.");
    }

  }

  public enum RocketMQModel {
    CLUSTERING, BROADCASTING
  }
}
