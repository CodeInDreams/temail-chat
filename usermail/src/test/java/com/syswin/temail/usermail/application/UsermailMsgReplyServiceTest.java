package com.syswin.temail.usermail.application;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.common.Constants.TemailType;
import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.core.util.MsgCompressor;
import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.domains.UsermailMsgReplyDO;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import com.syswin.temail.usermail.infrastructure.domain.UsermailRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailMsgReplyRepo;
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

  @Test
  public void sendUsermailMsgReply() {
    String to = "to@temail.com";
    String from = "from@temail.com";
    String message = Base64.getEncoder().encodeToString("Demo message.".getBytes());
    String msgid = "msgid";
    String parentMsgid = "string201810241832";
    String owner = to;
    int type = 1;
    int attachmentSize = 10;
    int storeType = 1;

    when(usermailAdapter.getMsgReplyPkID()).thenReturn(1L);
    when(usermailAdapter.getMsgReplySeqNo(parentMsgid, "")).thenReturn(1L);
    UsermailDO usermail = new UsermailDO();
    usermail.setMsgid(parentMsgid);
    usermail.setStatus(TemailStatus.STATUS_NORMAL_0);
    usermail.setZipMsg(msgCompressor.zipWithDecode(message));
    when(usermailRepo.getUsermailByMsgid(parentMsgid, to)).thenReturn(usermail);
    when(usermailAdapter.getMsgSeqNo(from, to, to)).thenReturn(2L);

    usermailMsgReplyService
        .createMsgReply(headerInfo, from, to, message, msgid, parentMsgid, type, attachmentSize, to);
    usermail2NotifyMqService.sendMqSaveMsgReply(headerInfo, from, to, owner, msgid, message,
        usermailAdapter.getMsgReplySeqNo(parentMsgid, ""), attachmentSize, parentMsgid);
    ArgumentCaptor<UsermailMsgReplyDO> argumentCaptor2 = ArgumentCaptor.forClass(UsermailMsgReplyDO.class);
    verify(usermailMsgReplyRepo).insert(argumentCaptor2.capture());
    UsermailMsgReplyDO msgReply = argumentCaptor2.getValue();
    assertEquals(from, msgReply.getFrom());
    assertEquals(to, msgReply.getTo());
    assertEquals(msgid, msgReply.getMsgid());
    assertEquals(message, msgCompressor.unzipEncode(msgReply.getZipMsg()));
    assertEquals(type, msgReply.getType());

    ArgumentCaptor<String> parentMsgIdCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> ownerCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> replyCountCaptor = ArgumentCaptor.forClass(Integer.class);
    ArgumentCaptor<String> lastReplyMsgidCaptor = ArgumentCaptor.forClass(String.class);

    Mockito.verify(usermailRepo).updateReplyCountAndLastReplyMsgid(parentMsgIdCaptor.capture(),
        ownerCaptor.capture(), replyCountCaptor.capture(), lastReplyMsgidCaptor.capture());
    Assert.assertEquals(parentMsgIdCaptor.getValue(), parentMsgid);
    Assert.assertEquals(ownerCaptor.getValue(), owner);
    Assert.assertEquals(1, replyCountCaptor.getValue().intValue());
//    Assert.assertEquals(seqNoCaptor.getValue().longValue(), 2L);
  }

  @Test
  public void revertMsgReply() {
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
    when(usermailRepo.getUsermailListByMsgid(parentMsgid)).thenReturn(usermails);
    when(usermailMsgReplyRepo.revertUsermailReply(Mockito.any(UsermailMsgReplyDO.class))).thenReturn(1);
    when(usermailRepo.getUsermailByMsgid(anyString(), anyString())).thenReturn(new UsermailDO());
    usermailMsgReplyService.revertMsgReply(xPacketId, header, from, to, from, parentMsgid, msgid);
    usermail2NotifyMqService.sendMqAfterUpdateMsgReply(xPacketId, header, from, to, owner, msgid, type, parentMsgid);
    ArgumentCaptor<UsermailMsgReplyDO> msgReplyCaptor = ArgumentCaptor.forClass(UsermailMsgReplyDO.class);
    ArgumentCaptor<String> ownerCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> parentMsgIdCaptor = ArgumentCaptor.forClass(String.class);
    Mockito.verify(usermailMsgReplyRepo).revertUsermailReply(msgReplyCaptor.capture());
    Mockito.verify(usermailRepo).getUsermailByMsgid(parentMsgIdCaptor.capture(), ownerCaptor.capture());
    UsermailMsgReplyDO msgReply = msgReplyCaptor.getValue();
    Assert.assertEquals(TemailStatus.STATUS_REVERT_1, msgReply.getStatus());
    Assert.assertEquals(parentMsgid, parentMsgIdCaptor.getValue());
    Assert.assertEquals(from, ownerCaptor.getValue());
  }

  @Test
  public void revertMsgReplyTest() {
    String to = "to@temail.com";
    String from = "from@temail.com";
    String msgid = "msgid";
    String parentMsgid = "string201810241832";
    when(usermailRepo.getUsermailListByMsgid(parentMsgid)).thenReturn(Arrays.asList(new UsermailDO()));
    usermailMsgReplyService.revertMsgReply(headerInfo, parentMsgid, msgid, from, to);
    verify(usermailMqService)
        .sendMqRevertReplyMsg(headerInfo.getxPacketId(), headerInfo.getCdtpHeader(), from, to, to, parentMsgid, msgid);
   /* verify(usermailMqService)
        .sendMqRevertReplyMsg(headerInfo.getxPacketId(), headerInfo.getCdtpHeader(), from, to, from, parentMsgid,
            msgid);
    ArgumentCaptor<String> xPacketIdCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> headerCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> fromCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> ownerCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> msgIdCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> parentIdCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> typeCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(usermail2NotifyMqService)
        .sendMqAfterUpdateMsgReply(xPacketIdCaptor.capture(),headerCaptor.capture(), fromCaptor.capture(), toCaptor.capture(),
            ownerCaptor.capture(), msgIdCaptor.capture(), typeCaptor.capture(), parentIdCaptor.capture());
    assertEquals(from, fromCaptor.getValue());
    assertEquals(msgid, msgIdCaptor.getValue());
    assertEquals(parentMsgid, parentIdCaptor.getValue());
    int type = typeCaptor.getValue();
    assertEquals(SessionEventType.EVENT_TYPE_19, type);*/
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
    when(usermailRepo.getUsermailByMsgid(parentMsgid, from)).thenReturn(usermail);
    usermailMsgReplyService.removeMsgReplys(headerInfo, parentMsgid, msgIds, from, to);
    usermail2NotifyMqService.sendMqAfterRemoveMsgReply(headerInfo, from, to, from, msgIds, type, parentMsgid);
    ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List> argumentCaptor2 = ArgumentCaptor.forClass(List.class);
    verify(usermailMsgReplyRepo)
        .deleteBatchMsgReplyStatus(argumentCaptor.capture(), argumentCaptor2.capture());
    List<String> msgIds2 = argumentCaptor2.getValue();
    Assert.assertEquals(msgIds, msgIds2);
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
    String filterSeqIds = "";
    List<UsermailDO> usermails = new ArrayList<>(1);
    UsermailDO usermail = new UsermailDO();
    usermail.setZipMsg(msgCompressor.zipWithDecode(Base64.getEncoder().encodeToString("demo msg".getBytes())));
    usermails.add(usermail);

    List<UsermailMsgReplyDO> usermailMsgReplyList = new ArrayList<>(1);
    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
    usermailMsgReply.setParentMsgid(parentMsgid);
    usermailMsgReply.setOwner(owner);
    usermailMsgReply.setStatus(1);
    usermailMsgReply.setMsgid(msgid);
    usermailMsgReply.setTo(to);
    usermailMsgReply.setFrom(from);
    usermailMsgReply.setSeqNo(seqId);
    usermailMsgReply.setZipMsg(msgCompressor.zipWithDecode(Base64.getEncoder().encodeToString("demo msg".getBytes())));
    usermailMsgReplyList.add(usermailMsgReply);
    when(usermailRepo.getUsermailListByMsgid(parentMsgid)).thenReturn(usermails);
    when(usermailRepo.getUsermailByMsgid(parentMsgid, owner)).thenReturn(usermail);
    when(usermailMsgReplyRepo.getMsgReplys(Mockito.any(QueryMsgReplyDTO.class)))
        .thenReturn(usermailMsgReplyList);
    when(convertMsgService.convertReplyMsg(usermailMsgReplyList)).thenReturn(usermailMsgReplyList);
    List<UsermailMsgReplyDO> result = usermailMsgReplyService
        .getMsgReplys(headerInfo, parentMsgid, pageSize, seqId, signal, owner, filterSeqIds);
    ArgumentCaptor<QueryMsgReplyDTO> argumentCaptor = ArgumentCaptor.forClass(QueryMsgReplyDTO.class);
    verify(usermailMsgReplyRepo).getMsgReplys(argumentCaptor.capture());
    QueryMsgReplyDTO queryMsgReplyDto = argumentCaptor.getValue();
    Assert.assertEquals(pageSize, queryMsgReplyDto.getPageSize());
    Assert.assertEquals(seqId, queryMsgReplyDto.getFromSeqNo());
    Assert.assertEquals(signal, queryMsgReplyDto.getSignal());
    Assert.assertEquals(parentMsgid, queryMsgReplyDto.getParentMsgid());
    Assert.assertEquals(result, usermailMsgReplyList);
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
    Mockito.when(usermailMsgReplyRepo.getMsgReplyByCondition(Mockito.any())).thenReturn(reply);
    List<UsermailDO> usermails = new ArrayList<>();
    usermails.add(new UsermailDO());
    when(usermailRepo.getUsermailListByMsgid(reply.getParentMsgid())).thenReturn(usermails);
    usermailMsgReplyService.destroyAfterRead(headerInfo, from, to, msgId);
    //verify(usermailMqService).sendMqReplyMsgDestroyAfterRead(headerInfo.getxPacketId(),headerInfo.getCdtpHeader(),from,to,from, msgId, reply.getParentMsgid());
    verify(usermailMqService)
        .sendMqReplyMsgDestroyAfterRead(headerInfo.getxPacketId(), headerInfo.getCdtpHeader(), from, to, to, msgId,
            reply.getParentMsgid());
   /* ArgumentCaptor<CdtpHeaderDTO> headerDtoArgumentCaptor = ArgumentCaptor.forClass(CdtpHeaderDTO.class);
    ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> fromCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> msgIdCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> typeCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(usermail2NotifyMqService).sendMqAfterUpdateStatus(headerDtoArgumentCaptor.capture(),
        toCaptor.capture(), fromCaptor.capture(), msgIdCaptor.capture(), typeCaptor.capture());
    assertEquals(from, fromCaptor.getValue());
    assertEquals(msgId, msgIdCaptor.getValue());
    assertEquals(SessionEventType.EVENT_TYPE_26, typeCaptor.getValue().intValue());*/

  }

  @Test
  public void destroyAfterReadTest() {
    String from = "owner@msgseal.com";
    String to = "to@msgseal.com";
    String owner = "owner@msgseal.com";
    String msgId = "2344";
    String replyMsgParentId = "13311";
    UsermailDO usermail = new UsermailDO(1, msgId, "", owner, "", 1, 1, owner, "", 0);
    when(usermailRepo.getUsermailByMsgid(replyMsgParentId, owner)).thenReturn(usermail);
    when(usermailMsgReplyRepo.destroyAfterRead(owner, msgId, TemailStatus.STATUS_DESTROY_AFTER_READ_2)).thenReturn(1);
    usermailMsgReplyService
        .destroyAfterRead(headerInfo.getxPacketId(), headerInfo.getCdtpHeader(), from, to, owner, msgId,
            replyMsgParentId);
    ArgumentCaptor<String> ownerCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> msgIdCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(usermailMsgReplyRepo)
        .destroyAfterRead(ownerCaptor.capture(), msgIdCaptor.capture(), statusCaptor.capture());
    int status = statusCaptor.getValue();
    assertEquals(TemailStatus.STATUS_DESTROY_AFTER_READ_2, status);
  }
}
