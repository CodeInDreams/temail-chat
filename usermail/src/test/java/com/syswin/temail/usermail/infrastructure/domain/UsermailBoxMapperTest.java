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

import com.syswin.temail.usermail.domains.UsermailBoxDO;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailBoxMapper;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class UsermailBoxMapperTest {

  private static Set<Long> existIds = new HashSet<>();

  @Autowired
  private UsermailBoxMapper usermailBoxMapper;

  @Test
  public void saveUsermailBox() {
    UsermailBoxDO usermailBox = new UsermailBoxDO();
    usermailBox.setId(this.generatePKid());
    usermailBox.setMail2("mailB@syswin.com");
    usermailBox.setSessionid("sessionid");
    usermailBox.setOwner("mailA@syswin.com");
    usermailBoxMapper.saveUsermailBox(usermailBox);
    Assert.assertTrue(true);
  }

  @Test
  public void testListUsermailBoxsByOwner() throws Exception {
    UsermailBoxDO box = new UsermailBoxDO(this.generatePKid(), "test-session-owner", "mailsend@temail.com",
        "mailsend@temail.com");
    usermailBoxMapper.saveUsermailBox(box);
    List<UsermailBoxDO> boxes = usermailBoxMapper.listUsermailBoxsByOwner("mailsend@temail.com", 0);
    assertThat(boxes.get(0).getOwner()).isEqualTo("mailsend@temail.com");
  }

  @Test
  public void testDeleteUsermailBox() throws Exception {
    UsermailBoxDO box = new UsermailBoxDO(this.generatePKid(), "test-session-owner2", "mailreceive12@temail.com",
        "mailsend12@temail.com");
    usermailBoxMapper.saveUsermailBox(box);
    int result = usermailBoxMapper.deleteByOwnerAndMail2("mailsend12@temail.com", "mailreceive12@temail.com");
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void testListUsermailBoxsByOwnerAndTo() {
    String owner = "mailsend3@temail.com";
    String mail2 = "mailreceive3@temail.com";
    UsermailBoxDO box = new UsermailBoxDO(this.generatePKid(), "test-session-owner3", mail2, owner);
    usermailBoxMapper.saveUsermailBox(box);
    List<UsermailBoxDO> usermailBoxes = usermailBoxMapper.listUsermailBoxsByOwnerAndTo(owner, mail2);
    assertThat(usermailBoxes).isNotNull();
    assertThat(usermailBoxes.size()).isOne();
  }

  @Test
  public void testUpdateArchiveStatus() {
    UsermailBoxDO box = new UsermailBoxDO(this.generatePKid(), "test-session-owner4", "mailreceive4@msgseal.com",
        "mailsend4@msgseal.com");
    usermailBoxMapper.saveUsermailBox(box);
    int result = usermailBoxMapper.updateArchiveStatus(box.getOwner(), box.getMail2(), box.getArchiveStatus());
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void testGetUsermailBox() {
    UsermailBoxDO box = new UsermailBoxDO(this.generatePKid(), "test-session-owner5", "mailreceive5@msgseal.com",
        "mailsend5@msgseal.com");
    usermailBoxMapper.saveUsermailBox(box);
    UsermailBoxDO usermailBox = usermailBoxMapper.selectByOwnerAndMail2(box.getOwner(), box.getMail2());
    assertThat(usermailBox.getOwner()).isEqualTo(box.getOwner());
    assertThat(usermailBox.getSessionid()).isEqualTo(usermailBox.getSessionid());
  }

  @Test
  public void deleteDomainTest() {
    UsermailBoxDO usermailBox = new UsermailBoxDO();
    usermailBox.setId(this.generatePKid());
    usermailBox.setMail2("mailB@deleteDomain");
    usermailBox.setSessionid("sessionid");
    usermailBox.setOwner("mailA@deleteDomain");
    usermailBoxMapper.saveUsermailBox(usermailBox);
    usermailBox.setId(this.generatePKid());
    usermailBox.setOwner("mailB@deleteDomain");
    usermailBoxMapper.saveUsermailBox(usermailBox);

    String domain = "deleteDomain";
    int pageSize = 1;
    int count = usermailBoxMapper.deleteDomain(domain, pageSize);

    assertThat(count).isOne();
  }

  @Test
  public void updateSessionExtDataTest() {
    String owner = "owner";
    String mail2 = "mail2";
    UsermailBoxDO usermailBox = new UsermailBoxDO();
    usermailBox.setId(this.generatePKid());
    usermailBox.setMail2(mail2);
    usermailBox.setSessionid("sessionid");
    usermailBox.setOwner(owner);
    usermailBox.setSessionExtData("sessionExtData");
    usermailBoxMapper.saveUsermailBox(usermailBox);
    String sessionExtDataUpdate = "sessionExtData-1";
    UsermailBoxDO usermailBoxDO = new UsermailBoxDO(owner, mail2, sessionExtDataUpdate);
    usermailBoxMapper.updateSessionExtData(usermailBoxDO);

    UsermailBoxDO dbBox = usermailBoxMapper.selectByOwnerAndMail2(owner, mail2);
    assertThat(dbBox.getSessionExtData()).isEqualTo(sessionExtDataUpdate);
  }

  @Test
  public void testGetTopNMailboxes() {
    String from = "from@t.email";
    UsermailBoxDO usermailBoxDO1 = new UsermailBoxDO(this.generatePKid(), "483578", "to@t.email", from);
    UsermailBoxDO usermailBoxDO2 = new UsermailBoxDO(this.generatePKid(), "4835378", "to2@t.email", from);
    usermailBoxMapper.saveUsermailBox(usermailBoxDO1);
    usermailBoxMapper.saveUsermailBox(usermailBoxDO2);
    List<UsermailBoxDO> usermailBoxes = usermailBoxMapper.selectTopNByOwner(from, 0);
    assertThat(usermailBoxes.size()).isEqualTo(2);
  }

  private long generatePKid() {
    boolean isUnique;
    long id;
    do {
      id = new Random().nextInt(100);
      isUnique = existIds.add(id);
    } while (!isUnique);
    return id;
  }
}
