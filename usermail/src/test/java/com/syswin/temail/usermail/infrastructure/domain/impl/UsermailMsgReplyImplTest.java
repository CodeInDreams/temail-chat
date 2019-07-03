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
import static org.mockito.Mockito.verify;

import com.syswin.temail.usermail.domains.UsermailMsgReplyDO;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailMsgReplyMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UsermailMsgReplyImplTest {

  @InjectMocks
  private UsermailMsgReplyRepoImpl usermailMsgReplyRepoImpl;
  @Mock
  private UsermailMsgReplyMapper usermailMsgReplyMapper;

  @Test
  public void insert() {
    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
    ArgumentCaptor<UsermailMsgReplyDO> usermailMsgReplyCap = ArgumentCaptor.forClass(UsermailMsgReplyDO.class);
    usermailMsgReplyRepoImpl.insert(usermailMsgReply);
    Mockito.verify(usermailMsgReplyMapper).insert(usermailMsgReplyCap.capture());
    assertThat(usermailMsgReplyCap.getValue()).isEqualTo(usermailMsgReply);
  }

  @Test
  public void getMsgReplys() {
    QueryMsgReplyDTO queryMsgReplyDTO = new QueryMsgReplyDTO();
    ArgumentCaptor<QueryMsgReplyDTO> queryMsgReplyDTOCap = ArgumentCaptor.forClass(QueryMsgReplyDTO.class);
    usermailMsgReplyRepoImpl.listMsgReplys(queryMsgReplyDTO);
    Mockito.verify(usermailMsgReplyMapper).listMsgReplys(queryMsgReplyDTOCap.capture());
    assertThat(queryMsgReplyDTOCap.getValue()).isEqualTo(queryMsgReplyDTO);
  }

  @Test
  public void deleteBatchMsgReplyStatus() {
    String ower = "ower";
    ArgumentCaptor<String> owerCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<String>> msgIdsCap = ArgumentCaptor.forClass(ArrayList.class);
    List<String> msgIds = new ArrayList<String>();
    usermailMsgReplyRepoImpl.deleteMsgReplysByMsgIds(ower, msgIds);
    Mockito.verify(usermailMsgReplyMapper).deleteMsgReplysByMsgIds(owerCap.capture(), msgIdsCap.capture());
    assertThat(owerCap.getValue()).isEqualTo(ower);
    assertThat(msgIdsCap.getValue()).isEqualTo(msgIds);
  }

  @Test
  public void batchDeleteBySessionId() {
    String ower = "ower";
    String sessionId = "sessionId";
    usermailMsgReplyRepoImpl.deleteMsgReplysBySessionId(sessionId, ower);
    ArgumentCaptor<String> owerCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> sessionIdCap = ArgumentCaptor.forClass(String.class);
    Mockito.verify(usermailMsgReplyMapper).deleteMsgReplysBySessionId(sessionIdCap.capture(), owerCap.capture());
    assertThat(owerCap.getValue()).isEqualTo(ower);
    assertThat(sessionIdCap.getValue()).isEqualTo(sessionId);
  }

  @Test
  public void deleteMsgByParentIdAndOwner() {
    String ower = "ower";
    List<String> parentMsgIds = new ArrayList<String>();
    usermailMsgReplyRepoImpl.deleteMsgReplysByParentIds(ower, parentMsgIds);
    ArgumentCaptor<String> owerCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<String>> parentMsgIdsCap = ArgumentCaptor.forClass(ArrayList.class);
    Mockito.verify(usermailMsgReplyMapper).deleteMsgReplysByParentIds(owerCap.capture(), parentMsgIdsCap.capture());
    assertThat(owerCap.getValue()).isEqualTo(ower);
    assertThat(parentMsgIdsCap.getValue()).isEqualTo(parentMsgIds);
  }

  @Test
  public void getMsgReplyByCondition() {
    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
    usermailMsgReplyRepoImpl.selectMsgReplyByCondition(usermailMsgReply);
    ArgumentCaptor<UsermailMsgReplyDO> usermailMsgReplyCap = ArgumentCaptor.forClass(UsermailMsgReplyDO.class);
    Mockito.verify(usermailMsgReplyMapper).selectMsgReplyByCondition(usermailMsgReplyCap.capture());
    assertThat(usermailMsgReplyCap.getValue()).isEqualTo(usermailMsgReply);
  }

  @Test
  public void deleteMsgReplyLessThanTest() {
    LocalDate createTime = LocalDate.now();
    int batchNum = 100;
    int count = 100;

    Mockito.when(usermailMsgReplyMapper.deleteMsgReplyLessThan(createTime, batchNum)).thenReturn(count);

    int result = usermailMsgReplyRepoImpl.deleteMsgReplyLessThan(createTime, batchNum);

    assertThat(result).isEqualTo(count);
  }

  @Test
  public void removeDomainTest() {
    String domain = "domain";
    int pageSize = 100;

    usermailMsgReplyRepoImpl.removeDomain(domain, pageSize);
    verify(usermailMsgReplyMapper).deleteDomain("%@" + domain, pageSize);
  }

}
