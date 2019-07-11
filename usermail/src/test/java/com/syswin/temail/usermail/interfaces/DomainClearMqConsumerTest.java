package com.syswin.temail.usermail.interfaces;

import com.syswin.temail.usermail.application.DomainClearService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DomainClearMqConsumerTest {

  @InjectMocks
  private DomainClearMqConsumer consumer;
  @Mock
  private DomainClearService service;
  private String message = "test.com";

  @Test
  public void consumer() {
    consumer.consumer(message);
    Mockito.verify(service).clearDomainAll(message);
  }
}