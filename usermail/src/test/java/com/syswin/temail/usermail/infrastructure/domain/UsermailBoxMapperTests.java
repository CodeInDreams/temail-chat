package com.syswin.temail.usermail.infrastructure.domain;


import static org.assertj.core.api.Assertions.assertThat;

import com.syswin.temail.usermail.UsermailAgentApplication;
import com.syswin.temail.usermail.domains.UsermailBox;
import com.syswin.temail.usermail.domains.UsermailBoxRepo;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(UsermailAgentApplication.class)
@ActiveProfiles("test")
public class UsermailBoxMapperTests {

  @Autowired
  private UsermailBoxRepo usermailBoxRepo;

  @Test
  public void saveUsermailBox() {
    UsermailBox usermailBox = new UsermailBox();
    usermailBox.setId(2);
    usermailBox.setMail2("mailB@syswin.com");
    usermailBox.setSessionid("sessionid");
    usermailBox.setOwner("mailA@syswin.com");
    usermailBoxRepo.saveUsermailBox(usermailBox);
    Assert.assertTrue(true);
  }


  @Test
  public void getBySessionid() throws Exception {
    UsermailBox box = new UsermailBox(1234L, "test-session-owner1", "mailB@temail.com", "mailA@temail.com");
    usermailBoxRepo.saveUsermailBox(box);
    UsermailBox resutbox = usermailBoxRepo.selectUsermailBox("mailA@temail.com", "mailB@temail.com");
    assertThat(resutbox.getSessionid()).isEqualTo("test-session-owner1");
  }

  @Test
  public void getUsermailBoxByOwner() throws Exception {
    UsermailBox box = new UsermailBox(12345L, "test-session-owner2", "mailsend@temail.com", "mailsend@temail.com");
    usermailBoxRepo.saveUsermailBox(box);
    List<UsermailBox> boxes = usermailBoxRepo.getUsermailBoxByOwner("mailsend@temail.com", 0);
    assertThat(boxes.get(0).getOwner()).isEqualTo("mailsend@temail.com");
  }

  @Test
  public void deleteByOwnerAndTo() throws Exception {
    UsermailBox box = new UsermailBox(123L, "test-session-owner3", "mailreceive1@temail.com", "mailsend1@temail.com");
    usermailBoxRepo.saveUsermailBox(box);
    int result = usermailBoxRepo.deleteByOwnerAndTo("mailsend1@temail.com", "mailreceive1@temail.com");
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void updateArchiveStatus() {
    UsermailBox box = new UsermailBox(24355, "test-session-owner3", "mailreceive1@msgseal.com",
        "mailsend1@msgseal.com");
    usermailBoxRepo.saveUsermailBox(box);
    int result = usermailBoxRepo.updateArchiveStatus(box.getOwner(), box.getMail2(), box.getArchiveStatus());
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void selectUsermailBox() {
    UsermailBox box = new UsermailBox(243545, "test-session-owner4", "mailreceive2@msgseal.com",
        "mailsend2@msgseal.com");
    usermailBoxRepo.saveUsermailBox(box);
    UsermailBox usermailBox = usermailBoxRepo.selectUsermailBox(box.getOwner(), box.getMail2());
    assertThat(usermailBox.getOwner()).isEqualTo(box.getOwner());
    assertThat(usermailBox.getSessionid()).isEqualTo(usermailBox.getSessionid());
  }
}
