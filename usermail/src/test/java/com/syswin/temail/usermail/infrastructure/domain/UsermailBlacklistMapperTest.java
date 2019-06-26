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
import com.syswin.temail.usermail.domains.UsermailBlacklistDO;
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
public class UsermailBlacklistMapperTest {

  @Autowired
  private UsermailBlacklistRepo usermailBlacklistRepo;

  @Test
  public void testInsertUsermailBlacklist() {
    UsermailBlacklistDO usermailBlacklist = new UsermailBlacklistDO(1, "from@msg.com", "blacklist@msgseal.com");
    int result = usermailBlacklistRepo.insertUsermailBlacklist(usermailBlacklist);
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void testDeleteUsermailBlacklist() {
    UsermailBlacklistDO usermailBlacklist = new UsermailBlacklistDO(2, "from2@msgseal.com", "blacklist2@msgseal.com");
    usermailBlacklistRepo.insertUsermailBlacklist(usermailBlacklist);
    int result = usermailBlacklistRepo.deleteByTemailAndBlackedAddress(usermailBlacklist);
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void testGetUsermailBlacklist() {
    String temailAddress = "temail@msgseal.com";
    String blackAddress = "blacklist@msgseal.com";
    UsermailBlacklistDO blacklist = new UsermailBlacklistDO(3, temailAddress, blackAddress);
    usermailBlacklistRepo.insertUsermailBlacklist(blacklist);
    UsermailBlacklistDO blacklists = usermailBlacklistRepo.selectByTemailAndBlackedAddress(temailAddress, blackAddress);
    assertThat(blacklists.getBlackedAddress()).isEqualTo(blackAddress);
  }

  @Test
  public void testListUsermailBlacklists() {
    String temailAddress = "temail2@msgseal.com";
    String blackAddress = "blacklist2@msgseal.com";
    UsermailBlacklistDO blacklist = new UsermailBlacklistDO(4, temailAddress, blackAddress);
    usermailBlacklistRepo.insertUsermailBlacklist(blacklist);
    List<UsermailBlacklistDO> usermailBlacklists = usermailBlacklistRepo.listUsermailBlacklists(temailAddress);
    assertThat(usermailBlacklists.get(0).getBlackedAddress()).isEqualTo(blackAddress);
  }

  @Test
  public void testCountByAddress() {
    String temailAddress = "temail3@msgseal.com";
    String blackAddress = "blacklist3@msgseal.com";
    UsermailBlacklistDO blacklist = new UsermailBlacklistDO(5, temailAddress, blackAddress);
    usermailBlacklistRepo.insertUsermailBlacklist(blacklist);
    int count = usermailBlacklistRepo.countByAddresses(temailAddress, blackAddress);
    Assert.assertTrue(count >= 1);
  }
}