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

package com.syswin.temail.usermail.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.common.Constants.TemailType;
import com.syswin.temail.usermail.common.ReplyCountEnum;
import com.syswin.temail.usermail.common.ResultCodeEnum;
import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import com.syswin.temail.usermail.core.util.MsgCompressor;
import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.domains.UsermailMsgReplyDO;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import com.syswin.temail.usermail.infrastructure.domain.UsermailMsgReplyRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailRepo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class UsermailMsgReplyServiceTest {

  private final UsermailRepo usermailRepo = Mockito.mock(UsermailRepo.class);
  private final UsermailMsgReplyRepo usermailMsgReplyRepo = Mockito.mock(UsermailMsgReplyRepo.class);
  private final IUsermailAdapter usermailAdapter = Mockito.mock(IUsermailAdapter.class);
  private final UsermailSessionService usermailSessionService = Mockito.mock(UsermailSessionService.class);
  private final Usermail2NotifyMqService usermail2NotifyMqService = Mockito.mock(Usermail2NotifyMqService.class);
  private final UsermailMqService usermailMqService = Mockito.mock(UsermailMqService.class);
  private CdtpHeaderDTO headerInfo = new CdtpHeaderDTO("{CDTP-header:value}",
      "{xPacketId:value}");
  private MsgCompressor msgCompressor = new MsgCompressor();
  private final ConvertMsgService convertMsgService = Mockito.mock(ConvertMsgService.class);
  private final UsermailMsgReplyService usermailMsgReplyService = new UsermailMsgReplyService(usermailRepo,
      usermailAdapter, usermailMsgReplyRepo, usermail2NotifyMqService, usermailSessionService, msgCompressor,
      usermailMqService, convertMsgService);


  @Test(expected = IllegalGmArgsException.class)
  public void createMsgReplyFailIfParentMsgidIllegalTest() {
    String from = "from@temail.com";
    String to = "to@temail.com";
    String message = Base64.getEncoder().encodeToString("Demo message.".getBytes());
    String msgid = "msgid";
    String parentMsgid = "string201810241832";
    String owner = to;
    int type = TemailType.TYPE_NORMAL_0;
    int attachmentSize = 10;

    when(usermailRepo.selectByMsgidAndOwner(parentMsgid, owner)).thenReturn(null);

    try {
      usermailMsgReplyService
          .createMsgReply(headerInfo, from, to, message, msgid, parentMsgid, type, attachmentSize, owner);
    } catch (IllegalGmArgsException e) {
      assertEquals(e.getResultCode(), ResultCodeEnum.ERROR_ILLEGAL_PARENT_MSG_ID);
      throw e;
    }
  }

  @Test
  public void createMsgReplySuccessTest() {
    String from = "from@temail.com";
    String to = "to@temail.com";
    String message = Base64.getEncoder().encodeToString("Demo message.".getBytes());
    String msgid = "msgid";
    String parentMsgid = "string201810241832";
    String owner = to;
    int type = TemailType.TYPE_NORMAL_0;
    int attachmentSize = 10;
    String sessionid = "sessionid-from@temail.com-to@temail.co";
    long seqNo = 1650L;

    // precondition
    UsermailDO parentMail = new UsermailDO();
    parentMail.setMsgid(parentMsgid);
    parentMail.setOwner(owner);
    parentMail.setStatus(TemailStatus.STATUS_NORMAL_0);
    when(usermailRepo.selectByMsgidAndOwner(parentMsgid, owner)).thenReturn(parentMail);
    when(usermailSessionService.getSessionID(from, to)).thenReturn(sessionid);
    when(usermailAdapter.getMsgReplySeqNo(parentMsgid, owner)).thenReturn(seqNo);

    // invoke method
    usermailMsgReplyService
        .createMsgReply(headerInfo, from, to, message, msgid, parentMsgid, type, attachmentSize, owner);

    // verify
    ArgumentCaptor<UsermailMsgReplyDO> msgReplyCaptor = ArgumentCaptor.forClass(UsermailMsgReplyDO.class);
    verify(usermailMsgReplyRepo).insert(msgReplyCaptor.capture());
    UsermailMsgReplyDO msgReplyCaptorValue = msgReplyCaptor.getValue();
    UsermailMsgReplyDO expectMsgReplyDo = new UsermailMsgReplyDO(usermailAdapter.getMsgReplyPkID(), parentMsgid,
        msgid, from, to, seqNo, "", TemailStatus.STATUS_NORMAL_0, type, owner, sessionid,
        msgCompressor.zipWithDecode(message));
    assertThat(msgReplyCaptorValue).isEqualToComparingOnlyGivenFields(expectMsgReplyDo);
    verify(usermailRepo).updateReplyCountAndLastReplyMsgid(parentMsgid, owner, ReplyCountEnum.INCR.value(), msgid);
    verify(usermail2NotifyMqService)
        .sendMqSaveMsgReply(headerInfo, from, to, owner, msgid, message, seqNo, attachmentSize, parentMsgid);

  }

  @Test
  public void testMqRevertMsgReply() {
    String xPacketId = UUID.randomUUID().toString();
    String header = "CDTP-header";
    String to = "to@temail.com";
    String from = "from@temail.com";
    String owner = to;
    String msgid = "msgid";
    String parentMsgid = "string201810241832";
    int type = 1;
    List<UsermailDO> usermails = new ArrayList<>(1);
    usermails.add(new UsermailDO());
    when(usermailRepo.listUsermailsByMsgid(parentMsgid)).thenReturn(usermails);
    when(usermailMsgReplyRepo.updateRevertUsermailReply(Mockito.any(UsermailMsgReplyDO.class))).thenReturn(1);
    when(usermailRepo.selectByMsgidAndOwner(anyString(), anyString())).thenReturn(new UsermailDO());
    usermailMsgReplyService.revertMsgReply(xPacketId, header, from, to, from, parentMsgid, msgid);
    usermail2NotifyMqService.sendMqAfterUpdateMsgReply(xPacketId, header, from, to, owner, msgid, type, parentMsgid);
    ArgumentCaptor<UsermailMsgReplyDO> msgReplyCaptor = ArgumentCaptor.forClass(UsermailMsgReplyDO.class);
    ArgumentCaptor<String> ownerCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> parentMsgIdCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(usermailMsgReplyRepo).updateRevertUsermailReply(msgReplyCaptor.capture());
    Mockito.verify(usermailRepo).selectByMsgidAndOwner(parentMsgIdCaptor.capture(), ownerCaptor.capture());
    UsermailMsgReplyDO msgReply = msgReplyCaptor.getValue();
    Assert.assertEquals(TemailStatus.STATUS_REVERT_1, msgReply.getStatus());
    Assert.assertEquals(parentMsgid, parentMsgIdCaptor.getValue());
    Assert.assertEquals(from, ownerCaptor.getValue());
  }

  @Test
  public void testRevertMsgReplyParentMsgNull() {
    String xPacketId = UUID.randomUUID().toString();
    String header = "CDTP-header";
    String to = "to@temail.com";
    String from = "from@temail.com";
    String msgid = "msgid";
    String parentMsgid = "string201810241832";
    List<UsermailDO> usermails = new ArrayList<>(1);
    usermails.add(new UsermailDO());
    when(usermailRepo.listUsermailsByMsgid(parentMsgid)).thenReturn(usermails);
    when(usermailMsgReplyRepo.updateRevertUsermailReply(Mockito.any(UsermailMsgReplyDO.class))).thenReturn(1);
    when(usermailRepo.selectByMsgidAndOwner(anyString(), anyString())).thenReturn(null);
    usermailMsgReplyService.revertMsgReply(xPacketId, header, from, to, from, parentMsgid, msgid);
  }

  @Test
  public void testRevertMsgReplyFail() {
    String xPacketId = UUID.randomUUID().toString();
    String header = "CDTP-header";
    String to = "to@temail.com";
    String from = "from@temail.com";
    String msgid = "msgid";
    String parentMsgid = "string201810241832";
    List<UsermailDO> usermails = new ArrayList<>(1);
    usermails.add(new UsermailDO());
    when(usermailRepo.listUsermailsByMsgid(parentMsgid)).thenReturn(usermails);
    when(usermailMsgReplyRepo.updateRevertUsermailReply(Mockito.any(UsermailMsgReplyDO.class))).thenReturn(0);
    usermailMsgReplyService.revertMsgReply(xPacketId, header, from, to, from, parentMsgid, msgid);
  }

  @Test
  public void revertMsgReplyTest() {
    String to = "to@temail.com";
    String from = "from@temail.com";
    String msgid = "msgid";
    String parentMsgid = "string201810241832";
    when(usermailRepo.listUsermailsByMsgid(parentMsgid)).thenReturn(Arrays.asList(new UsermailDO()));
    usermailMsgReplyService.revertMsgReply(headerInfo, parentMsgid, msgid, from, to);
    verify(usermailMqService)
        .sendMqRevertReplyMsg(headerInfo.getxPacketId(), headerInfo.getCdtpHeader(), from, to, to, parentMsgid, msgid);
  }

  @Test
  public void removeMsgReply() {
    String to = "to@temail.com";
    String from = "from@temail.com";
    List<String> msgIds = new ArrayList<>(1);
    msgIds.add("msgids");
    String parentMsgid = "string201810241832";
    int type = 1;
    List<UsermailDO> usermails = new ArrayList<>(1);
    usermails.add(new UsermailDO());
    UsermailDO usermail = new UsermailDO();
    when(usermailRepo.selectByMsgidAndOwner(parentMsgid, from)).thenReturn(usermail);
    usermailMsgReplyService.removeMsgReplys(headerInfo, parentMsgid, msgIds, from, to);
    usermail2NotifyMqService.sendMqAfterRemoveMsgReply(headerInfo, from, to, from, msgIds, type, parentMsgid);
    ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List> argumentCaptor2 = ArgumentCaptor.forClass(List.class);
    verify(usermailMsgReplyRepo)
        .deleteMsgReplysByMsgIds(argumentCaptor.capture(), argumentCaptor2.capture());
    List<String> msgIds2 = argumentCaptor2.getValue();
    Assert.assertEquals(msgIds, msgIds2);
  }

  @Test(expected = IllegalGmArgsException.class)
  public void testRemoveMsgReplyEmptyMsgids() {
    String to = "to@temail.com";
    String from = "from@temail.com";
    List<String> msgIds = new ArrayList<>(1);
    String parentMsgid = "string201810241832";
    List<UsermailDO> usermails = new ArrayList<>(1);
    usermails.add(new UsermailDO());
    usermailMsgReplyService.removeMsgReplys(headerInfo, parentMsgid, msgIds, from, to);
  }

  @Test(expected = IllegalGmArgsException.class)
  public void testRemoveMsgReplyEmptyParentMsg() {
    String to = "to@temail.com";
    String from = "from@temail.com";
    List<String> msgIds = new ArrayList<>(1);
    String parentMsgid = "string201810241832";
    List<UsermailDO> usermails = new ArrayList<>(1);
    usermails.add(new UsermailDO());
    when(usermailRepo.selectByMsgidAndOwner(any(), any())).thenReturn(null);
    usermailMsgReplyService.removeMsgReplys(headerInfo, parentMsgid, msgIds, from, to);
  }

  @Test
  public void testRemoveMsgReplyRevertLastMsgid() {
    String to = "to@temail.com";
    String from = "from@temail.com";
    List<String> msgIds = new ArrayList<>(1);
    msgIds.add("msgids");
    msgIds.add("lastReplyMsgid");
    String parentMsgid = "string201810241832";
    List<UsermailDO> usermails = new ArrayList<>(1);
    usermails.add(new UsermailDO());
    UsermailMsgReplyDO lastUsermailMsgReply = new UsermailMsgReplyDO();
    lastUsermailMsgReply.setMsgid("updatelastMsgid");
    when(usermailMsgReplyRepo.selectLastUsermailReply(any(), any(), anyInt())).thenReturn(lastUsermailMsgReply);
    when(usermailMsgReplyRepo.deleteMsgReplysByMsgIds(any(), any())).thenReturn(1);
    UsermailDO usermailByMsgid = new UsermailDO();
    usermailByMsgid.setLastReplyMsgId("lastReplyMsgid");
    usermailByMsgid.setOwner(from);
    when(usermailRepo.selectByMsgidAndOwner(any(), any())).thenReturn(usermailByMsgid);
    usermailMsgReplyService.removeMsgReplys(headerInfo, parentMsgid, msgIds, from, to);
    ArgumentCaptor<String> parentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> ownerCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> lastMsgIdCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> countCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(usermailRepo)
        .updateReplyCountAndLastReplyMsgid(parentCaptor.capture(), ownerCaptor.capture(), countCaptor.capture(),
            lastMsgIdCaptor.capture());
    verify(usermail2NotifyMqService).sendMqAfterRemoveMsgReply(any(), any(), any(), any(), any(), anyInt(), any());
    assertEquals(parentCaptor.getValue(), parentMsgid);
    assertEquals(ownerCaptor.getValue(), from);
    assertEquals(lastMsgIdCaptor.getValue(), lastUsermailMsgReply.getMsgid());
  }

  @Test
  public void getMsgReply() {
    int pageSize = 20;
    long seqId = 20;
    String to = "to@temail.com";
    String from = "from@temail.com";
    String signal = "before";
    String owner = "from@temail.com";
    String msgid = "msgid";
    String parentMsgid = "string201810241832";
    String filterSeqIds = "5_3,3_1";
    List<UsermailMsgReplyDO> usermailMsgReplyList = new ArrayList<>(1);
    for (int i = 1; i < 11; i++) {
      UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
      usermailMsgReply.setParentMsgid(parentMsgid);
      usermailMsgReply.setOwner(owner);
      usermailMsgReply.setStatus(1);
      usermailMsgReply.setMsgid(msgid);
      usermailMsgReply.setTo(to);
      usermailMsgReply.setFrom(from);
      usermailMsgReply.setSeqNo(i);
      usermailMsgReply
          .setZipMsg(msgCompressor.zipWithDecode(Base64.getEncoder().encodeToString("demo msg".getBytes())));
      usermailMsgReplyList.add(usermailMsgReply);
    }
    UsermailDO usermail = new UsermailDO();
    when(usermailRepo.selectByMsgidAndOwner(parentMsgid, owner)).thenReturn(usermail);
    when(convertMsgService.convertReplyMsg(any())).thenReturn(usermailMsgReplyList);
    List<UsermailMsgReplyDO> result = usermailMsgReplyService
        .getMsgReplys(parentMsgid, pageSize, seqId, signal, owner, filterSeqIds);
    ArgumentCaptor<QueryMsgReplyDTO> argumentCaptor = ArgumentCaptor.forClass(QueryMsgReplyDTO.class);
    verify(usermailMsgReplyRepo).listMsgReplys(argumentCaptor.capture());
    QueryMsgReplyDTO queryMsgReplyDto = argumentCaptor.getValue();
    Assert.assertEquals(pageSize, queryMsgReplyDto.getPageSize());
    Assert.assertEquals(seqId, queryMsgReplyDto.getFromSeqNo());
    Assert.assertEquals(signal, queryMsgReplyDto.getSignal());
    Assert.assertEquals(parentMsgid, queryMsgReplyDto.getParentMsgid());
    Assert.assertEquals(2, result.size());
  }

  @Test
  public void destroyAfterRead() {
    String from = "XXXX@qq.com";
    String to = "YYYY@qq.com";
    String msgId = "1234565432345432343";
    UsermailMsgReplyDO reply = new UsermailMsgReplyDO();
    reply.setOwner(from);
    reply.setMsgid(msgId);
    reply.setType(TemailType.TYPE_DESTROY_AFTER_READ_1);
    Mockito.when(usermailMsgReplyRepo.selectMsgReplyByCondition(Mockito.any())).thenReturn(reply);
    List<UsermailDO> usermails = new ArrayList<>();
    usermails.add(new UsermailDO());
    when(usermailRepo.listUsermailsByMsgid(reply.getParentMsgid())).thenReturn(usermails);
    usermailMsgReplyService.destroyAfterRead(headerInfo, from, to, msgId);
    verify(usermailMqService)
        .sendMqReplyMsgDestroyAfterRead(headerInfo.getxPacketId(), headerInfo.getCdtpHeader(), from, to, to, msgId,
            reply.getParentMsgid());

  }

  @Test
  public void destroyAfterReadTest() {
    String from = "owner@msgseal.com";
    String to = "to@msgseal.com";
    String owner = "owner@msgseal.com";
    String msgId = "2344";
    String replyMsgParentId = "13311";
    UsermailDO usermail = new UsermailDO(1, msgId, "", owner, "", 1, 1, owner, "", 0);
    when(usermailRepo.selectByMsgidAndOwner(replyMsgParentId, owner)).thenReturn(usermail);
    when(usermailMsgReplyRepo.updateDestroyAfterRead(owner, msgId, TemailStatus.STATUS_DESTROY_AFTER_READ_2))
        .thenReturn(1);
    usermailMsgReplyService
        .destroyAfterRead(headerInfo.getxPacketId(), headerInfo.getCdtpHeader(), from, to, owner, msgId,
            replyMsgParentId);
    ArgumentCaptor<String> ownerCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> msgIdCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(usermailMsgReplyRepo)
        .updateDestroyAfterRead(ownerCaptor.capture(), msgIdCaptor.capture(), statusCaptor.capture());
    int status = statusCaptor.getValue();
    assertEquals(TemailStatus.STATUS_DESTROY_AFTER_READ_2, status);
  }
}
