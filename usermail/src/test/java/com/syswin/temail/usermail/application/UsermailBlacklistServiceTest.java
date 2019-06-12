package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.domains.UsermailBlacklist;
import com.syswin.temail.usermail.infrastructure.domain.UsermailBlacklistRepo;
import org.junit.Test;
import org.mockito.Mockito;

public class UsermailBlacklistServiceTest {

  private  UsermailBlacklistRepo usermailBlacklistRepo = Mockito.mock(UsermailBlacklistRepo.class);

  private IUsermailAdapter iUsermailAdapter = Mockito.mock(IUsermailAdapter.class);

  private UsermailBlacklistService usermailBlacklistService = new UsermailBlacklistService(usermailBlacklistRepo, iUsermailAdapter);

  @Test
  public void saveTest(){
    UsermailBlacklist usermailBlacklist = new UsermailBlacklist();
    Mockito.when(iUsermailAdapter.getUsermailBlacklistPkID()).thenReturn(1l);
    usermailBlacklistService.save(usermailBlacklist);
    Mockito.verify(usermailBlacklistRepo).insert(usermailBlacklist);
  }

  @Test
  public void removeTest(){
    UsermailBlacklist usermailBlacklist = new UsermailBlacklist();
    usermailBlacklistService.remove(usermailBlacklist);
    Mockito.verify(usermailBlacklistRepo).deleteByAddresses(usermailBlacklist);
  }

  @Test
  public void findByTemailAddressTest(){
    String temail = "temail@syswin.com";
    usermailBlacklistService.findByTemailAddress(temail);
    Mockito.verify(usermailBlacklistRepo).selectByTemailAddress(temail);
  }

  @Test
  public void isInBlacklistTest(){
    String from = "from@syswin.com";
    String to = "to@syswin.com";
    usermailBlacklistService.isInBlacklist(from, to);
    Mockito.verify(usermailBlacklistRepo).countByAddresses(to, from);
  }
}
