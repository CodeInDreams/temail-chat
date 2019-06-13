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
