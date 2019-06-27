package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.infrastructure.domain.UsermailMsgReplyRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailRepo;
import java.sql.Timestamp;
import org.springframework.stereotype.Service;

@Service
public class HistoryMsgClearService {

  private final UsermailRepo usermailRepo;
  private final UsermailMsgReplyRepo usermailMsgReplyRepo;

  public HistoryMsgClearService(UsermailRepo usermailRepo, UsermailMsgReplyRepo usermailMsgReplyRepo) {
    this.usermailRepo = usermailRepo;
    this.usermailMsgReplyRepo = usermailMsgReplyRepo;
  }

  public void deleteHistoryMsg(int beforeDays, int batchNum) {
    long dateTime = System.currentTimeMillis() - beforeDays * 24 * 60 * 60 * 1000;
    Timestamp createTime = new Timestamp(dateTime);
    this.deleteMsg(createTime, batchNum);
    this.deleteMsgReply(createTime, batchNum);
  }

  private void deleteMsg(Timestamp createTime, int batchNum) {
    while (batchNum > 0) {
      int deleteCount = usermailRepo.deleteMsgLessThan(createTime, batchNum);
      batchNum = deleteCount > 0 ? batchNum : 0;
    }
  }

  private void deleteMsgReply(Timestamp createTime, int batchNum) {
    while (batchNum > 0) {
      int deleteCount = usermailMsgReplyRepo.deleteMsgReplyLessThan(createTime, batchNum);
      batchNum = deleteCount > 0 ? batchNum : 0;
    }
  }

}
