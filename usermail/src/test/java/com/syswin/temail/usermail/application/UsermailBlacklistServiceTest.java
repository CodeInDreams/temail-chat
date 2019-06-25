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
