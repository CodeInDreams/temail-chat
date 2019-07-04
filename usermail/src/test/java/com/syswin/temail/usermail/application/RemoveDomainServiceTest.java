package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.common.Constants.UsermailAgentEventType;
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
  private String removeEnabled = "true";

  @InjectMocks
  private RemoveDomainService removeDomainService;
  @Mock
  private UsermailMqService usermailMqService;

  @Before
  public void setUp () {
    ReflectionTestUtils.setField(removeDomainService, "pageSize", pageSize);
    ReflectionTestUtils.setField(removeDomainService, "removeEnabled", removeEnabled);
  }

  @Test
  public void removeDomain() {
    String domain = "domain";
    removeDomainService.removeDomain(domain);
    Mockito.verify(usermailMqService).sendMqRemoveDomain(domain, UsermailAgentEventType.REMOVE_ALL_USERMAIL_7);
    Mockito.verify(usermailMqService).sendMqRemoveDomain(domain, UsermailAgentEventType.REMOVE_ALL_USERMAIL_MSG_REPLY_8);
    Mockito.verify(usermailMqService).sendMqRemoveDomain(domain, UsermailAgentEventType.REMOVE_ALL_USERMAIL_BLACK_LIST_9);
    Mockito.verify(usermailMqService).sendMqRemoveDomain(domain, UsermailAgentEventType.REMOVE_ALL_USERMAIL_BOX_10);
  }
}