package com.syswin.temail.usermail.configuration;

import com.syswin.library.messaging.all.spring.MqConsumerConfig;
import com.syswin.library.messaging.all.spring.MqImplementation;
import com.syswin.library.messaging.all.spring.MqProducerConfig;
import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.core.util.MsgCompressor;
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
  MqClient mqclient(UsermailConfig config, UsermailMQConsumer usermailMQConsumer) {
    return new MqClient(config.mqUserMailAgentTopic, "*", config.mqTrashConsumer,
        config.namesrvAddr, usermailMQConsumer, MqClient.RocketMQModel.CLUSTERING);
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
        .implementation(config.mqType.isEmpty() ? MqImplementation.ROCKET_MQ : MqImplementation.valueOf(config.mqType))
        .build();
  }

  @Bean
  @ConditionalOnProperty(name = "spring.rocketmq.sender", havingValue = "libraryMessage")
  MqProducerConfig usermailProducerConfig(UsermailConfig usermailConfig, RocketMqProperties rocketMqProperties) {
    return new MqProducerConfig(rocketMqProperties.getProducerGroup(),
        usermailConfig.mqType.isEmpty() ? MqImplementation.ROCKET_MQ : MqImplementation.valueOf(usermailConfig.mqType));
  }

  @Bean
  public IUsermailAdapter usermailAdapter(RedisTemplate redisTemplate) {
    return new RedisUsermailAdapter(redisTemplate);
  }


}
