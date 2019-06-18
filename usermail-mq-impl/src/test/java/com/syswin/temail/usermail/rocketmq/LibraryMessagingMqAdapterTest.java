package com.syswin.temail.usermail.rocketmq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.syswin.library.messaging.MqProducer;
import com.syswin.library.messaging.rocketmq.RocketMqProducer;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
    ReflectionTestUtils.setField(libraryMessagingMqAdapter, "rocketMqProperties", rocketMQProperties);
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

  /**
   * Before Mockito can be used for mocking final classes and methods, it needs to be configured.
   * We need to add a text file to the project’s src/test/resources/mockito-extensions directory
   * named org.mockito.plugins.MockMaker and add a single line of text:
   * mock-maker-inline
   * @throws Exception
   */
  @Test
  public void sendMessageSuccess() throws Exception {
    String topic = "topic";
    String tag = "tag";
    String message = "message";
    when(rocketMQProperties.getProducerGroup()).thenReturn(producerGroup);
    RocketMqProducer rocketMqProducer = Mockito.mock(RocketMqProducer.class);
    when(mqProducerMap.get(producerGroup)).thenReturn(rocketMqProducer);
    boolean result = libraryMessagingMqAdapter.sendMessage(topic, tag, message);
    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> keysCaptor = ArgumentCaptor.forClass(String.class);
    verify(rocketMqProducer)
        .send(messageCaptor.capture(), topicCaptor.capture(), tagCaptor.capture(), keysCaptor.capture());
    assertThat(topicCaptor.getValue()).isEqualTo(topic);
    assertThat(tagCaptor.getValue()).isEqualTo(tag);
    assertThat(messageCaptor.getValue()).isEqualTo(message);
    assertThat(keysCaptor.getValue()).isEqualTo(null);
    assertThat(result).isTrue();
  }

}