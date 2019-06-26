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
