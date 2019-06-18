package com.syswin.temail.usermail.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.syswin.temail.usermail.common.ResultCodeEnum;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UmBlacklistProxyTest {

  @InjectMocks
  private UmBlacklistProxy umBlacklistProxy;
  @Mock
  private UsermailBlacklistService usermailBlacklistService;

  @Test
  public void checkInBlacklistExpectNotExist() {
    final String from = "from@syswin.com";
    final String blacker = "to@syswin.com";
    final int count = 0;
    Mockito.when(usermailBlacklistService.isInBlacklist(from, blacker)).thenReturn(count);

    umBlacklistProxy.checkInBlacklist(from, blacker);
    verify(usermailBlacklistService).isInBlacklist(from, blacker);
  }

  @Test(expected = IllegalGmArgsException.class)
  public void checkInBlacklistExceptExist() {
    final int count = 1;
    final String from = "from@syswin.com";
    final String blacker = "blacklist@syswin.com";
    when(usermailBlacklistService.isInBlacklist(from, blacker)).thenReturn(count);
    try {
      umBlacklistProxy.checkInBlacklist(from, blacker);
    } catch (IllegalGmArgsException e) {
      assertThat(e.getResultCode()).isEqualTo(ResultCodeEnum.ERROR_IN_BLACKLIST);
      throw e;
    }
  }
}
