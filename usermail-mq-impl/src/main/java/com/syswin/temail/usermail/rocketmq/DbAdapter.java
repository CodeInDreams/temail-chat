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

import com.syswin.temail.data.consistency.application.TemailMqSender;
import com.syswin.temail.usermail.core.IMqAdapter;

public class DbAdapter implements IMqAdapter {

  private TemailMqSender temailMqSender;

  public DbAdapter(TemailMqSender temailMqSender) {
    this.temailMqSender = temailMqSender;
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
    int count = temailMqSender.saveEvent(topic, tag, message);
    return count > 0;
  }
}
