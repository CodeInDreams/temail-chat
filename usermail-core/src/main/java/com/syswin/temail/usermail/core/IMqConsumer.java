package com.syswin.temail.usermail.core;

public interface IMqConsumer {

  /**
   * 消费mq消息
   * @param message mq消息
   * @return 消费成功 true，消费失败 false
   */
  boolean consumer(String message);

}
