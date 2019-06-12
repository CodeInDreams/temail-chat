package com.syswin.temail.usermail.rocketmq;

import com.syswin.library.messaging.MessagingException;
import com.syswin.library.messaging.MqProducer;
import com.syswin.temail.usermail.core.IMqAdapter;
import com.syswin.temail.usermail.core.exception.UserMailException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LibraryMessagingMqAdapter implements IMqAdapter {

  private final Logger LOGGER = LoggerFactory.getLogger(LibraryMessagingMqAdapter.class);
  private final Map<String, MqProducer> rocketMqProducers;
  private final RocketMqProperties rocketMqProperties;

  public LibraryMessagingMqAdapter(Map<String, MqProducer> rocketMqProducers, RocketMqProperties rocketMqProperties) {
    this.rocketMqProducers = rocketMqProducers;
    this.rocketMqProperties = rocketMqProperties;
  }

  @Override
  public void init() {

  }

  @Override
  public void destroy() {

  }

  @Override
  public boolean sendMessage(String topic, String tag, String message) {
    MqProducer mqProducer = rocketMqProducers.get(rocketMqProperties.getProducerGroup());
    if (mqProducer == null) {
      LOGGER.debug("no mq producer!");
      return false;
    }
    try {
      mqProducer.send(message, topic, tag, null);
    } catch (UnsupportedEncodingException | InterruptedException | MessagingException e) {
      LOGGER.error("send message error", e);
      throw new UserMailException(e);
    }
    return true;
  }

}
