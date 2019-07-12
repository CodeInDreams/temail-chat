package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.infrastructure.domain.IUsermailMsgReplyDB;
import com.syswin.temail.usermail.infrastructure.domain.IUsermailMsgDB;
import java.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class HistoryMsgClearServiceTest {

  @InjectMocks
  private HistoryMsgClearService historyMsgClearService;
  @Mock
  private IUsermailMsgDB usermailMsgDB;
  @Mock
  private IUsermailMsgReplyDB usermailMsgReplyDB;

  @Test
  public void deleteHistoryMsg() {
    int beforeDays = 7;
    int batchNum = 100;
    LocalDate createTime = LocalDate.now().minusDays(beforeDays);

    historyMsgClearService.deleteHistoryMsg(beforeDays, batchNum);

    Mockito.verify(usermailMsgDB).deleteMsgLessThan(createTime, batchNum);
    Mockito.verify(usermailMsgReplyDB).deleteMsgReplyLessThan(createTime, batchNum);
  }
}