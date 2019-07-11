package com.syswin.temail.usermail.interfaces;

import com.syswin.temail.usermail.application.DomainClearService;
import com.syswin.temail.usermail.core.IMqConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DomainClearMqConsumer implements IMqConsumer {

  @Autowired
  private DomainClearService domainClearService;

  @Override
  public boolean consumer(String domain) {
    log.info("label-DomainClearMqConsumer.consumer() receive message: [{}]", domain);
    domainClearService.clearDomainAll(domain);
    return true;
  }
}
