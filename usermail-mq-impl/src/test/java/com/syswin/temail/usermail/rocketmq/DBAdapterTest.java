package com.syswin.temail.usermail.rocketmq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.syswin.temail.data.consistency.application.TemailMqSender;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DBAdapterTest {

  @InjectMocks
  private DbAdapter dbAdapter;
  @Mock
  private TemailMqSender temailMqSender;

  @Test
  public void init() throws Exception {
    dbAdapter.init();
  }

  @Test
  public void destroy() throws Exception {
    dbAdapter.destroy();
  }

  @Test
  public void sendMessage() throws Exception {

    String topic = "dbadapter-topic";
    String tag = "tag";
    String message = "dbadapter-message";
    int count = 1;
    when(temailMqSender.saveEvent(topic, tag, message)).thenReturn(count);
    final boolean result = dbAdapter.sendMessage(topic, tag, message);

    verify(temailMqSender).saveEvent(topic, tag, message);
    assertThat(result).isTrue();

  }

}