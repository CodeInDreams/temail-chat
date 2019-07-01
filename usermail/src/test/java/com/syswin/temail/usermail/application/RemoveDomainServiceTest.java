package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.infrastructure.domain.UsermailBlacklistRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailBoxRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailMsgReplyRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RemoveDomainServiceTest {

  @InjectMocks
  private RemoveDomainService removeDomainService;
  private Integer pageSize = 100;
  @Mock
  private UsermailRepo usermailRepo;
  @Mock
  private UsermailMsgReplyRepo usermailMsgReplyRepo;
  @Mock
  private UsermailBlacklistRepo usermailBlacklistRepo;
  @Mock
  private UsermailBoxRepo usermailBoxRepo;

  @Test
  public void removeDomain() {
    String domain = "domain";
    removeDomainService.removeDomain(domain);
    Mockito.verify(usermailRepo).removeDomain(domain, pageSize);
    Mockito.verify(usermailMsgReplyRepo).removeDomain(domain, pageSize);
    Mockito.verify(usermailBlacklistRepo).removeDomain(domain, pageSize);
    Mockito.verify(usermailBoxRepo).removeDomain(domain, pageSize);
  }
}