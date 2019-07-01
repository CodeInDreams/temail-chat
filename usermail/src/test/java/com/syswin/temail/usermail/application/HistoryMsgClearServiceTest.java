package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.infrastructure.domain.UsermailMsgReplyRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailRepo;
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
  private UsermailRepo usermailRepo;
  @Mock
  private UsermailMsgReplyRepo usermailMsgReplyRepo;

  @Test
  public void deleteHistoryMsg() {
    int beforeDays = 7;
    int batchNum = 100;
    LocalDate createTime = LocalDate.now().minusDays(beforeDays);

    historyMsgClearService.deleteHistoryMsg(beforeDays, batchNum);

    Mockito.verify(usermailRepo).deleteMsgLessThan(createTime, batchNum);
    Mockito.verify(usermailMsgReplyRepo).deleteMsgReplyLessThan(createTime, batchNum);
  }
}