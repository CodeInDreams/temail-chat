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

  /**
   * 分页清除指定时间的历史数据
   *
   * @param beforeDays 时间
   * @param batchNum 页面大小
   */
  public void deleteHistoryMsg(int beforeDays, int batchNum) {
    long dateTime = System.currentTimeMillis() - beforeDays * 24 * 60 * 60 * 1000;
    Timestamp createTime = new Timestamp(dateTime);
    this.deleteMsg(createTime, batchNum);
    this.deleteMsgReply(createTime, batchNum);
  }

  private void deleteMsg(Timestamp createTime, int batchNum) {

    int count = 0;
    do {
      count = usermailRepo.deleteMsgLessThan(createTime, batchNum);
    } while (count > 0);
  }

  private void deleteMsgReply(Timestamp createTime, int batchNum) {
    int count = 0;
    do{
      count = usermailMsgReplyRepo.deleteMsgReplyLessThan(createTime, batchNum);
    } while (count > 0);
  }

}
