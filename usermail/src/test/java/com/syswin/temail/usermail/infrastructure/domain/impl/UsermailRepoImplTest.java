package com.syswin.temail.usermail.infrastructure.domain.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.domains.UsermailMsgReplyDO;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import com.syswin.temail.usermail.dto.UmQueryDTO;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailMapper;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailMsgReplyMapper;
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
public class UsermailRepoImplTest {

  private UsermailRepoImpl usermailRepoImpl;
  private UsermailMapper usermailMapper;

  @Before
  public void setup(){
    this.usermailMapper = Mockito.mock(UsermailMapper.class);
    this.usermailRepoImpl = new UsermailRepoImpl(usermailMapper);
  }

  @Test
  public void saveUsermail() {
    UsermailDO usermailDO = new UsermailDO();
    ArgumentCaptor<UsermailDO> usermailDOCap = ArgumentCaptor.forClass(UsermailDO.class);
    usermailRepoImpl.saveUsermail(usermailDO);
    Mockito.verify(usermailMapper).saveUsermail(usermailDOCap.capture());
    assertThat(usermailDOCap.getValue()).isEqualTo(usermailDO);
  }

  @Test
  public void getUsermail() {
    UmQueryDTO umQueryDTO = new UmQueryDTO();
    ArgumentCaptor<UmQueryDTO> usermailDOCap = ArgumentCaptor.forClass(UmQueryDTO.class);
    usermailRepoImpl.getUsermail(umQueryDTO);
    Mockito.verify(usermailMapper).getUsermail(usermailDOCap.capture());
    assertThat(usermailDOCap.getValue()).isEqualTo(umQueryDTO);
  }

  @Test
  public void getUsermailByMsgid() {
    String msgId = "msgId";
    String owner = "owner";
    ArgumentCaptor<String> msgIdCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> ownerCap = ArgumentCaptor.forClass(String.class);
    usermailRepoImpl.getUsermailByMsgid(msgId,owner);
    Mockito.verify(usermailMapper).getUsermailByMsgid(msgIdCap.capture(),ownerCap.capture());
    assertThat(msgIdCap.getValue()).isEqualTo(msgId);
    assertThat(ownerCap.getValue()).isEqualTo(owner);
  }


}
