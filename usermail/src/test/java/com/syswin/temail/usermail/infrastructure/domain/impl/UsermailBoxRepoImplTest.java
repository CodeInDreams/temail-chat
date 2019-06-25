package com.syswin.temail.usermail.infrastructure.domain.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.syswin.temail.usermail.domains.UsermailBlacklistDO;
import com.syswin.temail.usermail.domains.UsermailBoxDO;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailBlacklistMapper;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailBoxMapper;
import java.lang.management.MonitorInfo;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsermailBoxRepoImplTest {

  private UsermailBoxRepoImpl usermailBoxRepoImpl;
  private UsermailBoxMapper usermailBoxMapper;

  @Before
  public void setup(){
    this.usermailBoxMapper = Mockito.mock(UsermailBoxMapper.class);
    this.usermailBoxRepoImpl = new UsermailBoxRepoImpl(usermailBoxMapper);
  }

  @Test
  public void saveUsermailBox() {
    ArgumentCaptor<UsermailBoxDO> usermailBoxCap = ArgumentCaptor.forClass(UsermailBoxDO.class);
    UsermailBoxDO usermailBox = new UsermailBoxDO(1,"sessionId","mail2","a.test@");
    usermailBoxRepoImpl.saveUsermailBox(usermailBox);
    Mockito.verify(usermailBoxMapper).saveUsermailBox(usermailBoxCap.capture());
    assertThat(usermailBoxCap.getValue()).isEqualTo(usermailBox);
  }

  @Test
  public void getUsermailBoxByOwner() {
    String mail1 = "mail1";
    int archiveStatus = 0;
    ArgumentCaptor<String> mailCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> archiveStatusCap = ArgumentCaptor.forClass(Integer.class);
    List<UsermailBoxDO> usermailBoxList = new ArrayList<UsermailBoxDO>();
    usermailBoxRepoImpl.listUsermailBoxsByOwner(mail1,archiveStatus);
    Mockito.verify(usermailBoxMapper).listUsermailBoxsByOwner(mailCap.capture(),archiveStatusCap.capture());
    assertThat(mailCap.getValue()).isEqualTo(mail1);
    assertThat(archiveStatusCap.getValue()).isEqualTo(archiveStatus);
  }

  @Test
  public void deleteByOwnerAndTo() {
    String from = "from";
    String to = "to";
    ArgumentCaptor<String> fromCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> toCap = ArgumentCaptor.forClass(String.class);
    usermailBoxRepoImpl.deleteUsermailBox(from,to);
    Mockito.verify(usermailBoxMapper).deleteUsermailBox(fromCap.capture(),toCap.capture());
    assertThat(fromCap.getValue()).isEqualTo(from);
    assertThat(toCap.getValue()).isEqualTo(to);
  }

  @Test
  public void selectByOwnerAndTo() {
    String fromStr = "from";
    String toStr = "to";
    ArgumentCaptor<String> fromCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> toCap = ArgumentCaptor.forClass(String.class);
    usermailBoxRepoImpl.listUsermailBoxsByOwnerAndTo(fromStr,toStr);
    Mockito.verify(usermailBoxMapper).listUsermailBoxsByOwnerAndTo(fromCap.capture(),toCap.capture());
    assertThat(fromCap.getValue()).isEqualTo(fromStr);
    assertThat(toCap.getValue()).isEqualTo(toStr);
  }

  @Test
  public void updateArchiveStatus() {
    String from = "from";
    String to = "to";
    int archiveStatus = 0;
    ArgumentCaptor<String> fromCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> toCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> archiveStatusCap = ArgumentCaptor.forClass(Integer.class);
    usermailBoxRepoImpl.updateArchiveStatus(from,to,archiveStatus);
    Mockito.verify(usermailBoxMapper).updateArchiveStatus(fromCap.capture(),toCap.capture(),archiveStatusCap.capture());
    assertThat(fromCap.getValue()).isEqualTo(from);
    assertThat(toCap.getValue()).isEqualTo(to);
    assertThat(archiveStatusCap.getValue()).isEqualTo(archiveStatus);
  }

  @Test
  public void selectUsermailBox() {
    String owner = "ower";
    String to = "to";
    ArgumentCaptor<String> owerCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> toCap = ArgumentCaptor.forClass(String.class);
    usermailBoxRepoImpl.listUsermailBoxsByOwnerAndTo(owner,to);
    Mockito.verify(usermailBoxMapper).listUsermailBoxsByOwnerAndTo(owerCap.capture(),toCap.capture());
    assertThat(owerCap.getValue()).isEqualTo(owner);
    assertThat(toCap.getValue()).isEqualTo(to);
  }
}
