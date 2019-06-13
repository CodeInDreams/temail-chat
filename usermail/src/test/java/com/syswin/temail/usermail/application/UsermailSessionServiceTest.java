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
  }

}
