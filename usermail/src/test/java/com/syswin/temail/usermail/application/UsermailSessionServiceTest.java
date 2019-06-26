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

package com.syswin.temail.usermail.application;

import org.junit.Assert;
import org.junit.Test;

public class UsermailSessionServiceTest {

  @Test
  public void getSessionID() {
    String from = "from@syswin.com";
    String to = "to@syswin.com";
    UsermailSessionService usermailSessionService = new UsermailSessionService();
    String sessionID = usermailSessionService.getSessionID(from, to);
    Assert.assertNotNull(sessionID);
    from = "test5@syswin,com";
    to = "test1@syswin,com";
    String finalSessionId = usermailSessionService.getSessionID(from, to);
    Assert.assertNotNull(finalSessionId);
    String fromNullSessionId = null;
    String toNullSessionId = null;
    try {
      from = null;
      fromNullSessionId = usermailSessionService.getSessionID(from, to);
    } catch (IllegalArgumentException e) {}
    Assert.assertNull(fromNullSessionId);
    from = "from";
    to = null;
    try {
      toNullSessionId = usermailSessionService.getSessionID(from, to);
    } catch (IllegalArgumentException e) {}
    Assert.assertNull(toNullSessionId);

  }

}
