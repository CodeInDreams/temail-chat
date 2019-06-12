package com.syswin.temail.usermail.rocketmq;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.rocketmq")
public class RocketMqProperties {

  private String host;
  private String producerGroup;
  private String topic;

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getProducerGroup() {
    return producerGroup;
  }

  public void setProducerGroup(String producerGroup) {
    this.producerGroup = producerGroup;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }
}
