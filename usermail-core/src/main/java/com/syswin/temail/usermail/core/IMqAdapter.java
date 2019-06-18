package com.syswin.temail.usermail.core;

public interface IMqAdapter {

  /**
   * 初始化方法
   */
  void init();

  /**
   * 销毁方法
   */
  void destroy();

  /**
   * 发送mq消息
   *
   * @param topic 队列topic
   * @param tag 消息tag
   * @param message 消息体
   * @return 是否发送成功
   */
  boolean sendMessage(String topic, String tag, String message);

}
