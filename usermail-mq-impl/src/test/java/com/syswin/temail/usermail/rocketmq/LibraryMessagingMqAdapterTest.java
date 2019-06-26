/*
 * MIT License
 *
 * Copyright (c) 2019 Syswin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.syswin.temail.usermail.rocketmq;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.syswin.library.messaging.MessagingException;
import com.syswin.library.messaging.MqProducer;
import com.syswin.library.messaging.rocketmq.RocketMqProducer;
import com.syswin.temail.usermail.core.exception.UserMailException;
import java.io.UnsupportedEncodingException;
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
   * Before Mockito can be used for mocking final classes and methods, it needs to be configured. We need to add a text
   * file to the project’s src/test/resources/mockito-extensions directory named org.mockito.plugins.MockMaker and add a
   * single line of text: mock-maker-inline
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

  @Test(expected = UserMailException.class)
  public void sendMessageFailThrowUnsupportedEncodingException()
      throws InterruptedException, UnsupportedEncodingException, MessagingException {
    String topic = "topic";
    String tag = "tag";
    String message = "message";
    when(rocketMQProperties.getProducerGroup()).thenReturn(producerGroup);
    RocketMqProducer rocketMqProducer = Mockito.mock(RocketMqProducer.class);
    when(mqProducerMap.get(producerGroup)).thenReturn(rocketMqProducer);
    doThrow(UnsupportedEncodingException.class).when(rocketMqProducer).send(message, topic, tag, null);
    libraryMessagingMqAdapter.sendMessage(topic, tag, message);
  }

  @Test(expected = UserMailException.class)
  public void sendMessageFailThrowInterruptedException()
      throws InterruptedException, UnsupportedEncodingException, MessagingException {
    String topic = "topic";
    String tag = "tag";
    String message = "message";
    when(rocketMQProperties.getProducerGroup()).thenReturn(producerGroup);
    RocketMqProducer rocketMqProducer = Mockito.mock(RocketMqProducer.class);
    when(mqProducerMap.get(producerGroup)).thenReturn(rocketMqProducer);
    doThrow(InterruptedException.class).when(rocketMqProducer).send(message, topic, tag, null);
    libraryMessagingMqAdapter.sendMessage(topic, tag, message);
  }


}