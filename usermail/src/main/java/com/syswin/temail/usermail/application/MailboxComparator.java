package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.dto.MailboxDTO;

import java.sql.Timestamp;
import java.util.Comparator;

/**
 * 根据时间戳倒排
 */
public class MailboxComparator implements Comparator<MailboxDTO> {

  @Override
  public int compare(MailboxDTO o1, MailboxDTO o2) {

    if(o1 == null) {
      if(o2 == null) {
        return 0;
      } else {
        return 1;
      }
    }
    if(o2 == null) {
      return -1;
    }

    UsermailDO lastMsg1 = o1.getLastMsg();
    UsermailDO lastMsg2 = o2.getLastMsg();
    if(lastMsg1 == null) {
      if(lastMsg2 == null) {
        return 0;
      } else {
        return 1;
      }
    }
    if(lastMsg2 == null) {
      return -1;
    }

    Timestamp createTime1 = lastMsg1.getCreateTime();
    Timestamp createTime2 = lastMsg2.getCreateTime();
    if(createTime1 == null){
      if(createTime1 == null) {
        return 0;
      } else {
        return 1;
      }
    }
    if(createTime2 == null) {
      return -1;
    }

    long r = createTime1.getTime() - createTime2.getTime();
    if (r > 0) {
      return -1;
    } else if (r < 0) {
      return 1;
    } else {
      return 0;
    }
  }
}
