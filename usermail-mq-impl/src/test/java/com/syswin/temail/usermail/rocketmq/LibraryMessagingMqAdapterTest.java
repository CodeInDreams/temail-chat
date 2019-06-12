package com.syswin.temail.usermail.rocketmq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.syswin.library.messaging.MqProducer;
import com.syswin.library.messaging.rocketmq.RocketMqProducer;
import java.util.Map;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class LibraryMessagingMqAdapterTest {

  @InjectMocks
  private LibraryMessagingMqAdapter libraryMessagingMqAdapter;
  @Mock
  private RocketMqProperties rocketMQProperties;
  @Mock(name = "mqProducers")
  private Map<String, MqProducer> mqProducerMap;
  private String producerGroup = "producerGroup";


  @Before
  public void setUp() {
    ReflectionTestUtils.setField(libraryMessagingMqAdapter, "rocketMQProperties", rocketMQProperties);
  }

  @Test
  public void init() throws Exception {
    libraryMessagingMqAdapter.init();
  }

  @Test
  public void destroy() throws Exception {
    libraryMessagingMqAdapter.destroy();
  }

  @Test
  public void sendMessageFailIfMqProducerIsNull() throws Exception {
    when(rocketMQProperties.getProducerGroup()).thenReturn(producerGroup);
    when(mqProducerMap.get(producerGroup)).thenReturn(null);

    String topic = "topic";
    String tag = "tag";
    String message = "message";
    boolean result = libraryMessagingMqAdapter.sendMessage(topic, tag, message);

    assertThat(result).isFalse();
  }

  @Ignore
  @Test
  public void sendMessageSuccess() throws Exception {
    String topic = "topic";
    String tag = "tag";
    String message = "message";
    when(rocketMQProperties.getProducerGroup()).thenReturn(producerGroup);
    RocketMqProducer rocketMqProducer = Mockito.mock(RocketMqProducer.class);
    when(mqProducerMap.get(producerGroup)).thenReturn(rocketMqProducer);

    Mockito.doNothing().when(rocketMqProducer).send(topic, tag, message, null);

    boolean result = libraryMessagingMqAdapter.sendMessage(topic, tag, message);

    assertThat(result).isTrue();
  }

}