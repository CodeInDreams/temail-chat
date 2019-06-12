package com.syswin.temail.usermail.infrastructure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.syswin.temail.usermail.UsermailAgentApplication;
import com.syswin.temail.usermail.domains.UsermailBlacklist;
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
public class UsermailBlacklistMapperTests {

  @Autowired
  private UsermailBlacklistRepo usermailBlacklistRepo;

  @Test
  public void insert() {
    UsermailBlacklist usermailBlacklist = new UsermailBlacklist(1, "from@msg.com", "blacklist@msgseal.com");
    int result = usermailBlacklistRepo.insert(usermailBlacklist);
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void deleteByAddress() {
    UsermailBlacklist usermailBlacklist = new UsermailBlacklist(2, "from2@msgseal.com", "blacklist2@msgseal.com");
    usermailBlacklistRepo.insert(usermailBlacklist);
    int result = usermailBlacklistRepo.deleteByAddresses(usermailBlacklist);
    assertThat(result).isEqualTo(1);
  }

  @Test
  public void selectByAddress() {
    String temailAddress = "temail@msgseal.com";
    String blackAddress = "blacklist@msgseal.com";
    UsermailBlacklist blacklist = new UsermailBlacklist(3, temailAddress, blackAddress);
    usermailBlacklistRepo.insert(blacklist);
    UsermailBlacklist blacklists = usermailBlacklistRepo.selectByAddresses(temailAddress, blackAddress);
    assertThat(blacklists.getBlackedAddress()).isEqualTo(blackAddress);
  }

  @Test
  public void selectByTemailAddress() {
    String temailAddress = "temail2@msgseal.com";
    String blackAddress = "blacklist2@msgseal.com";
    UsermailBlacklist blacklist = new UsermailBlacklist(4, temailAddress, blackAddress);
    usermailBlacklistRepo.insert(blacklist);
    List<UsermailBlacklist> usermailBlacklists = usermailBlacklistRepo.selectByTemailAddress(temailAddress);
    assertThat(usermailBlacklists.get(0).getBlackedAddress()).isEqualTo(blackAddress);
  }

  @Test
  public void countByAddress() {
    String temailAddress = "temail3@msgseal.com";
    String blackAddress = "blacklist3@msgseal.com";
    UsermailBlacklist blacklist = new UsermailBlacklist(5, temailAddress, blackAddress);
    usermailBlacklistRepo.insert(blacklist);
    int count = usermailBlacklistRepo.countByAddresses(temailAddress, blackAddress);
    Assert.assertTrue(count >= 1);
  }
}