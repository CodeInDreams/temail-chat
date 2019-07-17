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

package com.syswin.temail.usermail.configuration;

import com.syswin.library.messaging.all.spring.MqConsumerConfig;
import com.syswin.library.messaging.all.spring.MqImplementation;
import com.syswin.library.messaging.all.spring.MqProducerConfig;
import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.core.util.MsgCompressor;
import com.syswin.temail.usermail.interfaces.DomainClearMqConsumer;
import com.syswin.temail.usermail.interfaces.UsermailMQConsumer;
import com.syswin.temail.usermail.redis.RedisUsermailAdapter;
import com.syswin.temail.usermail.rocketmq.MqClient;
import com.syswin.temail.usermail.rocketmq.RocketMqProperties;
import java.util.function.Consumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class UsermailConfiguration {

  @Bean
  @ConditionalOnProperty(name = "spring.rocketmq.receiver", havingValue = "ROCKETMQ", matchIfMissing = true)
  MqClient usermailMqClient(UsermailConfig config, UsermailMQConsumer usermailMqConsumer) {
    return new MqClient(config.mqUserMailAgentTopic, "*", config.mqTrashConsumer, config.namesrvAddr,
        usermailMqConsumer, MqClient.RocketMQModel.CLUSTERING);
  }

  @Bean
  @ConditionalOnProperty(name = "spring.rocketmq.receiver", havingValue = "ROCKETMQ", matchIfMissing = true)
  MqClient mgtMqClient(UsermailConfig config, DomainClearMqConsumer domainClearMqConsumer) {
    return new MqClient(config.mqMgtDeleteDomainTopic, "*", config.mqMgtDeleteDomainGroup, config.namesrvAddr,
        domainClearMqConsumer,
        MqClient.RocketMQModel.CLUSTERING);
  }

  @Bean
  public MsgCompressor msgCompressor() {
    return new MsgCompressor();
  }


  @Bean
  @ConditionalOnProperty(name = "spring.rocketmq.receiver", havingValue = "libraryMessage")
  MqConsumerConfig usermailConsumerConfig(UsermailConfig config, UsermailMQConsumer consumer) {
    Consumer<String> listener = consumer::consumer;
    return MqConsumerConfig.create()
        .group(config.mqTrashConsumer)
        .topic(config.mqUserMailAgentTopic)
        .listener(listener)
        .implementation(config.receiverMqType.isEmpty() ? MqImplementation.ROCKET_MQ
            : MqImplementation.valueOf(config.receiverMqType))
        .build();
  }


  @Bean
  @ConditionalOnProperty(name = "spring.rocketmq.sender", havingValue = "libraryMessage")
  MqProducerConfig usermailProducerConfig(UsermailConfig usermailConfig, RocketMqProperties rocketMqProperties) {
    return new MqProducerConfig(rocketMqProperties.getProducerGroup(),
        usermailConfig.senderMqType.isEmpty() ? MqImplementation.ROCKET_MQ
            : MqImplementation.valueOf(usermailConfig.senderMqType));
  }

  @Bean
  public IUsermailAdapter usermailAdapter(RedisTemplate redisTemplate) {
    return new RedisUsermailAdapter(redisTemplate);
  }

}