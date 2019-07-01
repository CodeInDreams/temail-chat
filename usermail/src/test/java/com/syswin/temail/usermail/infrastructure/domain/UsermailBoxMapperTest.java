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
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailBoxMapper;
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
  private UsermailBoxMapper usermailBoxMapper;

  @Test
  public void saveUsermailBox() {
    UsermailBoxDO usermailBox = new UsermailBoxDO();
    usermailBox.setId(2);
    usermailBox.setMail2("mailB@syswin.com");
    usermailBox.setSessionid("sessionid");
    usermailBox.setOwner("mailA@syswin.com");
    usermailBoxMapper.saveUsermailBox(usermailBox);
    Assert.assertTrue(true);
  }

  @Test
  public void testListUsermailBoxsByOwner() throws Exception {
    UsermailBoxDO box = new UsermailBoxDO(12345L, "test-session-owner2", "mailsend@temail.com", "mailsend@temail.com");
    usermailBoxMapper.saveUsermailBox(box);
    List<UsermailBoxDO> boxes = usermailBoxMapper.listUsermailBoxsByOwner("mailsend@temail.com", 0);
    assertThat(boxes.get(0).getOwner()).isEqualTo("mailsend@temail.com");
  }

  @Test
  public void testDeleteUsermailBox() throws Exception {
    UsermailBoxDO box = new UsermailBoxDO(124L, "test-session-owner3", "mailreceive12@temail.com", "mailsend12@temail.com");
    usermailBoxMapper.saveUsermailBox(box);
    int result = usermailBoxMapper.deleteByOwnerAndMail2("mailsend12@temail.com", "mailreceive12@temail.com");
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void testListUsermailBoxsByOwnerAndTo() {
    String owner = "mailsend1@temail.com";
    String mail2 = "mailreceive1@temail.com";
    UsermailBoxDO box = new UsermailBoxDO(123L, "test-session-owner3", mail2, owner);
    usermailBoxMapper.saveUsermailBox(box);
    List<UsermailBoxDO> usermailBoxes = usermailBoxMapper.listUsermailBoxsByOwnerAndTo(owner, mail2);
    assertThat(usermailBoxes).isNotNull();
    assertThat(usermailBoxes.size()).isOne();
  }

  @Test
  public void testUpdateArchiveStatus() {
    UsermailBoxDO box = new UsermailBoxDO(24355, "test-session-owner3", "mailreceive1@msgseal.com",
        "mailsend1@msgseal.com");
    usermailBoxMapper.saveUsermailBox(box);
    int result = usermailBoxMapper.updateArchiveStatus(box.getOwner(), box.getMail2(), box.getArchiveStatus());
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void testGetUsermailBox() {
    UsermailBoxDO box = new UsermailBoxDO(243545, "test-session-owner4", "mailreceive2@msgseal.com",
        "mailsend2@msgseal.com");
    usermailBoxMapper.saveUsermailBox(box);
    UsermailBoxDO usermailBox = usermailBoxMapper.selectByOwnerAndMail2(box.getOwner(), box.getMail2());
    assertThat(usermailBox.getOwner()).isEqualTo(box.getOwner());
    assertThat(usermailBox.getSessionid()).isEqualTo(usermailBox.getSessionid());
  }

  @Test
  public void deleteDomainTest() {
    String domain = "domain";
    int pageSize = 100;
    usermailBoxMapper.deleteDomain(domain, pageSize);
  }

  @Test
  public void updateSessionExtDataTest() {
    UsermailBoxDO usermailBoxDO = new UsermailBoxDO("owner", "mail2", "sessionExtData");
    usermailBoxMapper.updateSessionExtData(usermailBoxDO);
  }

  @Test
  public void testGetTopNMailboxes() {
    String from = "from@t.email";
    UsermailBoxDO usermailBoxDO1 = new UsermailBoxDO(100L, "483578", "to@t.email", from);
    UsermailBoxDO usermailBoxDO2 = new UsermailBoxDO(102L, "4835378", "to2@t.email", from);
    usermailBoxMapper.saveUsermailBox(usermailBoxDO1);
    usermailBoxMapper.saveUsermailBox(usermailBoxDO2);
    List<UsermailBoxDO> usermailBoxDOS = boxMapper.selectTopNByOwner(from, 0);
    assertThat(usermailBoxDOS.get(0).getMail2()).isEqualTo(usermailBoxDO1.getMail2());
    assertThat(usermailBoxDOS.get(0).getSessionid()).isEqualTo(usermailBoxDO1.getSessionid());
    assertThat(usermailBoxDOS.size()).isEqualTo(2);
  }
}
