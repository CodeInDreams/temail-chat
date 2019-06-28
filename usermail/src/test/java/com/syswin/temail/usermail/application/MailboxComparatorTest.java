package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.dto.MailboxDTO;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class MailboxComparatorTest {

  @Test
  public void testCompatator() {
    UsermailDO lastMsg1 = new UsermailDO();
    lastMsg1.setId(1L);
    lastMsg1.setCreateTime(new Timestamp(100000L));
    MailboxDTO dto1 = new MailboxDTO();
    dto1.setLastMsg(lastMsg1);
    dto1.setTo("to1");

    UsermailDO lastMsg2 = new UsermailDO();
    lastMsg2.setId(2L);
    lastMsg2.setCreateTime(new Timestamp(2000000L));
    MailboxDTO dto2 = new MailboxDTO();
    dto2.setLastMsg(lastMsg2);
    dto2.setTo("to2");

    MailboxDTO dto3 = new MailboxDTO();
    dto3.setTo("to3");

    List<MailboxDTO> list = new ArrayList();
    list.add(null);
    list.add(dto1);
    list.add(null);
    list.add(dto3);
    list.add(null);
    list.add(dto2);
    list.add(null);

    Collections.sort(list, new MailboxComparator());
    for (int i = 0; i < list.size(); i++) {
      System.out.println(list.get(i));
    }
    Assert.assertEquals("to2", list.get(0).getTo());
    Assert.assertEquals("to1", list.get(1).getTo());
    Assert.assertEquals("to3", list.get(2).getTo());
    Assert.assertNull(list.get(3));
    Assert.assertNull(list.get(4));
    Assert.assertNull(list.get(5));
    Assert.assertNull(list.get(6));
  }

}
