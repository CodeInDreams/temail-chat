package com.syswin.temail.usermail.rocketmq;

import com.syswin.library.messaging.MqProducer;
import com.syswin.temail.data.consistency.application.TemailMqSender;
import com.syswin.temail.usermail.core.IMqAdapter;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RocketMqProperties.class)
public class RocketMqAutoConfiguration {

  private static final Logger LOGGER = LoggerFactory.getLogger(RocketMqAutoConfiguration.class);

  @Bean(initMethod = "init", destroyMethod = "destroy")
  @ConditionalOnProperty(name = "spring.rocketmq.sender", havingValue = "ROCKETMQ")
  public IMqAdapter imq(RocketMqProperties rocketMqProperties) {
    LOGGER.info("MQAdapter [rocketmq] started");
    return new RocketMqAdapter(rocketMqProperties.getProducerGroup(), rocketMqProperties.getHost());
  }

  @Bean
  @ConditionalOnProperty(name = "spring.rocketmq.sender", matchIfMissing = true, havingValue = "DB")
  public IMqAdapter dbImq(TemailMqSender temailMqSender) {
    LOGGER.info("MQAdapter [DB] started");
    return new DbAdapter(temailMqSender);
  }

  @Bean
  @ConditionalOnProperty(name = "spring.rocketmq.sender", havingValue = "libraryMessage")
  public IMqAdapter libraryMessagingMqAdapter(Map<String, MqProducer> mqProducers,
      RocketMqProperties rocketMqProperties) {
    LOGGER.info("MQAdapter [libraryMessage] started");
    return new LibraryMessagingMqAdapter(mqProducers, rocketMqProperties);
  }

}
