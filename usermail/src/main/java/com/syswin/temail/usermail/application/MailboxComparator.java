package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.dto.MailboxDTO;
import java.util.Comparator;

/**
 * 根据时间戳倒排
 */
public class MailboxComparator implements Comparator<MailboxDTO> {

  @Override
  public int compare(MailboxDTO o1, MailboxDTO o2) {
    if (o1 == null || o1.getLastMsg() == null || o1.getLastMsg().getCreateTime() == null) {
      if (o2 ==  null || o2.getLastMsg() == null || o2.getLastMsg().getCreateTime() == null) {
        return -1;
      } else {
        return 1;
      }
    }
    if (o2 == null || o2.getLastMsg() == null || o2.getLastMsg().getCreateTime() == null) {
      return -1;
    }

    long r = o1.getLastMsg().getCreateTime().getTime() - o2.getLastMsg().getCreateTime().getTime();
    if (r > 0) {
      return -1;
    } else if (r < 0) {
      return 1;
    } else {
      return 0;
    }
  }
}
