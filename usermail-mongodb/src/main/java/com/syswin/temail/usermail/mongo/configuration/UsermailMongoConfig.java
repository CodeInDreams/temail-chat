package com.syswin.temail.usermail.mongo.configuration;

import com.syswin.library.messaging.all.spring.MqConsumerConfig;
import com.syswin.library.messaging.all.spring.MqImplementation;
import com.syswin.temail.usermail.mongo.UsermailMongoMQConsumer;
import com.syswin.temail.usermail.rocketmq.MqClient;
import java.util.function.Consumer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsermailMongoConfig {


  @Bean
  @ConditionalOnProperty(name = "spring.rocketmq.receiver", havingValue = "ROCKETMQ", matchIfMissing = true)
  MqClient mongoMqClient(UsermailMongoProperties properties, UsermailMongoMQConsumer mongoMQConsumer) {
    return new MqClient(properties.mongoTopic, "*", properties.mongoGroup, properties.namesrvAddr,
        mongoMQConsumer,
        MqClient.RocketMQModel.CLUSTERING);
  }

  @Bean
  @ConditionalOnProperty(name = "spring.rocketmq.receiver", havingValue = "libraryMessage")
  MqConsumerConfig usermailMongoConsumerConfig(UsermailMongoProperties properties, UsermailMongoMQConsumer consumer) {
    Consumer<String> listener = consumer::consumer;
    return MqConsumerConfig.create()
        .group(properties.mongoGroup).topic(properties.mongoTopic).listener(listener)
        .implementation(properties.receiverMqType.isEmpty() ? MqImplementation.ROCKET_MQ
            : MqImplementation.valueOf(properties.receiverMqType))
        .build();
  }

}
