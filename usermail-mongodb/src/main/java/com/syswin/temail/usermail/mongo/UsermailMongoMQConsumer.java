package com.syswin.temail.usermail.mongo;

import com.syswin.temail.usermail.core.IMqConsumer;
import org.springframework.stereotype.Component;


@Component
public class UsermailMongoMQConsumer implements IMqConsumer {

  private UsermailMongoService mongoService;

  @Override
  public boolean consumer(String message) {
    //判断是单聊消息还是单聊回复消息
    return false;
  }
}
