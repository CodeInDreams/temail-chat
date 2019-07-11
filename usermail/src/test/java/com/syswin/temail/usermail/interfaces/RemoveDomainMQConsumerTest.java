package com.syswin.temail.usermail.interfaces;

import com.syswin.temail.usermail.application.RemoveDomainService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RemoveDomainMQConsumerTest {

  @InjectMocks
  private RemoveDomainMQConsumer consumer;
  @Mock
  private RemoveDomainService service;
  private String message = "test.com";

  @Test
  public void consumer() {
    consumer.consumer(message);
    Mockito.verify(service).removeDomain(message);
  }
}