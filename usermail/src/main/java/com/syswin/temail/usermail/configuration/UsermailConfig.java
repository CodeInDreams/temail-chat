package com.syswin.temail.usermail.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UsermailConfig {

  /**
   * 发送给通知服务的topic
   */
  @Value("${spring.rocketmq.topic.notify}")
  public String mqTopic = "temail-usermail";

  /**
   * 自消费的topic
   */
  @Value("${spring.rocketmq.topic.usermail}")
  public String mqUserMailAgentTopic = "temail-usermailagent";

  @Value("${spring.rocketmq.consumer-group}")
  public String mqTrashConsumer = "temail-usermail-consumer";

  @Value("${spring.rocketmq.host}")
  public String namesrvAddr;


  /**
   * libraryMessage配置
   */
  @Value("${spring.rocketmq.senderType:}")
  public String mqType;
}
