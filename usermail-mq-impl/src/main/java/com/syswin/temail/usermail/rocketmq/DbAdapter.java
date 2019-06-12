package com.syswin.temail.usermail.rocketmq;

import com.syswin.temail.data.consistency.application.TemailMqSender;
import com.syswin.temail.usermail.core.IMqAdapter;

public class DbAdapter implements IMqAdapter {

  private TemailMqSender temailMqSender;

  public DbAdapter(TemailMqSender temailMqSender) {
    this.temailMqSender = temailMqSender;
  }

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
