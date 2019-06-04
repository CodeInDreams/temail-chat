package com.syswin.temail.usermail.rocketmq;

import com.syswin.temail.data.consistency.application.TemailMqSender;
import com.syswin.temail.usermail.core.IMqAdapter;
import org.springframework.beans.factory.annotation.Autowired;

public class DBAdapter implements IMqAdapter {


  @Autowired
  private TemailMqSender temailMqSender;

  @Override
  public void init() {

  }

  @Override
  public void destroy() {

  }

  @Override
  public boolean sendMessage(String topic, String tag, String message) {
    int count = temailMqSender.saveEvent(topic, tag, message);
    return count > 0;
  }
}
