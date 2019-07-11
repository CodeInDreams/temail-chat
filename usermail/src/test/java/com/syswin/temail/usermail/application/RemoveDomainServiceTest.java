package com.syswin.temail.usermail.application;

import static org.mockito.Mockito.verify;

import com.syswin.temail.usermail.common.Constants.UsermailAgentEventType;
import com.syswin.temail.usermail.infrastructure.domain.UsermailBlacklistRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailBoxRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailMsgReplyRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailRepo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
  @Mock
  private UsermailRepo usermailRepo;
  @Mock
  private UsermailMsgReplyRepo msgReplyRepo;
  @Mock
  private UsermailBoxRepo boxRepo;
  @Mock
  private UsermailBlacklistRepo blacklistRepo;


  @Before
  public void setUp() {
    ReflectionTestUtils.setField(removeDomainService, "pageSize", pageSize);
    ReflectionTestUtils.setField(removeDomainService, "removeEnabled", removeEnabled);
  }

  @Test
  public void removeDomainTest() {
    String domain = "domain";
    removeDomainService.removeDomain(domain);
    verify(usermailMqService).sendMqRemoveDomain(domain, UsermailAgentEventType.REMOVE_ALL_USERMAIL_7);
    verify(usermailMqService).sendMqRemoveDomain(domain, UsermailAgentEventType.REMOVE_ALL_USERMAIL_MSG_REPLY_8);
    verify(usermailMqService).sendMqRemoveDomain(domain, UsermailAgentEventType.REMOVE_ALL_USERMAIL_BLACK_LIST_9);
    verify(usermailMqService).sendMqRemoveDomain(domain, UsermailAgentEventType.REMOVE_ALL_USERMAIL_BOX_10);
  }

  @Test
  public void removeUsermailTest() {
    String domain = "domain";
    removeDomainService.removeUsermail(domain);
    verify(usermailRepo).deleteDomain(domain, pageSize);
  }

  @Test
  public void removeMsgReplyTest() {
    String domain = "domain";
    removeDomainService.removeMsgReply(domain);
    verify(msgReplyRepo).deleteDomain(domain, pageSize);
  }

  @Test
  public void removeBlackTest() {
    String domain = "domain";
    removeDomainService.removeBlack(domain);
    verify(blacklistRepo).deleteDomain(domain, pageSize);
  }

  @Test
  public void removeBoxTest() {
    String domain = "domain";
    removeDomainService.removeBox(domain);
    verify(boxRepo).deleteDomain(domain, pageSize);
  }
}