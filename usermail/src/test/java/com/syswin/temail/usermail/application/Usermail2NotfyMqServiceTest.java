package com.syswin.temail.usermail.application;

import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.FROM;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.MSGID;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.OWNER;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.PARENT_MSGID;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.SEQ_NO;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.TO;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.TO_MSG;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.syswin.temail.usermail.common.SessionEventType;
import com.syswin.temail.usermail.configuration.UsermailConfig;
import com.syswin.temail.usermail.core.IMqAdapter;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class Usermail2NotfyMqServiceTest {

  private final IMqAdapter mqAdapter = Mockito.mock(IMqAdapter.class);
  private final UsermailConfig usermailConfig = new UsermailConfig();
  private final Usermail2NotfyMqService usermail2NotfyMqService = new Usermail2NotfyMqService(mqAdapter,
      usermailConfig);
  private CdtpHeaderDTO headerInfo = new CdtpHeaderDTO("{CDTP-header:value}",
      "{xPacketId:value}");

  @Test
  public void sendMqMsgSaveMail() {
    String header = "CDTP-header";
    String from = "from@temail.com";
    String to = "to@temail.com";
    String msgid = "msgid";
    String toMsg = "tomsg";
    int attachmentSize = 100;
    long seqNo = 1L;
    int eventType = SessionEventType.EVENT_TYPE_0;
    usermail2NotfyMqService
        .sendMqMsgSaveMail(headerInfo, from, to, from, msgid, toMsg, seqNo, eventType, attachmentSize, from, null);
    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor.capture(), messageCaptor.capture());
    String value = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObj = parser.parse(value).getAsJsonObject();
    assertEquals(from, jsonObj.get(FROM).getAsString());
    assertEquals(to, jsonObj.get(TO).getAsString());
    assertEquals(msgid, jsonObj.get(MSGID).getAsString());
    assertEquals(toMsg, jsonObj.get(TO_MSG).getAsString());
    assertEquals(seqNo, jsonObj.get(SEQ_NO).getAsLong());
    Assert.assertEquals(from, tagCaptor.getValue());
  }

  @Test
  public void sendMqAfterUpdateStatus() {
    String from = "from@syswin.com";
    String to = "to@syswin.com";
    String header = "CDTP-header";
    String msgid = "msgid";
    int type = SessionEventType.EVENT_TYPE_0;
    usermail2NotfyMqService.sendMqAfterUpdateStatus(headerInfo, from, to, msgid, type);
    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor.capture(), messageCaptor.capture());
    String value = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObj = parser.parse(value).getAsJsonObject();
    Assert.assertEquals(from, jsonObj.get("from").getAsString());
    Assert.assertEquals(to, jsonObj.get("to").getAsString());
    Assert.assertEquals(msgid, jsonObj.get("msgid").getAsString());
    Assert.assertEquals(from, tagCaptor.getValue());
  }


  @Test
  public void sendMqUpdateMsg() {
    String xPacketId = UUID.randomUUID().toString();
    String cdtpHeader = "header";
    String from = "from@systoontest.com";
    String to = "to@systoontest.com";
    String owner = from;
    String msgid = "835u389";
    int type = SessionEventType.EVENT_TYPE_2;
    usermail2NotfyMqService.sendMqUpdateMsg(xPacketId, cdtpHeader, from, to, owner, msgid, type);
    ArgumentCaptor<String> fromCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> mapCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(eq(usermailConfig.mqTopic), fromCaptor.capture(), mapCaptor.capture());
    assertEquals(from, fromCaptor.getValue());
    String value = mapCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObject = parser.parse(value).getAsJsonObject();
    assertEquals(to, jsonObject.get(TO).getAsString());
    assertEquals(msgid, jsonObject.get(MSGID).getAsString());
  }


  @Test
  @Ignore
  // 此功能目前没有使用，测试用例先忽略
  public void sendMqMsgGetMail() {
    String header = "CDTP-header";
    String from = "from@temail.com";
    String to = "to@temail.com";
    String msgid = "msgid";
    Usermail usermail = new Usermail();
    usermail.setMsgid(msgid);
    List<Usermail> result = Collections.singletonList(usermail);
    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor.capture(), messageCaptor.capture());
    String value = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObj = parser.parse(value).getAsJsonObject();
    assertEquals(from, jsonObj.get(FROM).getAsString());
    assertEquals(to, jsonObj.get(TO).getAsString());
    assertEquals(msgid, jsonObj.get(MSGID).getAsString());
    Assert.assertEquals(from, tagCaptor.getValue());
  }

  @Test
  public void sendMqAfterDeleteSession() {
    String from = "from@temail";
    String to = "to@from@temail";
    boolean deleteAllMsg = true;
    int eventType = SessionEventType.EVENT_TYPE_4;
    usermail2NotfyMqService.sendMqAfterDeleteSession(headerInfo, from, to, deleteAllMsg, eventType);
    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor.capture(), messageCaptor.capture());
    String value = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject object = parser.parse(value).getAsJsonObject();
    assertEquals(from, object.get(FROM).getAsString());
    assertEquals(to, object.get(TO).getAsString());
    Assert.assertEquals(from, tagCaptor.getValue());
  }

  @Test
  public void sendSaveMsgReply() {
    String from = "from@temail";
    String to = "to@temail";
    String owner = from;
    String msgId = "12344";
    String toMsg = "skjgi";
    long seqNo = 0;
    int attachmentSize = 100;
    String parentMsgId = "1938";

    usermail2NotfyMqService
        .sendMqSaveMsgReply(headerInfo, from, to, owner, msgId, toMsg, seqNo, attachmentSize, parentMsgId);
    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor.capture(), messageCaptor.capture());
    String value = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject object = parser.parse(value).getAsJsonObject();
    assertEquals(from, object.get(FROM).getAsString());
    assertEquals(to, object.get(TO).getAsString());
    assertEquals(msgId, object.get(MSGID).getAsString());
    Assert.assertEquals(from, tagCaptor.getValue());
  }


  @Test
  public void sendMqAfterUpdateMsgReply() {
    String xPacketId = UUID.randomUUID().toString();
    String cdtpHeader = "1header";
    String from = "from@temail";
    String to = "to@temail";
    String owner = to;
    String msgId = "12344";
    int type = 0;
    String parentMsgId = "1938";

    usermail2NotfyMqService.sendMqAfterUpdateMsgReply(xPacketId, cdtpHeader, from, to, owner, msgId, type, parentMsgId);
    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor.capture(), messageCaptor.capture());
    String value = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject object = parser.parse(value).getAsJsonObject();
    assertEquals(from, object.get(FROM).getAsString());
    assertEquals(to, object.get(TO).getAsString());
    assertEquals(msgId, object.get(MSGID).getAsString());
    Assert.assertEquals(from, tagCaptor.getValue());
  }

  @Test
  public void sendMqAfterRemoveMsgReply() {
    String from = "from@temail";
    String to = "to@temail";
    String owner = from;
    List<String> msgIds = new ArrayList<String>();
    String msgId = "12344";
    msgIds.add(msgId);
    int type = 0;
    String parentMsgId = "1938";
    usermail2NotfyMqService.sendMqAfterRemoveMsgReply(headerInfo, from, to, owner, msgIds, type, parentMsgId);
    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor.capture(), messageCaptor.capture());
    String value = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject object = parser.parse(value).getAsJsonObject();
    assertEquals(from, object.get(FROM).getAsString());
    assertEquals(to, object.get(TO).getAsString());
    assertEquals(parentMsgId, object.get(PARENT_MSGID).getAsString());
    Assert.assertEquals(from, tagCaptor.getValue());
  }

  @Test
  public void sendMqAfterUpdateArchiveStatus() throws Exception {
    String from = "a@msgseal.com";
    String to = "b@msgseal.com";
    int type = SessionEventType.EVENT_TYPE_33;

    usermail2NotfyMqService.sendMqAfterUpdateArchiveStatus(headerInfo, from, to, type);
    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor.capture(), messageCaptor.capture());
    String message = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject object = parser.parse(message).getAsJsonObject();
    assertEquals(from, object.get(FROM).getAsString());
    assertEquals(to, object.get(TO).getAsString());
    assertEquals(from, tagCaptor.getValue());
  }

  @Test
  public void sendMqMoveTrashNotify() throws Exception {
    String from = "a@msgseal.com";
    String to = "b@msgseal.com";
    String msgId = "3747475";
    List<String> msgIds = new ArrayList<>();
    msgIds.add(msgId);
    int type = SessionEventType.EVENT_TYPE_35;
    usermail2NotfyMqService.sendMqMoveTrashNotify(headerInfo, from, to, msgIds, type);
    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor
        .capture(), messageCaptor.capture());
    String message = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject object = parser.parse(message).getAsJsonObject();
    assertEquals(from, object.get(FROM).getAsString());
    assertEquals(to, object.get(TO).getAsString());
    assertEquals(from, tagCaptor.getValue());
  }

  @Test
  public void sendMqTrashMsgOpratorNotify() throws Exception {
    String owner = "a@msgseal.com";
    String to = "a@msgseal.com";
    String msgId = "1132";
    List<TrashMailDTO> trashMailDtos = Arrays.asList(new TrashMailDTO(owner, to, msgId));
    int type = SessionEventType.EVENT_TYPE_36;
    usermail2NotfyMqService.sendMqTrashMsgOpratorNotify(headerInfo, owner, trashMailDtos, type);
    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor
        .capture(), messageCaptor.capture());
    String message = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject object = parser.parse(message).getAsJsonObject();
    assertEquals(owner, object.get(OWNER).getAsString());
    assertEquals(owner, tagCaptor.getValue());
  }
}
