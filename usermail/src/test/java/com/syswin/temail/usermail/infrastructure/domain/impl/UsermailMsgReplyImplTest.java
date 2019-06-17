package com.syswin.temail.usermail.infrastructure.domain.impl;

import static org.assertj.core.api.Assertions.assertThat;

import com.syswin.temail.usermail.domains.UsermailBox;
import com.syswin.temail.usermail.domains.UsermailMsgReply;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailBoxMapper;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailMsgReplyMapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.internal.handler.MockHandlerImpl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsermailMsgReplyImplTest {

    private UsermailMsgReplyRepoImpl usermailMsgReplyRepoImpl;
  private UsermailMsgReplyMapper usermailMsgReplyMapper;

  @Before
  public void setup(){
    this.usermailMsgReplyMapper = Mockito.mock(UsermailMsgReplyMapper.class);
    this.usermailMsgReplyRepoImpl = new UsermailMsgReplyRepoImpl(usermailMsgReplyMapper);
  }

  @Test
  public void insert() {
    UsermailMsgReply usermailMsgReply = new UsermailMsgReply();
    ArgumentCaptor<UsermailMsgReply> usermailMsgReplyCap = ArgumentCaptor.forClass(UsermailMsgReply.class);
    usermailMsgReplyRepoImpl.insert(usermailMsgReply);
    Mockito.verify(usermailMsgReplyMapper).insert(usermailMsgReplyCap.capture());
    assertThat(usermailMsgReplyCap.getValue()).isEqualTo(usermailMsgReply);
  }

  @Test
  public void getMsgReplys() {
    QueryMsgReplyDTO queryMsgReplyDTO = new QueryMsgReplyDTO();
    ArgumentCaptor<QueryMsgReplyDTO> queryMsgReplyDTOCap = ArgumentCaptor.forClass(QueryMsgReplyDTO.class);
    usermailMsgReplyRepoImpl.getMsgReplys(queryMsgReplyDTO);
    Mockito.verify(usermailMsgReplyMapper).getMsgReplys(queryMsgReplyDTOCap.capture());
    assertThat(queryMsgReplyDTOCap.getValue()).isEqualTo(queryMsgReplyDTO);
  }

  @Test
  public void deleteBatchMsgReplyStatus() {
    String ower = "ower";
    ArgumentCaptor<String> owerCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<String>> msgIdsCap = ArgumentCaptor.forClass(ArrayList.class);
    List<String> msgIds = new ArrayList<String>();
    usermailMsgReplyRepoImpl.deleteBatchMsgReplyStatus(ower,msgIds);
    Mockito.verify(usermailMsgReplyMapper).deleteBatchMsgReplyStatus(owerCap.capture(),msgIdsCap.capture());
    assertThat(owerCap.getValue()).isEqualTo(ower);
    assertThat(msgIdsCap.getValue()).isEqualTo(msgIds);
  }


}
