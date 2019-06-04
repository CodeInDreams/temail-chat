package com.syswin.temail.usermail.core;

public interface IMqAdapter {

  void init();

  void destroy();

  boolean sendMessage(String topic, String tag, String message);

}
