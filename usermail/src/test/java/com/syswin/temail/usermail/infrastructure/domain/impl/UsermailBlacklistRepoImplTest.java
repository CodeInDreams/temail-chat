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
  public void insert() {
    UsermailBlacklistDO usermailBlacklist = new UsermailBlacklistDO(1, "from@msg.com", "blacklist@msgseal.com");
    Mockito.when(usermailBlacklistMapper.insert(usermailBlacklist)).thenReturn(1);
    int row = usermailBlacklistRepoImpl.insert(usermailBlacklist);
    Assert.assertEquals(1,row);
  }

  @Test
  public void deleteByAddress() {
    UsermailBlacklistDO usermailBlacklist = new UsermailBlacklistDO(2, "from2@msgseal.com", "blacklist2@msgseal.com");
    Mockito.when(usermailBlacklistMapper.deleteByAddresses(usermailBlacklist)).thenReturn(1);
    int row = usermailBlacklistRepoImpl.deleteByAddresses(usermailBlacklist);
    Assert.assertEquals(1,row);
  }

  @Test
  public void selectByAddress() {
    String temailAddress = "temail@msgseal.com";
    String blackAddress = "blacklist@msgseal.com";
    UsermailBlacklistDO blacklist = new UsermailBlacklistDO(3, temailAddress, blackAddress);
    Mockito.when(usermailBlacklistMapper.selectByAddresses(temailAddress,blackAddress)).thenReturn(blacklist);
    UsermailBlacklistDO blacklists = usermailBlacklistRepoImpl.selectByAddresses(temailAddress, blackAddress);
    assertThat(blacklists).isEqualTo(blacklist);
  }

  @Test
  public void selectByTemailAddress() {
    String temailAddress = "temail2@msgseal.com";
    String blackAddress = "blacklist2@msgseal.com";
    List<UsermailBlacklistDO> usermailBlacklists = new ArrayList<UsermailBlacklistDO>();
    Mockito.when(usermailBlacklistMapper.selectByTemailAddress(temailAddress)).thenReturn(usermailBlacklists);
    List<UsermailBlacklistDO> usermailBlacklist = usermailBlacklistRepoImpl.selectByTemailAddress(temailAddress);
    assertThat(usermailBlacklists).isEqualTo(usermailBlacklist);
  }

  @Test
  public void countByAddress() {
    String temailAddress = "temail3@msgseal.com";
    String blackAddress = "blacklist3@msgseal.com";
    int a = 2;
    UsermailBlacklistDO blacklist = new UsermailBlacklistDO(5, temailAddress, blackAddress);
    Mockito.when(usermailBlacklistMapper.countByAddresses(temailAddress, blackAddress)).thenReturn(a);
    int count = usermailBlacklistRepoImpl.countByAddresses(temailAddress, blackAddress);
    assertThat(count).isEqualTo(a);
  }


}
