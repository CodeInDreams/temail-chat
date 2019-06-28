package com.syswin.temail.usermail.interfaces;

import com.syswin.temail.usermail.application.RemoveDomainService;
import com.syswin.temail.usermail.core.IMqConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ManageBackgroundMQConsumer implements IMqConsumer {

  @Autowired
  private RemoveDomainService removeDomainService;

  @Override
  public boolean consumer(String message) {

    // 转换message

    String domain = "@test.com";
    removeDomainService.removeDomain(domain);

    return false;
  }
}
