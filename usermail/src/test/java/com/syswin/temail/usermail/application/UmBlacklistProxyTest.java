package com.syswin.temail.usermail.application;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class UmBlacklistProxyTest {

  @Test
  public void checkInBlacklist() {
    UsermailBlacklistService usermailBlacklistService = Mockito
        .mock(UsermailBlacklistService.class);
    UmBlacklistProxy umBlacklistProxy = new UmBlacklistProxy(usermailBlacklistService);
    String from = "from@syswin.com";
    String blacker = "to@syswin.com";
    umBlacklistProxy.checkInBlacklist(from, blacker);
    ArgumentCaptor<String> argumentCaptor1 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);
    verify(usermailBlacklistService)
        .isInBlacklist(argumentCaptor1.capture(), argumentCaptor2.capture());
    assertEquals(from, argumentCaptor1.getValue());
    assertEquals(blacker, argumentCaptor2.getValue());
  }
}
