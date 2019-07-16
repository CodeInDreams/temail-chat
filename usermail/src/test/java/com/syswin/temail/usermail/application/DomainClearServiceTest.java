package com.syswin.temail.usermail.application;

import static org.mockito.Mockito.verify;

import com.syswin.temail.usermail.common.Constants.UsermailAgentEventType;
import com.syswin.temail.usermail.infrastructure.domain.IUsermailBlacklistDB;
import com.syswin.temail.usermail.infrastructure.domain.IUsermailMsgDB;
import com.syswin.temail.usermail.infrastructure.domain.IUsermailMsgReplyDB;
import com.syswin.temail.usermail.infrastructure.domain.UsermailBoxRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class DomainClearServiceTest {

  private Integer pageSize = 100;
  private String enabled = "true";

  @InjectMocks
  private DomainClearService domainClearService;
  @Mock
  private UsermailMqService usermailMqService;
  @Mock
  private IUsermailMsgDB usermailMsgDB;
  @Mock
  private IUsermailMsgReplyDB msgReplyRepo;
  @Mock
  private UsermailBoxRepo boxRepo;
  @Mock
  private IUsermailBlacklistDB usermailBlacklistDB;


  @Before
  public void setUp() {
    ReflectionTestUtils.setField(domainClearService, "pageSize", pageSize);
    ReflectionTestUtils.setField(domainClearService, "enabled", enabled);
  }

  @Test
  public void removeDomainTest() {
    String domain = "domain";
    domainClearService.clearDomainAll(domain);
    verify(usermailMqService).sendMqClearDomain(domain, UsermailAgentEventType.CLEAR_ALL_USERMAIL_7);
    verify(usermailMqService).sendMqClearDomain(domain, UsermailAgentEventType.CLEAR_ALL_USERMAIL_MSG_REPLY_8);
    verify(usermailMqService).sendMqClearDomain(domain, UsermailAgentEventType.CLEAR_ALL_USERMAIL_BLACK_LIST_9);
    verify(usermailMqService).sendMqClearDomain(domain, UsermailAgentEventType.CLEAR_ALL_USERMAIL_BOX_10);
  }

  @Test
  public void removeUsermailTest() {
    String domain = "domain";
    domainClearService.clearUsermailAll(domain);
    verify(usermailMsgDB).deleteDomain(domain, pageSize);
  }

  @Test
  public void removeMsgReplyTest() {
    String domain = "domain";
    domainClearService.clearMsgReplyAll(domain);
    verify(msgReplyRepo).deleteDomain(domain, pageSize);
  }

  @Test
  public void removeBlackTest() {
    String domain = "domain";
    domainClearService.clearBlackAll(domain);
    verify(usermailBlacklistDB).deleteDomain(domain, pageSize);
  }

  @Test
  public void removeBoxTest() {
    String domain = "domain";
    domainClearService.clearBoxAll(domain);
    verify(boxRepo).deleteDomain(domain, pageSize);
  }
}