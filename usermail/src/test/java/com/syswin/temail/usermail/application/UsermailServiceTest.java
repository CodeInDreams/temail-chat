package com.syswin.temail.usermail.application;

import static com.syswin.temail.usermail.common.Constants.TemailType.TYPE_DESTORY_AFTER_READ_1;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.ImmutableMap;
import com.syswin.temail.usermail.common.Constants.TemailArchiveStatus;
import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.common.Constants.TemailType;
import com.syswin.temail.usermail.common.Constants.UsermailAgentEventType;
import com.syswin.temail.usermail.common.ParamsKey;
import com.syswin.temail.usermail.common.SessionEventType;
import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import com.syswin.temail.usermail.core.util.MsgCompressor;
import com.syswin.temail.usermail.domains.UsermailBoxDO;
import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.dto.CreateUsermailDTO;
import com.syswin.temail.usermail.dto.DeleteMailBoxQueryDTO;
import com.syswin.temail.usermail.dto.MailboxDTO;
import com.syswin.temail.usermail.dto.QueryTrashDTO;
import com.syswin.temail.usermail.dto.RevertMailDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import com.syswin.temail.usermail.dto.UmQueryDTO;
import com.syswin.temail.usermail.infrastructure.domain.UsermailBoxRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailMsgReplyRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailRepo;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class UsermailServiceTest {

  private final UsermailRepo usermailRepo = Mockito.mock(UsermailRepo.class);
  private final UsermailBoxRepo usermailBoxRepo = Mockito.mock(UsermailBoxRepo.class);
  private final UsermailMsgReplyRepo usermailMsgReplyRepo = Mockito.mock(UsermailMsgReplyRepo.class);
  private final IUsermailAdapter usermailAdapter = Mockito.mock(IUsermailAdapter.class);
  private final UsermailSessionService usermailSessionService = Mockito.mock(UsermailSessionService.class);
  private final Usermail2NotfyMqService usermail2NotfyMqService = Mockito
      .mock(Usermail2NotfyMqService.class, RETURNS_SMART_NULLS);
  private final UsermailMqService usermailMqService = Mockito.mock(UsermailMqService.class, RETURNS_SMART_NULLS);
  private final ConvertMsgService convertMsgService = Mockito.mock(ConvertMsgService.class);
  private final UsermailService usermailService = new UsermailService(
      usermailRepo, usermailBoxRepo, usermailMsgReplyRepo, usermailAdapter, usermailSessionService,
      usermail2NotfyMqService,
      usermailMqService, new MsgCompressor(), convertMsgService
  );

  private CdtpHeaderDTO headerInfo = new CdtpHeaderDTO("{CDTP-header:value}",
      "{xPacketId:value}");

  @Test
  public void saveUsermailBoxInfo() {
    String from = "from@temail.com";
    String to = "to@temail.com";
    String owner = from;
    when(usermailAdapter.getPkID()).thenReturn(1L);
    when(usermailSessionService.getSessionID(from, to)).thenReturn("sessionid");
    usermailService.saveUsermailBoxInfo(from, to, owner);
    ArgumentCaptor<UsermailBoxDO> argumentCaptor1 = ArgumentCaptor.forClass(UsermailBoxDO.class);
    verify(usermailBoxRepo).saveUsermailBox(argumentCaptor1.capture());
    UsermailBoxDO usermailBox = argumentCaptor1.getValue();
    assertEquals(to, usermailBox.getMail2());
    assertEquals(owner, usermailBox.getOwner());
  }

  @Test
  public void sendMailWhenNormal() {
    String msgid = "msgId";
    String from = "from@temail.com";
    String to = "to@temail.com";
    String owner = "from@temail.com";
    int type = 0;
    int storeType = 2;
    String msgData = "msgData";
    int attachmentSize = 100;
    int eventType = SessionEventType.EVENT_TYPE_0;
    CreateUsermailDTO createUsermailDto = new CreateUsermailDTO(msgid, from, to, type, storeType,
        msgData, attachmentSize);

    when(usermailAdapter.getPkID()).thenReturn(1L);
    when(usermailSessionService.getSessionID(from, to)).thenReturn("sessionid");
    when(usermailAdapter.getMsgSeqNo(from, to, from)).thenReturn(1L);

    usermail2NotfyMqService
        .sendMqMsgSaveMail(headerInfo, from, to, from, msgid, msgData, 1L, eventType, attachmentSize, from, null);
    usermailService.sendMail(headerInfo, createUsermailDto, owner, to);

    ArgumentCaptor<UsermailBoxDO> argumentCaptor1 = ArgumentCaptor.forClass(UsermailBoxDO.class);
    verify(usermailBoxRepo).saveUsermailBox(argumentCaptor1.capture());
    UsermailBoxDO usermailBox = argumentCaptor1.getValue();
    assertEquals(to, usermailBox.getMail2());
    assertEquals("sessionid", usermailBox.getSessionid());
    assertEquals(1L, usermailBox.getId());

    ArgumentCaptor<UsermailDO> argumentCaptor2 = ArgumentCaptor.forClass(UsermailDO.class);
    verify(usermailRepo).saveUsermail(argumentCaptor2.capture());
    UsermailDO usermail = argumentCaptor2.getValue();
    assertEquals(from, usermail.getFrom());
    assertEquals(to, usermail.getTo());
    assertEquals(msgid, usermail.getMsgid());
    //assertEquals(msgData, usermail.getMessage());
    assertEquals(type, usermail.getType());
  }

  @Test
  public void sendMailWhenDestoryAfterRead() {
    String msgid = "msgId";
    String from = "from@temail.com";
    String to = "to@temail.com";
    String owner = "from@temail.com";
    int type = TemailType.TYPE_DESTORY_AFTER_READ_1;
    int storeType = 2;
    String msgData = "msgData";
    int attachmentSize = 100;
    int eventType = SessionEventType.EVENT_TYPE_0;
    CreateUsermailDTO createUsermailDto = new CreateUsermailDTO(msgid, from, to, type, storeType,
        msgData, attachmentSize);

    when(usermailAdapter.getPkID()).thenReturn(1L);
    when(usermailSessionService.getSessionID(from, to)).thenReturn("sessionid");
    when(usermailAdapter.getMsgSeqNo(from, to, from)).thenReturn(1L);

    usermail2NotfyMqService
        .sendMqMsgSaveMail(headerInfo, from, to, from, msgid, msgData, 1L, eventType, attachmentSize, from, null);
    usermailService.sendMail(headerInfo, createUsermailDto, owner, to);

    ArgumentCaptor<UsermailBoxDO> argumentCaptor1 = ArgumentCaptor.forClass(UsermailBoxDO.class);
    verify(usermailBoxRepo).saveUsermailBox(argumentCaptor1.capture());
    UsermailBoxDO usermailBox = argumentCaptor1.getValue();
    assertEquals(to, usermailBox.getMail2());
    assertEquals("sessionid", usermailBox.getSessionid());
    assertEquals(1L, usermailBox.getId());

    ArgumentCaptor<UsermailDO> argumentCaptor2 = ArgumentCaptor.forClass(UsermailDO.class);
    verify(usermailRepo).saveUsermail(argumentCaptor2.capture());
    UsermailDO usermail = argumentCaptor2.getValue();
    assertEquals(from, usermail.getFrom());
    assertEquals(to, usermail.getTo());
    assertEquals(msgid, usermail.getMsgid());
    //assertEquals(msgData, usermail.getMessage());
    assertEquals(type, usermail.getType());
  }

  @Test
  public void sendMailWhenCrossMsg() {
    String msgid = "msgId";
    String from = "from@temail.com";
    String to = "to@temail.com";
    String owner = "from@temail.com";
    int type = TemailType.TYPE_CROSS_DOMAIN_GROUP_EVENT_2;
    int storeType = 2;
    String msgData = "msgData";
    int attachmentSize = 100;
    int eventType = SessionEventType.EVENT_TYPE_0;
    CreateUsermailDTO createUsermailDto = new CreateUsermailDTO(msgid, from, to, type, storeType,
        msgData, attachmentSize);

    when(usermailAdapter.getPkID()).thenReturn(1L);
    when(usermailSessionService.getSessionID(from, to)).thenReturn("sessionid");
    when(usermailAdapter.getMsgSeqNo(from, to, from)).thenReturn(1L);

    usermail2NotfyMqService
        .sendMqMsgSaveMail(headerInfo, from, to, from, msgid, msgData, 1L, eventType, attachmentSize, from, null);
    usermailService.sendMail(headerInfo, createUsermailDto, owner, to);

    ArgumentCaptor<UsermailBoxDO> argumentCaptor1 = ArgumentCaptor.forClass(UsermailBoxDO.class);
    verify(usermailBoxRepo).saveUsermailBox(argumentCaptor1.capture());
    UsermailBoxDO usermailBox = argumentCaptor1.getValue();
    assertEquals(to, usermailBox.getMail2());
    assertEquals("sessionid", usermailBox.getSessionid());
    assertEquals(1L, usermailBox.getId());

    ArgumentCaptor<UsermailDO> argumentCaptor2 = ArgumentCaptor.forClass(UsermailDO.class);
    verify(usermailRepo).saveUsermail(argumentCaptor2.capture());
    UsermailDO usermail = argumentCaptor2.getValue();
    assertEquals(from, usermail.getFrom());
    assertEquals(to, usermail.getTo());
    assertEquals(msgid, usermail.getMsgid());
    //assertEquals(msgData, usermail.getMessage());
    assertEquals(type, usermail.getType());
  }

  @Test
  public void getMailsWhenFilterSeqIdsIsEmpty() {
    String from = "from@temail.com";
    String to = "to@temail.com";
    long fromSeqNo = 0L;
    int pageSize = 10;
    String filterSeqIds = "";
    when(usermailSessionService.getSessionID(from, to)).thenReturn("sessionid");
    UmQueryDTO umQueryDto = new UmQueryDTO();
    umQueryDto.setFromSeqNo(fromSeqNo);
    umQueryDto.setSessionid("sessionid");
    umQueryDto.setPageSize(pageSize);
    umQueryDto.setOwner(from);
    List<UsermailDO> usermails = new ArrayList<>();
    UsermailDO usermail1 = new UsermailDO();
    usermails.add(usermail1);
    when(usermailRepo.getUsermail(any())).thenReturn(usermails);
    List<UsermailDO> list = usermailService.getMails(from, to, fromSeqNo, pageSize, filterSeqIds, "before");
    assertNotNull(list);
  }

  @Test
  public void getMailsWhenFilterSeqIdsIsNotEmpty() {
    String from = "from@temail.com";
    String to = "to@temail.com";
    long fromSeqNo = 0L;
    int pageSize = 10;
    // 过去seqNo=1(包含1)的消息，‘-1’表示不限
    String filterSeqIds = "1_-1";
    String sessionid = "sessionid";
    when(usermailSessionService.getSessionID(from, to)).thenReturn(sessionid);
    UmQueryDTO umQueryDto = new UmQueryDTO();
    umQueryDto.setFromSeqNo(fromSeqNo);
    umQueryDto.setSessionid(sessionid);
    umQueryDto.setPageSize(pageSize);
    umQueryDto.setOwner(from);
    List<UsermailDO> usermails = new ArrayList<>();
    UsermailDO usermail_1 = new UsermailDO();
    usermail_1.setId(1L);
    usermail_1.setFrom(from);
    usermail_1.setTo(to);
    usermail_1.setOwner(from);
    usermail_1.setSeqNo(0);
    UsermailDO usermail_2 = new UsermailDO();
    usermail_2.setId(2L);
    usermail_2.setFrom(from);
    usermail_2.setTo(to);
    usermail_2.setOwner(from);
    usermail_2.setSeqNo(1);
    usermails.add(usermail_1);
    usermails.add(usermail_2);

    when(convertMsgService.convertMsg(any())).thenReturn(usermails);

    List<UsermailDO> list = usermailService.getMails(from, to, fromSeqNo, pageSize, filterSeqIds, "before");

    assertNotNull(list);
    assertThat(list.size()).isOne();
    assertThat(list.get(0).getSeqNo()).isEqualTo(usermail_1.getSeqNo());
  }

  @Test
  public void revert() {
    String cdtpheader = "cdtpheader";
    String packetId = "packetId";
    CdtpHeaderDTO headerDTO = new CdtpHeaderDTO(cdtpheader, packetId);
    String from = "from";
    String to = "to";
    String msgid = "msgid";

    usermailService.revert(headerDTO, from, to, msgid);

    verify(usermailMqService).sendMqRevertMsg(packetId, cdtpheader, from, to, to, msgid);
    verify(usermailMqService)
        .sendMqRevertMsg(packetId + ParamsKey.SessionEventKey.PACKET_ID_SUFFIX, cdtpheader, from, to, from, msgid);
  }

  @Test
  public void revertMqHandler() {
    String xPacketId = UUID.randomUUID().toString();
    String header = "CDTP-header";
    String from = "from@temail.com";
    String to = "to@temail.com";
    String owner = from;
    String msgid = "msgid";
    when(usermailRepo.revertUsermail(any(RevertMailDTO.class))).thenReturn(1);
    usermailService.revertMqHandler(xPacketId, header, from, to, owner, msgid);
    ArgumentCaptor<RevertMailDTO> revertDtoCapture = ArgumentCaptor.forClass(RevertMailDTO.class);
    verify(usermailRepo).revertUsermail(revertDtoCapture.capture());
    RevertMailDTO revertMailDto = revertDtoCapture.getValue();
    assertEquals(from, revertMailDto.getOwner());
    assertEquals(msgid, revertMailDto.getMsgid());
    verify(usermail2NotfyMqService)
        .sendMqUpdateMsg(xPacketId, header, from, to, owner, msgid, SessionEventType.EVENT_TYPE_2);

    // 验证撤回失败的情况
    when(usermailRepo.revertUsermail(any(RevertMailDTO.class))).thenReturn(0);
    usermailService.revertMqHandler(xPacketId, header, from, to, owner, msgid);
    verify(usermail2NotfyMqService, times(1))
        .sendMqUpdateMsg(xPacketId, header, from, to, owner, msgid, SessionEventType.EVENT_TYPE_2);
  }

  @Test
  public void mailboxes() {
    String from = "from@temail.com";
    int archiveStatus = TemailArchiveStatus.STATUS_NORMAL_0;
    String to_1 = "to_1";
    String localMsgid_1 = "msgid_1";
    String sessionid_1 = "sessionid_1";
    String to_2 = "to_2";
    String localMsgid_2 = "msgid_2";
    String sessionod_2 = "sessionod_2";
    Map<String, String> localMailBoxes = ImmutableMap.of(to_1, localMsgid_1, to_2, localMsgid_2);
    UsermailBoxDO box_1 = new UsermailBoxDO(1L, sessionid_1, to_1, from);
    UsermailBoxDO box_2 = new UsermailBoxDO(2L, sessionod_2, to_2, from);
    when(usermailBoxRepo.getUsermailBoxByOwner(from, archiveStatus)).thenReturn(Arrays.asList(box_1, box_2));
    when(usermailAdapter.getLastMsgId(from, to_1)).thenReturn(localMsgid_1);
    when(usermailAdapter.getLastMsgId(from, to_2)).thenReturn("msgid_other");
    UsermailDO lastUsermail_to_2 = new UsermailDO(1L, "msgid_2_actualLast", sessionod_2, from, to_2,
        TemailStatus.STATUS_NORMAL_0, TemailType.TYPE_NORMAL_0, from, "", 3);
    when(convertMsgService.convertMsg(any())).thenReturn(Arrays.asList(lastUsermail_to_2));

    List<MailboxDTO> list = usermailService.mailboxes(from, 0, localMailBoxes);
    assertNotNull(list);
    assertThat(list.size()).isOne();
    assertThat(list.get(0).getTo()).isEqualTo(to_2);
    assertThat(list.get(0).getLastMsg()).isEqualToComparingFieldByField(lastUsermail_to_2);
  }

  @Test
  public void removeMsgExcludeLast() {
    String from = "from@temail.com";
    String to = "to@temail.com";
    List<String> msgIds = new ArrayList<>();
    msgIds.add("ldfk");
    msgIds.add("syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707");

    String dbLastMsgid = "dbLastMsgid";
    UsermailDO lastUsermail = new UsermailDO(1L, dbLastMsgid, "sessionid", from, to,
        TemailStatus.STATUS_NORMAL_0, TemailType.TYPE_NORMAL_0, from, "", 3);
    when(usermailRepo.getLastUsermail(any(UmQueryDTO.class))).thenReturn(Arrays.asList(lastUsermail));
    when(usermailAdapter.getLastMsgId(from, to)).thenReturn("cacheLaseMsgid");

    usermailService.removeMsg(headerInfo, from, to, msgIds);

    verify(usermailRepo).removeMsg(msgIds, from);
    verify(usermailMsgReplyRepo).deleteMsgByParentIdAndOwner(from, msgIds);
    verify(usermail2NotfyMqService)
        .sendMqAfterUpdateStatus(eq(headerInfo), eq(from), eq(to), anyString(), eq(SessionEventType.EVENT_TYPE_4));
    verify(usermailSessionService).getSessionID(from, to);
    verify(usermailRepo).getLastUsermail(any(UmQueryDTO.class));
    verify(usermailAdapter).getLastMsgId(from, to);
    verify(usermailAdapter).setLastMsgId(from, to, dbLastMsgid);
  }

  @Test
  public void removeMsgIncludeLast() {
    String from = "from@temail.com";
    String to = "to@temail.com";
    List<String> msgIds = new ArrayList<>();
    msgIds.add("ldfk");
    msgIds.add("syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707");
    when(usermailRepo.getLastUsermail(any(UmQueryDTO.class))).thenReturn(null);

    usermailService.removeMsg(headerInfo, from, to, msgIds);

    verify(usermailRepo).removeMsg(msgIds, from);
    verify(usermailMsgReplyRepo).deleteMsgByParentIdAndOwner(from, msgIds);
    verify(usermail2NotfyMqService)
        .sendMqAfterUpdateStatus(eq(headerInfo), eq(from), eq(to), anyString(), eq(SessionEventType.EVENT_TYPE_4));
    verify(usermailSessionService).getSessionID(from, to);
    verify(usermailRepo).getLastUsermail(any(UmQueryDTO.class));
    verify(usermailAdapter).deleteLastMsgId(from, to);
  }

  @Test
  public void destoryAfterRead() {
    String header = "CDTP-header";
    String from = "from@temail.com";
    String to = "to@temail.com";
    String msgId = "msgid";
    UsermailDO usermail = new UsermailDO();
    usermail.setType(TYPE_DESTORY_AFTER_READ_1);
    usermail2NotfyMqService.sendMqAfterUpdateStatus(headerInfo, to, from, msgId, SessionEventType.EVENT_TYPE_3);
    usermailService.destroyAfterRead(headerInfo, from, to, msgId);
    //调用"阅后即焚已读"接口，已变更为只发MQ消息，不对数据库中的数据进行变更。此处不需要做任何验证
    Assert.assertTrue(true);
  }

  @Test
  public void shouldDestoryAfterRead() {
    String xPacketId = UUID.randomUUID().toString();
    String header = "CDTP-header";
    String msgId = "12345";
    String from = "from@msgseal.com";
    String to = "to@t.email";
    String author = "from@msgseal.com";
    UsermailDO usermail = new UsermailDO(22, msgId, "", from, "", 0, TemailType.TYPE_DESTORY_AFTER_READ_1, from, "", 1,
        "".getBytes(), author, null);
    when(usermailRepo.getUsermailByMsgid(msgId, from)).thenReturn(usermail);
    usermailService.destroyAfterRead(xPacketId, header, from, to, from, msgId);

    ArgumentCaptor<String> fromCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> msgIdCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(usermailRepo).destoryAfterRead(fromCaptor.capture(), msgIdCaptor.capture(), statusCaptor.capture());
    int status = statusCaptor.getValue();
    assertEquals(from, fromCaptor.getValue());
    assertEquals(msgId, msgIdCaptor.getValue());
    assertEquals(TemailStatus.STATUS_DESTORY_AFTER_READ_2, status);
  }

  @Test
  public void batchQueryMsgs() {
    String from = "from@systoontest.com";
    String to = "to@systoontest.com";
    List<String> msgIds = new ArrayList<>();
    msgIds.add("6d19b1c4-2665-49aa-801b-eb40bc10f532");
    msgIds.add("74e3bccd-b894-42d0-b535-9cea0824147d");
    msgIds.add("7a14838f-c003-41eb-b360-814265e4fcda");
    List<UsermailDO> expectMailList = new ArrayList<>();
    List<UsermailDO> actualMailList;

    UsermailDO mail1 = new UsermailDO();
    UsermailDO mail2 = new UsermailDO();
    UsermailDO mail3 = new UsermailDO();
    mail1.setMsgid(msgIds.get(0));
    mail2.setMsgid(msgIds.get(1));
    mail3.setMsgid(msgIds.get(2));
    expectMailList.add(mail1);
    expectMailList.add(mail2);
    expectMailList.add(mail3);

    Mockito.when(usermailRepo.getUsermailByFromToMsgIds(from, msgIds)).thenReturn(expectMailList);
    Mockito.when(convertMsgService.convertMsg(expectMailList)).thenReturn(expectMailList);
    actualMailList = usermailService.batchQueryMsgs(from, msgIds);
    Assert.assertEquals(actualMailList, expectMailList);
  }

  @Test
  public void batchQueryMsgsReplyCount() {
    String from = "from@systoontest.com";
    String to = "to@systoontest.com";
    List<String> msgIds = new ArrayList<>();
    msgIds.add("6d19b1c4-2665-49aa-801b-eb40bc10f532");
    msgIds.add("74e3bccd-b894-42d0-b535-9cea0824147d");
    msgIds.add("7a14838f-c003-41eb-b360-814265e4fcda");
    List<UsermailDO> expectMailList = new ArrayList<>();
    List<UsermailDO> actualMailList;

    UsermailDO mail1 = new UsermailDO();
    UsermailDO mail2 = new UsermailDO();
    UsermailDO mail3 = new UsermailDO();
    mail1.setMsgid(msgIds.get(0));
    mail1.setMessage("test");
    mail2.setMsgid(msgIds.get(1));
    mail2.setMessage("test");
    mail3.setMsgid(msgIds.get(2));
    mail3.setMessage("test");
    expectMailList.add(mail1);
    expectMailList.add(mail2);
    expectMailList.add(mail3);

    Mockito.when(usermailRepo.getUsermailByFromToMsgIds(from, msgIds)).thenReturn(expectMailList);

    actualMailList = usermailService.batchQueryMsgsReplyCount(from, msgIds);
    Assert.assertEquals(actualMailList, expectMailList);
  }

  @Test
  public void deleteSession() {
    String from = "from@msgseal.com";
    String to = "to@msgseal.com";
    String sessionID = "sessionId";
    when(usermailSessionService.getSessionID(to, from)).thenReturn(sessionID);
    DeleteMailBoxQueryDTO queryDto = new DeleteMailBoxQueryDTO(from, to, true);
    usermailService.deleteSession(headerInfo, queryDto);
    verify(usermail2NotfyMqService)
        .sendMqAfterDeleteSession(headerInfo, from, to, queryDto.isDeleteAllMsg(), SessionEventType.EVENT_TYPE_4);

    ArgumentCaptor<String> fromCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
    verify(usermailBoxRepo).deleteByOwnerAndTo(fromCaptor.capture(), toCaptor.capture());
    assertEquals(from, fromCaptor.getValue());
    assertEquals(to, toCaptor.getValue());

    ArgumentCaptor<String> sessIdCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> fromCaptor1 = ArgumentCaptor.forClass(String.class);
    verify(usermailRepo).batchDeleteBySessionId(sessIdCaptor.capture(), fromCaptor1.capture());
    String from1 = fromCaptor1.getValue();
    String sessionid = sessIdCaptor.getValue();
    assertEquals(from, from1);
    assertEquals(sessionID, sessionid);

    ArgumentCaptor<String> sessIdCaptor2 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> fromCaptor2 = ArgumentCaptor.forClass(String.class);
    verify(usermailMsgReplyRepo).batchDeleteBySessionId(sessIdCaptor2.capture(), fromCaptor2.capture());
    String from2 = fromCaptor2.getValue();
    String sessionid2 = sessIdCaptor2.getValue();
    assertEquals(from, from2);
    assertEquals(sessionID, sessionid2);

    ArgumentCaptor<String> fromCaptor3 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> toCaptor3 = ArgumentCaptor.forClass(String.class);
    verify(usermailAdapter).deleteLastMsgId(fromCaptor3.capture(), toCaptor3.capture());
    assertEquals(from, fromCaptor3.getValue());
    assertEquals(to, toCaptor3.getValue());
  }


  @Test
  public void moveMsgToTrash() {
    String from = "from@msgseal.com";
    String to = "to@msgseal.com";
    List<String> msgIds = new ArrayList<>();
    msgIds.add("aa");
    msgIds.add("bb");
    usermailService.moveMsgToTrash(headerInfo, from, to, msgIds);
    verify(usermail2NotfyMqService).sendMqMoveTrashNotify(headerInfo, from, to, msgIds, SessionEventType.EVENT_TYPE_35);
    ArgumentCaptor<List<String>> msgIdCaptor = ArgumentCaptor.forClass(List.class);
    ArgumentCaptor<String> fromCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(usermailRepo).updateStatusByMsgIds(msgIdCaptor.capture(), fromCaptor.capture(), statusCaptor.capture());
    int status = statusCaptor.getValue();
    assertEquals(msgIds, msgIdCaptor.getValue());
    assertEquals(from, fromCaptor.getValue());
    assertEquals(TemailStatus.STATUS_TRASH_4, status);

    ArgumentCaptor<String> fromCaptor2 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<String>> msgIdCaptor2 = ArgumentCaptor.forClass(List.class);
    ArgumentCaptor<Integer> statusCaptor2 = ArgumentCaptor.forClass(Integer.class);
    verify(usermailMsgReplyRepo)
        .batchUpdateByParentMsgIds(fromCaptor2.capture(), msgIdCaptor2.capture(), statusCaptor2.capture());
    int status2 = statusCaptor2.getValue();
    assertEquals(msgIds, msgIdCaptor2.getValue());
    assertEquals(from, fromCaptor2.getValue());
    assertEquals(TemailStatus.STATUS_TRASH_4, status2);
  }

  @Test
  public void revertMsgToTrash() {
    String temail = "from@msgseal.com";
    String to = "to@msgseal.com";
    List<TrashMailDTO> trashMailDtos = Arrays.asList(
        new TrashMailDTO(temail, to, "12"),
        new TrashMailDTO(temail, to, "122")
    );
    List<String> msgIds = new ArrayList<>();
    msgIds.add(trashMailDtos.get(0).getMsgId());
    msgIds.add(trashMailDtos.get(1).getMsgId());
    usermailService.revertMsgFromTrash(headerInfo, temail, trashMailDtos);
    verify(usermail2NotfyMqService)
        .sendMqTrashMsgOpratorNotify(headerInfo, temail, trashMailDtos, SessionEventType.EVENT_TYPE_36);
    ArgumentCaptor<List<TrashMailDTO>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
    ArgumentCaptor<String> temailCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(usermailRepo)
        .revertMsgFromTrash(listArgumentCaptor.capture(), temailCaptor.capture(), statusCaptor.capture());
    int status = statusCaptor.getValue();
    assertEquals(trashMailDtos, listArgumentCaptor.getValue());
    assertEquals(temail, temailCaptor.getValue());
    assertEquals(TemailStatus.STATUS_NORMAL_0, status);

    ArgumentCaptor<String> temailCaptor2 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<String>> msgIdCaptor = ArgumentCaptor.forClass(List.class);
    ArgumentCaptor<Integer> statusCaptor2 = ArgumentCaptor.forClass(Integer.class);
    verify(usermailMsgReplyRepo)
        .batchUpdateByParentMsgIds(temailCaptor2.capture(), msgIdCaptor.capture(), statusCaptor2.capture());
    int status2 = statusCaptor2.getValue();
    assertEquals(temail, temailCaptor2.getValue());
    assertEquals(msgIds, msgIdCaptor.getValue());
    assertEquals(TemailStatus.STATUS_NORMAL_0, status2);
  }

  @Test
  public void removeMsgFromTrash() {
    String temail = "from@msgseal.com";
    String to = "to@msgseal.com";
    List<TrashMailDTO> trashMailDtos = Arrays.asList(
        new TrashMailDTO(temail, to, "12"),
        new TrashMailDTO(temail, to, "122")
    );
    List<String> msgIds = new ArrayList<>();
    msgIds.add(trashMailDtos.get(0).getMsgId());
    msgIds.add(trashMailDtos.get(1).getMsgId());
    usermailService.removeMsgFromTrash(temail, trashMailDtos);

    ArgumentCaptor<List<TrashMailDTO>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
    ArgumentCaptor<String> temailCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(usermailRepo)
        .removeMsgByStatus(listArgumentCaptor.capture(), temailCaptor.capture(), statusCaptor.capture());
    int status = statusCaptor.getValue();
    assertEquals(trashMailDtos, listArgumentCaptor.getValue());
    assertEquals(temail, temailCaptor.getValue());
    assertEquals(TemailStatus.STATUS_TRASH_4, status);

    ArgumentCaptor<String> temailCaptor2 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<String>> msgIdCaptor = ArgumentCaptor.forClass(List.class);
    verify(usermailMsgReplyRepo).deleteMsgByParentIdAndOwner(temailCaptor2.capture(), msgIdCaptor.capture());
    assertEquals(temail, temailCaptor2.getValue());
    assertEquals(msgIds, msgIdCaptor.getValue());
  }

  @Test
  public void removeMsgFromTrash2() {
    String temail = "from@msgseal.com";
    String to = "to@msgseal.com";
    List<TrashMailDTO> trashMailDtos = Arrays.asList(
        new TrashMailDTO(temail, to, "12"),
        new TrashMailDTO(temail, to, "122")
    );
    usermailService.removeMsgFromTrash(headerInfo, temail, trashMailDtos);
    verify(usermail2NotfyMqService)
        .sendMqTrashMsgOpratorNotify(headerInfo, temail, trashMailDtos, SessionEventType.EVENT_TYPE_37);
    ArgumentCaptor<String> temailCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<TrashMailDTO>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
    ArgumentCaptor<Integer> typeCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(usermailMqService)
        .sendMqRemoveTrash(temailCaptor.capture(), listArgumentCaptor.capture(), typeCaptor.capture());
    int type = typeCaptor.getValue();
    assertEquals(trashMailDtos, listArgumentCaptor.getValue());
    assertEquals(temail, temailCaptor.getValue());
    assertEquals(UsermailAgentEventType.TRASH_REMOVE_0, type);
  }

  @Test
  public void clearMsgFromTrash() {
    String temail = "from@msgseal.com";
    usermailService.clearMsgFromTrash(temail);

    ArgumentCaptor<List<TrashMailDTO>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
    ArgumentCaptor<String> temailCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(usermailRepo)
        .removeMsgByStatus(listArgumentCaptor.capture(), temailCaptor.capture(), statusCaptor.capture());
    int status = statusCaptor.getValue();
    assertEquals(null, listArgumentCaptor.getValue());
    assertEquals(temail, temailCaptor.getValue());
    assertEquals(TemailStatus.STATUS_TRASH_4, status);

    ArgumentCaptor<String> temailCaptor2 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> statusCaptor2 = ArgumentCaptor.forClass(Integer.class);
    verify(usermailMsgReplyRepo).batchDeleteByStatus(temailCaptor2.capture(), statusCaptor2.capture());
    int status2 = statusCaptor2.getValue();
    assertEquals(temail, temailCaptor2.getValue());
    assertEquals(TemailStatus.STATUS_TRASH_4, status2);
  }

  @Test
  public void getMsgFromTrash() {
    String temail = "from@msgseal.com";
    long timestamp = 11111;
    int pageSize = 20;
    String signal = "before";
    QueryTrashDTO queryTrashDto = new QueryTrashDTO(new Timestamp(timestamp), pageSize, TemailStatus.STATUS_TRASH_4,
        signal, temail);
    List<UsermailDO> result = Arrays.asList(
        new UsermailDO(22, "111", "", temail, "", 0, TemailType.TYPE_NORMAL_0, temail, "", 0, "".getBytes(), temail,
            null)
    );
    when(usermailRepo.getUsermailByStatus((queryTrashDto))).thenReturn(result);
    when(convertMsgService.convertMsg(result)).thenReturn(result);
    List<UsermailDO> msgFromTrash = usermailService.getMsgFromTrash(temail, timestamp, pageSize, signal);
    ArgumentCaptor<QueryTrashDTO> trashCaptor = ArgumentCaptor.forClass(QueryTrashDTO.class);
    verify(usermailRepo).getUsermailByStatus(trashCaptor.capture());
    QueryTrashDTO dtos = trashCaptor.getValue();
    assertEquals(temail, dtos.getOwner());
    assertEquals(msgFromTrash, result);

  }

  @Test
  public void updateUsermailBoxToArchive() {
    String from = "from@msgseal.com";
    String to = "to@msgseal.com";
    int archiveStatus = TemailArchiveStatus.STATUS_ARCHIVE_1;
    usermailService.updateUsermailBoxArchiveStatus(headerInfo, from, to, archiveStatus);
    verify(usermail2NotfyMqService)
        .sendMqAfterUpdateArchiveStatus(headerInfo, from, to, SessionEventType.EVENT_TYPE_33);
    ArgumentCaptor<String> fromCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(usermailBoxRepo).updateArchiveStatus(fromCaptor.capture(), toCaptor.capture(), statusCaptor.capture());
    int status = statusCaptor.getValue();
    assertEquals(from, fromCaptor.getValue());
    assertEquals(to, toCaptor.getValue());
    assertEquals(archiveStatus, status);
  }

  @Test
  public void updateUsermailBoxCancelArchive() {
    String from = "from@msgseal.com";
    String to = "to@msgseal.com";
    int archiveStatus = TemailArchiveStatus.STATUS_NORMAL_0;
    usermailService.updateUsermailBoxArchiveStatus(headerInfo, from, to, archiveStatus);
    verify(usermail2NotfyMqService)
        .sendMqAfterUpdateArchiveStatus(headerInfo, from, to, SessionEventType.EVENT_TYPE_34);
    ArgumentCaptor<String> fromCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> toCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(usermailBoxRepo).updateArchiveStatus(fromCaptor.capture(), toCaptor.capture(), statusCaptor.capture());
    int status = statusCaptor.getValue();
    assertEquals(from, fromCaptor.getValue());
    assertEquals(to, toCaptor.getValue());
    assertEquals(archiveStatus, status);
  }

  @Test(expected = IllegalGmArgsException.class)
  public void failUpdateUsermailBoxArchiveStatus() {
    String from = "from@msgseal.com";
    String to = "to@msgseal.com";
    int archiveStatus = 2;
    usermailService.updateUsermailBoxArchiveStatus(headerInfo, from, to, archiveStatus);
    verify(usermailBoxRepo).updateArchiveStatus(from, to, archiveStatus);
  }

}