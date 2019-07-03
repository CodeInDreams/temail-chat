package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.infrastructure.domain.UsermailBlacklistRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailBoxRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailMsgReplyRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class RemoveDomainServiceTest {

  private Integer pageSize = 100;

  @InjectMocks
  private RemoveDomainService removeDomainService;
  @Mock
  private UsermailRepo usermailRepo;
  @Mock
  private UsermailMsgReplyRepo usermailMsgReplyRepo;
  @Mock
  private UsermailBlacklistRepo usermailBlacklistRepo;
  @Mock
  private UsermailBoxRepo usermailBoxRepo;

  @Before
  public void setUp () {
    ReflectionTestUtils.setField(removeDomainService, "pageSize", pageSize);
  }

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