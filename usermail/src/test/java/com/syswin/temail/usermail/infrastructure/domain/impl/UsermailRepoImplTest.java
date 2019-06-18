package com.syswin.temail.usermail.infrastructure.domain.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.dto.RevertMailDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import com.syswin.temail.usermail.dto.UmQueryDTO;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailMapper;
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

  @Test
  public void getLastUsermail() {
    UmQueryDTO umQueryDto = new UmQueryDTO();
    ArgumentCaptor<UmQueryDTO> umQueryDtoCap = ArgumentCaptor.forClass(UmQueryDTO.class);
    usermailRepoImpl.getLastUsermail(umQueryDto);
    Mockito.verify(usermailMapper).getLastUsermail(umQueryDtoCap.capture());
    assertThat(umQueryDtoCap.getValue()).isEqualTo(umQueryDto);
  }

  @Test
  public void revertUsermail() {
    RevertMailDTO revertMailDTO = new RevertMailDTO();
    ArgumentCaptor<RevertMailDTO> revertMailDTOCap = ArgumentCaptor.forClass(RevertMailDTO.class);
    usermailRepoImpl.revertUsermail(revertMailDTO);
    Mockito.verify(usermailMapper).revertUsermail(revertMailDTOCap.capture());
    assertThat(revertMailDTOCap.getValue()).isEqualTo(revertMailDTO);
  }

  @Test
  public void removeMsg() {
    List<String> msgIds = new ArrayList<String>();
    String owner = "owner";
    ArgumentCaptor<String> ownerCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<String>> msgIdsCap = ArgumentCaptor.forClass(ArrayList.class);
    usermailRepoImpl.removeMsg(msgIds, owner);
    Mockito.verify(usermailMapper).removeMsg(msgIdsCap.capture(),ownerCap.capture());
    assertThat(ownerCap.getValue()).isEqualTo(owner);
    assertThat(msgIdsCap.getValue()).isEqualTo(msgIds);
  }

  @Test
  public void destroyAfterRead() {
    String owner = "owner";
    String msgid = "msgid";
    int status = TemailStatus.STATUS_DESTROY_AFTER_READ_2;
    ArgumentCaptor<String> ownerCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> msgIdCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> statusCap = ArgumentCaptor.forClass(Integer.class);
    usermailRepoImpl.destroyAfterRead(owner, msgid, status);
    Mockito.verify(usermailMapper).destroyAfterRead(ownerCap.capture(),msgIdCap.capture(),statusCap.capture());
    assertThat(ownerCap.getValue()).isEqualTo(owner);
    assertThat(msgIdCap.getValue()).isEqualTo(msgid);
    assertThat(statusCap.getValue()).isEqualTo(status);
  }

  @Test
  public void batchDeleteBySessionId() {
    String sessionId = "sessionId";
    String owner = "owner";
    ArgumentCaptor<String> ownerCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> sessionIdCap = ArgumentCaptor.forClass(String.class);
    usermailRepoImpl.batchDeleteBySessionId(sessionId, owner);
    Mockito.verify(usermailMapper).batchDeleteBySessionId(sessionIdCap.capture(),ownerCap.capture());
    assertThat(sessionIdCap.getValue()).isEqualTo(sessionId);
    assertThat(ownerCap.getValue()).isEqualTo(owner);
  }

  @Test
  public void getUsermailListByMsgid() {
    String msgid = "msgid";
    ArgumentCaptor<String> msgidCap = ArgumentCaptor.forClass(String.class);
    usermailRepoImpl.getUsermailListByMsgid(msgid);
    Mockito.verify(usermailMapper).getUsermailListByMsgid(msgidCap.capture());
    assertThat(msgidCap.getValue()).isEqualTo(msgid);
  }

  @Test
  public void getUsermailByFromToMsgIds() {
    String from = "from";
    List<String> msgIds = new ArrayList<String>();
    ArgumentCaptor<String> fromCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List> msgIdsCap = ArgumentCaptor.forClass(ArrayList.class);

    usermailRepoImpl.getUsermailByFromToMsgIds(from,msgIds);
    Mockito.verify(usermailMapper).getUsermailByFromToMsgIds(fromCap.capture(),msgIdsCap.capture());
    assertThat(fromCap.getValue()).isEqualTo(from);
    assertThat(msgIdsCap.getValue()).isEqualTo(msgIds);
  }

  @Test
  public void updateReplyCountAndLastReplyMsgid() {
    String msgid = "msgid";
    String owner = "owner";
    int count = 1;
    String lastReplyMsgid = "lastReplyMsgid";
    ArgumentCaptor<String> ownerCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> msgIdCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> lastReplyMsgidCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> countCap = ArgumentCaptor.forClass(Integer.class);
    usermailRepoImpl.updateReplyCountAndLastReplyMsgid(msgid, owner, count, lastReplyMsgid);
    Mockito.verify(usermailMapper).updateReplyCountAndLastReplyMsgid(msgIdCap.capture(),ownerCap.capture(),countCap.capture(),lastReplyMsgidCap.capture());
    assertThat(msgIdCap.getValue()).isEqualTo(msgid);
    assertThat(ownerCap.getValue()).isEqualTo(owner);
    assertThat(countCap.getValue()).isEqualTo(count);
    assertThat(lastReplyMsgidCap.getValue()).isEqualTo(lastReplyMsgid);
  }

  @Test
  public void updateStatusByMsgIds() {
    List<String> msgIds = new ArrayList<String>();
    String owner = "owner";
    int status = 0;
    ArgumentCaptor<String> ownerCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<String>> msgIdsCap = ArgumentCaptor.forClass(ArrayList.class);
    ArgumentCaptor<Integer> statueCap = ArgumentCaptor.forClass(Integer.class);
    usermailRepoImpl.updateStatusByMsgIds(msgIds, owner, status);
    Mockito.verify(usermailMapper).updateStatusByMsgIds(msgIdsCap.capture(),ownerCap.capture(),statueCap.capture());
    assertThat(msgIdsCap.getValue()).isEqualTo(msgIds);
    assertThat(ownerCap.getValue()).isEqualTo(owner);
    assertThat(statueCap.getValue()).isEqualTo(status);
  }

  @Test
  public void removeMsgByStatus() {
    List<TrashMailDTO> trashMails = new ArrayList<TrashMailDTO>();
    String owner = "owner";
    int status = TemailStatus.STATUS_NORMAL_0;
    ArgumentCaptor<String> ownerCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<TrashMailDTO>> trashMailsCap = ArgumentCaptor.forClass(ArrayList.class);
    ArgumentCaptor<Integer> statueCap = ArgumentCaptor.forClass(Integer.class);
    usermailRepoImpl.removeMsgByStatus(trashMails,owner,status);
    Mockito.verify(usermailMapper).removeMsgByStatus(trashMailsCap.capture(),ownerCap.capture(),statueCap.capture());
    assertThat(trashMailsCap.getValue()).isEqualTo(trashMails);
    assertThat(ownerCap.getValue()).isEqualTo(owner);
    assertThat(statueCap.getValue()).isEqualTo(status);
  }

  @Test
  public void revertMsgFromTrash() {
    List<TrashMailDTO> trashMails = new ArrayList<TrashMailDTO>();
    String owner = "owner";
    int status = TemailStatus.STATUS_NORMAL_0;
    int originStatus = TemailStatus.STATUS_TRASH_4;
    ArgumentCaptor<String> ownerCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<TrashMailDTO>> trashMailsCap = ArgumentCaptor.forClass(ArrayList.class);
    ArgumentCaptor<Integer> statusCap = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<Integer> originStatusCap = ArgumentCaptor.forClass(Integer.class);
    usermailRepoImpl.revertMsgFromTrash(trashMails,owner,status);
    Mockito.verify(usermailMapper).revertMsgFromTrash(trashMailsCap.capture(),ownerCap.capture(),statusCap.capture(),originStatusCap.capture());
    assertThat(trashMailsCap.getValue()).isEqualTo(trashMails);
    assertThat(ownerCap.getValue()).isEqualTo(owner);
    assertThat(statusCap.getValue()).isEqualTo(status);
    assertThat(originStatusCap.getValue()).isEqualTo(originStatus);
  }



}
