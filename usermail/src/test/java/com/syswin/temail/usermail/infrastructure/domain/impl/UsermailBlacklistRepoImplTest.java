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

package com.syswin.temail.usermail.infrastructure.domain.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.syswin.temail.usermail.domains.UsermailBlacklistDO;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailBlacklistMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsermailBlacklistRepoImplTest {

  private UsermailBlacklistRepoImpl usermailBlacklistRepoImpl;
  private UsermailBlacklistMapper usermailBlacklistMapper;

  @Before
  public void setup(){
    this.usermailBlacklistMapper = Mockito.mock(UsermailBlacklistMapper.class);
    this.usermailBlacklistRepoImpl = new UsermailBlacklistRepoImpl(usermailBlacklistMapper);
  }

  @Test
  public void testInsertUsermailBlacklist() {
    UsermailBlacklistDO usermailBlacklist = new UsermailBlacklistDO(1, "from@msg.com", "blacklist@msgseal.com");
    Mockito.when(usermailBlacklistMapper.insertUsermailBlacklist(usermailBlacklist)).thenReturn(1);
    int row = usermailBlacklistRepoImpl.insertUsermailBlacklist(usermailBlacklist);
    Assert.assertEquals(1,row);
  }

  @Test
  public void testDeleteUsermailBlacklist() {
    UsermailBlacklistDO usermailBlacklist = new UsermailBlacklistDO(2, "from2@msgseal.com", "blacklist2@msgseal.com");
    Mockito.when(usermailBlacklistMapper.deleteUsermailBlacklist(usermailBlacklist)).thenReturn(1);
    int row = usermailBlacklistRepoImpl.deleteUsermailBlacklist(usermailBlacklist);
    Assert.assertEquals(1,row);
  }

  @Test
  public void testGetUsermailBlacklist() {
    String temailAddress = "temail@msgseal.com";
    String blackAddress = "blacklist@msgseal.com";
    UsermailBlacklistDO blacklist = new UsermailBlacklistDO(3, temailAddress, blackAddress);
    Mockito.when(usermailBlacklistMapper.getUsermailBlacklist(temailAddress,blackAddress)).thenReturn(blacklist);
    UsermailBlacklistDO blacklists = usermailBlacklistRepoImpl.getUsermailBlacklist(temailAddress, blackAddress);
    assertThat(blacklists).isEqualTo(blacklist);
  }

  @Test
  public void testListUsermailBlacklists() {
    String temailAddress = "temail2@msgseal.com";
    String blackAddress = "blacklist2@msgseal.com";
    List<UsermailBlacklistDO> usermailBlacklists = new ArrayList<UsermailBlacklistDO>();
    Mockito.when(usermailBlacklistMapper.listUsermailBlacklists(temailAddress)).thenReturn(usermailBlacklists);
    List<UsermailBlacklistDO> usermailBlacklist = usermailBlacklistRepoImpl.listUsermailBlacklists(temailAddress);
    assertThat(usermailBlacklists).isEqualTo(usermailBlacklist);
  }

  @Test
  public void testCountByAddress() {
    String temailAddress = "temail3@msgseal.com";
    String blackAddress = "blacklist3@msgseal.com";
    int a = 2;
    UsermailBlacklistDO blacklist = new UsermailBlacklistDO(5, temailAddress, blackAddress);
    Mockito.when(usermailBlacklistMapper.countByAddresses(temailAddress, blackAddress)).thenReturn(a);
    int count = usermailBlacklistRepoImpl.countByAddresses(temailAddress, blackAddress);
    assertThat(count).isEqualTo(a);
  }


}
