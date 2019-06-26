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

import com.syswin.library.messaging.MessagingException;
import com.syswin.library.messaging.MqProducer;
import com.syswin.temail.usermail.core.IMqAdapter;
import com.syswin.temail.usermail.core.exception.UserMailException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LibraryMessagingMqAdapter implements IMqAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(LibraryMessagingMqAdapter.class);
  private final Map<String, MqProducer> rocketMqProducers;
  private final RocketMqProperties rocketMqProperties;

  public LibraryMessagingMqAdapter(Map<String, MqProducer> rocketMqProducers, RocketMqProperties rocketMqProperties) {
    this.rocketMqProducers = rocketMqProducers;
    this.rocketMqProperties = rocketMqProperties;
  }

  @Override
  public void init() {
    // Do nothing
  }

  @Override
  public void destroy() {
    // Do nothing
  }

  @Override
  public boolean sendMessage(String topic, String tag, String message) {
    MqProducer mqProducer = rocketMqProducers.get(rocketMqProperties.getProducerGroup());
    if (mqProducer == null) {
      LOGGER.debug("no mq producer!");
      return false;
    }
    try {
      mqProducer.send(message, topic, tag, null);
    } catch (UnsupportedEncodingException | MessagingException e) {
      LOGGER.error("send message error", e);
      throw new UserMailException(e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      LOGGER.error("thread interrupted error", e);
      throw new UserMailException(e);
    }
    return true;
  }

}
