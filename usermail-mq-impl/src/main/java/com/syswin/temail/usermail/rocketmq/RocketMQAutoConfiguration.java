package com.syswin.temail.usermail.rocketmq;

import com.syswin.library.messaging.MqProducer;
import com.syswin.temail.usermail.core.IMqAdapter;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
public class RocketMQAutoConfiguration {

  private static final Logger logger = LoggerFactory.getLogger(RocketMQAutoConfiguration.class);

  @Bean(initMethod = "init", destroyMethod = "destroy")
  @ConditionalOnProperty(name = "spring.rocketmq.sender", havingValue = "ROCKETMQ")
  public IMqAdapter imq(RocketMQProperties rocketMQProperties) {
    logger.info("MQAdapter [rocketmq] started");
    return new RocketMQAdapter(rocketMQProperties.getProducerGroup(), rocketMQProperties.getHost());
  }

  @Bean
  @ConditionalOnProperty(name = "spring.rocketmq.sender", matchIfMissing = true, havingValue = "DB")
  public IMqAdapter dbImq() {
    logger.info("MQAdapter [DB] started");
    return new DBAdapter();
  }

  @Bean
  @ConditionalOnProperty(name = "spring.rocketmq.sender", havingValue = "libraryMessage")
  public IMqAdapter libraryMessagingMqAdapter(Map<String, MqProducer> mqProducers) {
    logger.info("MQAdapter [libraryMessage] started");
    return new LibraryMessagingMqAdapter(mqProducers);
  }

}
