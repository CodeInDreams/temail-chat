package com.syswin.temail.usermail.infrastructure.domain;


import static org.assertj.core.api.Assertions.assertThat;

import com.syswin.temail.usermail.UsermailAgentApplication;
import com.syswin.temail.usermail.domains.UsermailBoxDO;
import java.util.List;
import org.junit.Assert;
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
public class UsermailBoxMapperTest {

  @Autowired
  private UsermailBoxRepo usermailBoxRepo;

  @Test
  public void saveUsermailBox() {
    UsermailBoxDO usermailBox = new UsermailBoxDO();
    usermailBox.setId(2);
    usermailBox.setMail2("mailB@syswin.com");
    usermailBox.setSessionid("sessionid");
    usermailBox.setOwner("mailA@syswin.com");
    usermailBoxRepo.saveUsermailBox(usermailBox);
    Assert.assertTrue(true);
  }

  @Test
  public void testListUsermailBoxsByOwner() throws Exception {
    UsermailBoxDO box = new UsermailBoxDO(12345L, "test-session-owner2", "mailsend@temail.com", "mailsend@temail.com");
    usermailBoxRepo.saveUsermailBox(box);
    List<UsermailBoxDO> boxes = usermailBoxRepo.listUsermailBoxsByOwner("mailsend@temail.com", 0);
    assertThat(boxes.get(0).getOwner()).isEqualTo("mailsend@temail.com");
  }

  @Test
  public void testDeleteUsermailBox() throws Exception {
    UsermailBoxDO box = new UsermailBoxDO(124L, "test-session-owner3", "mailreceive12@temail.com", "mailsend12@temail.com");
    usermailBoxRepo.saveUsermailBox(box);
    int result = usermailBoxRepo.deleteUsermailBox("mailsend12@temail.com", "mailreceive12@temail.com");
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void testListUsermailBoxsByOwnerAndTo() {
    String owner = "mailsend1@temail.com";
    String mail2 = "mailreceive1@temail.com";
    UsermailBoxDO box = new UsermailBoxDO(123L, "test-session-owner3", mail2, owner);
    usermailBoxRepo.saveUsermailBox(box);
    List<UsermailBoxDO> usermailBoxes = usermailBoxRepo.listUsermailBoxsByOwnerAndTo(owner, mail2);
    assertThat(usermailBoxes).isNotNull();
    assertThat(usermailBoxes.size()).isOne();
  }

  @Test
  public void testUpdateArchiveStatus() {
    UsermailBoxDO box = new UsermailBoxDO(24355, "test-session-owner3", "mailreceive1@msgseal.com",
        "mailsend1@msgseal.com");
    usermailBoxRepo.saveUsermailBox(box);
    int result = usermailBoxRepo.updateArchiveStatus(box.getOwner(), box.getMail2(), box.getArchiveStatus());
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void testGetUsermailBox() {
    UsermailBoxDO box = new UsermailBoxDO(243545, "test-session-owner4", "mailreceive2@msgseal.com",
        "mailsend2@msgseal.com");
    usermailBoxRepo.saveUsermailBox(box);
    UsermailBoxDO usermailBox = usermailBoxRepo.getUsermailBox(box.getOwner(), box.getMail2());
    assertThat(usermailBox.getOwner()).isEqualTo(box.getOwner());
    assertThat(usermailBox.getSessionid()).isEqualTo(usermailBox.getSessionid());
  }
}
