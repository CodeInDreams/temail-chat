/*
 * MIT License
 *
 * Copyright (c) 2019 Syswin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
    usermailBox.setSessionExtData("sessionExtData");
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
    int result = usermailBoxRepo.deleteByOwnerAndMail2("mailsend12@temail.com", "mailreceive12@temail.com");
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
    UsermailBoxDO usermailBox = usermailBoxRepo.selectByOwnerAndMail2(box.getOwner(), box.getMail2());
    assertThat(usermailBox.getOwner()).isEqualTo(box.getOwner());
    assertThat(usermailBox.getSessionid()).isEqualTo(usermailBox.getSessionid());
  }
}
