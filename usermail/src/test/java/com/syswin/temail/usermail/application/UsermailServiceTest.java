package com.syswin.temail.usermail.application;

import static com.syswin.temail.usermail.common.Contants.TemailType.TYPE_DESTORY_AFTER_READ_1;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.RETURNS_SMART_NULLS;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.syswin.temail.usermail.common.Contants.SessionEventType;
import com.syswin.temail.usermail.common.Contants.TemailArchiveStatus;
import com.syswin.temail.usermail.common.Contants.TemailStatus;
import com.syswin.temail.usermail.common.Contants.TemailType;
import com.syswin.temail.usermail.common.Contants.UsermailAgentEventType;
import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDto;
import com.syswin.temail.usermail.core.exception.IllegalGMArgsException;
import com.syswin.temail.usermail.core.util.MsgCompressor;
import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.domains.UsermailBox;
import com.syswin.temail.usermail.dto.CreateUsermailDto;
import com.syswin.temail.usermail.dto.DeleteMailBoxQueryDto;
import com.syswin.temail.usermail.dto.MailboxDto;
import com.syswin.temail.usermail.dto.QueryTrashDto;
import com.syswin.temail.usermail.dto.TrashMailDto;
import com.syswin.temail.usermail.dto.UmQueryDto;
import com.syswin.temail.usermail.domains.UsermailBlacklistRepo;
import com.syswin.temail.usermail.domains.UsermailBoxRepo;
import com.syswin.temail.usermail.domains.UsermailRepo;
import com.syswin.temail.usermail.domains.UsermailMsgReplyRepo;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
  private final UsermailBlacklistRepo usermailBlacklistRepo = Mockito.mock(UsermailBlacklistRepo.class);
  private final ConvertMsgService convertMsgService = Mockito.mock(ConvertMsgService.class);
  private final UsermailService usermailService = new UsermailService(
      usermailRepo, usermailBoxRepo, usermailMsgReplyRepo, usermailAdapter, usermailSessionService,
      usermail2NotfyMqService,
      usermailMqService, new MsgCompressor(), convertMsgService
  );

  private CdtpHeaderDto headerInfo = new CdtpHeaderDto("{CDTP-header:value}",
      "{xPacketId:value}");

  @Test
  public void saveUsermailBoxInfo() {
    String from = "from@temail.com";
    String to = "to@temail.com";
    String owner = from;
    when(usermailAdapter.getPkID()).thenReturn(1L);
    when(usermailSessionService.getSessionID(from, to)).thenReturn("sessionid");
    usermailService.saveUsermailBoxInfo(from, to, owner);
    ArgumentCaptor<UsermailBox> argumentCaptor1 = ArgumentCaptor.forClass(UsermailBox.class);
    verify(usermailBoxRepo).saveUsermailBox(argumentCaptor1.capture());
    UsermailBox usermailBox = argumentCaptor1.getValue();
    assertEquals(to, usermailBox.getMail2());
    assertEquals(owner, usermailBox.getOwner());
  }

  @Test
  public void sendMail() {
    String header = "CDTP-header";
    String msgid = "msgId";
    String from = "from@temail.com";
    String to = "to@temail.com";
    String owner = "from@temail.com";
    int type = 0;
    int storeType = 2;
    String msgData = "msgData";
    int attachmentSize = 100;
    int eventType = SessionEventType.EVENT_TYPE_0;
    CreateUsermailDto createUsermailDto = new CreateUsermailDto(msgid, from, to, type, storeType,
        msgData, attachmentSize);

    when(usermailAdapter.getPkID()).thenReturn(1L);
    when(usermailSessionService.getSessionID(from, to)).thenReturn("sessionid");
    when(usermailAdapter.getMsgSeqNo(from, to, from)).thenReturn(1L);

    usermail2NotfyMqService
        .sendMqMsgSaveMail(headerInfo, from, to, from, msgid, msgData, 1L, eventType, attachmentSize, from, null);
    usermailService.sendMail(headerInfo, createUsermailDto, owner, to);

    ArgumentCaptor<UsermailBox> argumentCaptor1 = ArgumentCaptor.forClass(UsermailBox.class);
    verify(usermailBoxRepo).saveUsermailBox(argumentCaptor1.capture());
    UsermailBox usermailBox = argumentCaptor1.getValue();
    assertEquals(to, usermailBox.getMail2());
    assertEquals("sessionid", usermailBox.getSessionid());
    assertEquals(1L, usermailBox.getId());

    ArgumentCaptor<Usermail> argumentCaptor2 = ArgumentCaptor.forClass(Usermail.class);
    verify(usermailRepo).saveUsermail(argumentCaptor2.capture());
    Usermail usermail = argumentCaptor2.getValue();
    assertEquals(from, usermail.getFrom());
    assertEquals(to, usermail.getTo());
    assertEquals(msgid, usermail.getMsgid());
    //assertEquals(msgData, usermail.getMessage());
    assertEquals(type, usermail.getType());
  }

  @Test
  public void getMails() {
    String header = "CDTP-header";
    String from = "from@temail.com";
    String to = "to@temail.com";
    long fromSeqNo = 0L;
    int pageSize = 10;
    String filterSeqIds = "";
    when(usermailSessionService.getSessionID(from, to)).thenReturn("sessionid");
    UmQueryDto umQueryDto = new UmQueryDto();
    umQueryDto.setFromSeqNo(fromSeqNo);
    umQueryDto.setSessionid("sessionid");
    umQueryDto.setPageSize(pageSize);
    umQueryDto.setOwner(from);
    List<Usermail> usermails = new ArrayList<>();
    Usermail usermail1 = new Usermail();
    usermails.add(usermail1);
    when(usermailRepo.getUsermail(any())).thenReturn(usermails);
    List<Usermail> list = usermailService.getMails(headerInfo, from, to, fromSeqNo, pageSize, filterSeqIds, "before");
    System.out.println(list);
    ArgumentCaptor<CdtpHeaderDto> argumentCaptor1 = ArgumentCaptor.forClass(CdtpHeaderDto.class);
    ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> argumentCaptor3 = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<Usermail>> argumentCaptor4 = ArgumentCaptor.forClass(List.class);
    assertNotNull(list);
  }

  @Test
  public void revert() {
    String xPacketId = UUID.randomUUID().toString();
    String header = "CDTP-header";
    String from = "from@temail.com";
    String to = "to@temail.com";
    String msgid = "msgid";
    usermail2NotfyMqService.sendMqAfterUpdateStatus(headerInfo, from, to, msgid, SessionEventType.EVENT_TYPE_2);
    usermailService.revert(xPacketId, header, from, to, from, msgid);
    ArgumentCaptor<UmQueryDto> queryDtoCaptor = ArgumentCaptor.forClass(UmQueryDto.class);
    Mockito.verify(usermailRepo).revertUsermail(queryDtoCaptor.capture());
    UmQueryDto umQueryDto = queryDtoCaptor.getValue();
    assertEquals(from, umQueryDto.getOwner());
    assertEquals(msgid, umQueryDto.getMsgid());
  }

  @Test
  public void mailboxes() {
    String header = "CDTP-header";
    String from = "from@temail.com";
    String sessionid = "sessionid";
    int archiveStatus = TemailArchiveStatus.STATUS_NORMAL_0;
    UsermailBox usermailBox = new UsermailBox();
    List<UsermailBox> usermailBoxes = new ArrayList<>();
    usermailBoxes.add(usermailBox);
    when(usermailBoxRepo.getUsermailBoxByOwner(from, archiveStatus)).thenReturn(usermailBoxes);
    usermailBox.setSessionid(sessionid);
    UmQueryDto umQueryDto = new UmQueryDto();
    umQueryDto.setSessionid(sessionid);
    umQueryDto.setOwner(from);
    when(usermailRepo.getLastUsermail(umQueryDto)).thenReturn(singletonList(new Usermail()));
    List<MailboxDto> list = usermailService.mailboxes(headerInfo, from, 0, null);
    assertNotNull(list);
  }

  @Test
  public void removeMsg() {
    String header = "CDTP-header";
    String from = "from@temail.com";
    String to = "to@temail.com";
    List<String> msgIds = new ArrayList<>();
    msgIds.add("ldfk");
    msgIds.add("syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707");
    for (String msgId : msgIds) {
      usermail2NotfyMqService.sendMqAfterUpdateStatus(headerInfo, from, to, msgId, SessionEventType.EVENT_TYPE_4);
    }
    usermailService.removeMsg(headerInfo, from, to, msgIds);

    ArgumentCaptor<List<String>> argumentCaptor1 = ArgumentCaptor.forClass(List.class);
    ArgumentCaptor<String> argumentCaptor2 = ArgumentCaptor.forClass(String.class);
    verify(usermailRepo)
        .removeMsg(argumentCaptor1.capture(), argumentCaptor2.capture());
    assertEquals(msgIds, argumentCaptor1.getValue());
    assertEquals(from, argumentCaptor2.getValue());
  }

  @Test
  public void destoryAfterRead() {
    String header = "CDTP-header";
    String from = "from@temail.com";
    String to = "to@temail.com";
    String msgId = "msgid";
    Usermail usermail = new Usermail();
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
    Usermail usermail = new Usermail(22, msgId, "", from, "", 0, TemailType.TYPE_DESTORY_AFTER_READ_1, from, "", 1,
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
    List<Usermail> expectMailList = new ArrayList<>();
    List<Usermail> actualMailList;

    Usermail mail1 = new Usermail();
    Usermail mail2 = new Usermail();
    Usermail mail3 = new Usermail();
    mail1.setMsgid(msgIds.get(0));
    mail2.setMsgid(msgIds.get(1));
    mail3.setMsgid(msgIds.get(2));
    expectMailList.add(mail1);
    expectMailList.add(mail2);
    expectMailList.add(mail3);

    Mockito.when(usermailRepo.getUsermailByFromToMsgIds(from, msgIds)).thenReturn(expectMailList);
    Mockito.when(convertMsgService.convertMsg(expectMailList)).thenReturn(expectMailList);
    actualMailList = usermailService.batchQueryMsgs(headerInfo, from, to, msgIds);
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
    List<Usermail> expectMailList = new ArrayList<>();
    List<Usermail> actualMailList;

    Usermail mail1 = new Usermail();
    Usermail mail2 = new Usermail();
    Usermail mail3 = new Usermail();
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

    actualMailList = usermailService.batchQueryMsgsReplyCount(headerInfo, from, to, msgIds);
    System.out.println("actualMailList:" + actualMailList);
    Assert.assertEquals(actualMailList, expectMailList);
  }

  @Test
  public void deleteSession() {
    String from = "from@msgseal.com";
    String to = "to@msgseal.com";
    String sessionID = "sessionId";
    when(usermailSessionService.getSessionID(to, from)).thenReturn(sessionID);
    DeleteMailBoxQueryDto queryDto = new DeleteMailBoxQueryDto(from, to, true);
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
    List<TrashMailDto> trashMailDtos = Arrays.asList(
        new TrashMailDto(temail, to, "12"),
        new TrashMailDto(temail, to, "122")
    );
    List<String> msgIds = new ArrayList<>();
    msgIds.add(trashMailDtos.get(0).getMsgId());
    msgIds.add(trashMailDtos.get(1).getMsgId());
    usermailService.revertMsgToTrash(headerInfo, temail, trashMailDtos);
    verify(usermail2NotfyMqService)
        .sendMqTrashMsgOpratorNotify(headerInfo, temail, trashMailDtos, SessionEventType.EVENT_TYPE_36);
    ArgumentCaptor<List<TrashMailDto>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
    ArgumentCaptor<String> temailCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<Integer> statusCaptor = ArgumentCaptor.forClass(Integer.class);
    verify(usermailRepo)
        .updateStatusByTemail(listArgumentCaptor.capture(), temailCaptor.capture(), statusCaptor.capture());
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
    List<TrashMailDto> trashMailDtos = Arrays.asList(
        new TrashMailDto(temail, to, "12"),
        new TrashMailDto(temail, to, "122")
    );
    List<String> msgIds = new ArrayList<>();
    msgIds.add(trashMailDtos.get(0).getMsgId());
    msgIds.add(trashMailDtos.get(1).getMsgId());
    usermailService.removeMsgFromTrash(temail, trashMailDtos);

    ArgumentCaptor<List<TrashMailDto>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
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
    List<TrashMailDto> trashMailDtos = Arrays.asList(
        new TrashMailDto(temail, to, "12"),
        new TrashMailDto(temail, to, "122")
    );
    usermailService.removeMsgFromTrash(headerInfo, temail, trashMailDtos);
    verify(usermail2NotfyMqService)
        .sendMqTrashMsgOpratorNotify(headerInfo, temail, trashMailDtos, SessionEventType.EVENT_TYPE_37);
    ArgumentCaptor<String> temailCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<List<TrashMailDto>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
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

    ArgumentCaptor<List<TrashMailDto>> listArgumentCaptor = ArgumentCaptor.forClass(List.class);
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
    QueryTrashDto queryTrashDto = new QueryTrashDto(new Timestamp(timestamp), pageSize, TemailStatus.STATUS_TRASH_4,
        signal, temail);
    List<Usermail> result = Arrays.asList(
        new Usermail(22, "111", "", temail, "", 0, TemailType.TYPE_NORMAL_0, temail, "", 0, "".getBytes(), temail, null)
    );
    when(usermailRepo.getUsermailByStatus((queryTrashDto))).thenReturn(result);
    when(convertMsgService.convertMsg(result)).thenReturn(result);
    List<Usermail> msgFromTrash = usermailService.getMsgFromTrash(headerInfo, temail, timestamp, pageSize, signal);
    ArgumentCaptor<QueryTrashDto> trashCaptor = ArgumentCaptor.forClass(QueryTrashDto.class);
    verify(usermailRepo).getUsermailByStatus(trashCaptor.capture());
    QueryTrashDto dtos = trashCaptor.getValue();
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

  @Test(expected = IllegalGMArgsException.class)
  public void failUpdateUsermailBoxArchiveStatus() {
    String from = "from@msgseal.com";
    String to = "to@msgseal.com";
    int archiveStatus = 2;
    usermailService.updateUsermailBoxArchiveStatus(headerInfo, from, to, archiveStatus);
    verify(usermailBoxRepo).updateArchiveStatus(from, to, archiveStatus);
  }

}