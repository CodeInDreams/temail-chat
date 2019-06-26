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

import static org.mockito.Mockito.verify;

import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.domains.UsermailBlacklistDO;
import com.syswin.temail.usermail.infrastructure.domain.UsermailBlacklistRepo;
import org.junit.Test;
import org.mockito.Mockito;

public class UsermailBlacklistServiceTest {

  private UsermailBlacklistRepo usermailBlacklistRepo = Mockito.mock(UsermailBlacklistRepo.class);

  private IUsermailAdapter iUsermailAdapter = Mockito.mock(IUsermailAdapter.class);

  private UsermailBlacklistService usermailBlacklistService = new UsermailBlacklistService(usermailBlacklistRepo,
      iUsermailAdapter);

  @Test
  public void saveTest() {
    UsermailBlacklistDO usermailBlacklist = new UsermailBlacklistDO();
    Mockito.when(iUsermailAdapter.getUsermailBlacklistPkID()).thenReturn(1L);
    usermailBlacklistService.save(usermailBlacklist);
    verify(usermailBlacklistRepo).insertUsermailBlacklist(usermailBlacklist);
  }

  @Test
  public void removeTest() {
    UsermailBlacklistDO usermailBlacklist = new UsermailBlacklistDO();
    usermailBlacklistService.remove(usermailBlacklist);
    verify(usermailBlacklistRepo).deleteUsermailBlacklist(usermailBlacklist);
  }

  @Test
  public void findByTemailAddressTest() {
    String temail = "temail@syswin.com";
    usermailBlacklistService.findByTemailAddress(temail);
    verify(usermailBlacklistRepo).listUsermailBlacklists(temail);
  }

  @Test
  public void findByAddress() {
    String temailAddress = "temail@syswin.com";
    String blackedAddress = "blackAddress@syswin.com";
    usermailBlacklistService.findByAddresses(temailAddress, blackedAddress);
    verify(usermailBlacklistRepo).getUsermailBlacklist(temailAddress, blackedAddress);
  }

  @Test
  public void isInBlacklistTest() {
    String from = "from@syswin.com";
    String to = "to@syswin.com";
    usermailBlacklistService.isInBlacklist(from, to);
    verify(usermailBlacklistRepo).countByAddresses(to, from);
  }
}
