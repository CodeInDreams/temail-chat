package com.syswin.temail.usermail.interfaces;

import com.syswin.temail.usermail.application.RemoveDomainService;
import com.syswin.temail.usermail.core.IMqConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RemoveDomainMQConsumer implements IMqConsumer {

  @Autowired
  private RemoveDomainService removeDomainService;

  @Override
  public boolean consumer(String domain) {
    removeDomainService.removeDomain(domain);
    return true;
  }
}
