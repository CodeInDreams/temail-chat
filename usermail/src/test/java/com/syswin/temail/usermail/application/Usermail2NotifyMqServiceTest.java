package com.syswin.temail.usermail.application;

import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.ATTACHMENT_SIZE;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.AUTHOR;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.CDTP_HEADER;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.DELETE_ALL_MSG;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.FILTER;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.FROM;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.MSGID;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.OWNER;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.PARENT_MSGID;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.SEQ_NO;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.TIMESTAMP;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.TO;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.TO_MSG;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.TRASH_MSG_INFO;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.X_PACKET_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.syswin.temail.usermail.common.SessionEventType;
import com.syswin.temail.usermail.configuration.UsermailConfig;
import com.syswin.temail.usermail.core.IMqAdapter;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class Usermail2NotifyMqServiceTest {

  private final IMqAdapter mqAdapter = Mockito.mock(IMqAdapter.class);
  private final UsermailConfig usermailConfig = new UsermailConfig();
  private final Usermail2NotifyMqService usermail2NotifyMqService = new Usermail2NotifyMqService(mqAdapter,
      usermailConfig);
  private CdtpHeaderDTO headerInfo = new CdtpHeaderDTO("{CDTP-header:value}",
      "{xPacketId:value}");
  private Gson gson = new Gson();;

  @Test
  public void sendMqMsgSaveMail() {
    String from = "from@temail.com";
    String to = "to@temail.com";
    String msgid = "msgid";
    String toMsg = "tomsg";
    int attachmentSize = 100;
    long seqNo = 1L;
    int eventType = SessionEventType.EVENT_TYPE_0;

    usermail2NotifyMqService
        .sendMqMsgSaveMail(headerInfo, from, to, from, msgid, toMsg, seqNo, eventType, attachmentSize, from, null);

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(eq(usermailConfig.mqTopic), eq(from), messageCaptor.capture());
    String value = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObj = parser.parse(value).getAsJsonObject();
    assertEquals(headerInfo.getCdtpHeader(), jsonObj.get(CDTP_HEADER).getAsString());
    assertEquals(headerInfo.getxPacketId(), jsonObj.get(X_PACKET_ID).getAsString());
    assertThat(jsonObj.get(TIMESTAMP)).isNotNull();
    assertEquals(eventType, jsonObj.get(SESSION_MESSAGE_TYPE).getAsInt());
    assertEquals(from, jsonObj.get(FROM).getAsString());
    assertEquals(to, jsonObj.get(TO).getAsString());
    assertEquals(from, jsonObj.get(OWNER).getAsString());
    assertEquals(msgid, jsonObj.get(MSGID).getAsString());
    assertEquals(toMsg, jsonObj.get(TO_MSG).getAsString());
    assertEquals(seqNo, jsonObj.get(SEQ_NO).getAsLong());
    assertEquals(attachmentSize, jsonObj.get(ATTACHMENT_SIZE).getAsInt());
    assertEquals(from, jsonObj.get(AUTHOR).getAsString());
    assertThat(jsonObj.get(FILTER)).isNull();
    assertEquals(jsonObj.size(), 12);
  }

  @Test
  public void sendMqAfterUpdateStatus() {
    String from = "from@syswin.com";
    String to = "to@syswin.com";
    String msgid = "msgid";
    int eventType = SessionEventType.EVENT_TYPE_0;

    usermail2NotifyMqService.sendMqAfterUpdateStatus(headerInfo, from, to, msgid, eventType);

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(eq(usermailConfig.mqTopic), eq(from), messageCaptor.capture());
    String value = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObj = parser.parse(value).getAsJsonObject();
    assertEquals(headerInfo.getCdtpHeader(), jsonObj.get(CDTP_HEADER).getAsString());
    assertEquals(headerInfo.getxPacketId(), jsonObj.get(X_PACKET_ID).getAsString());
    assertThat(jsonObj.get(TIMESTAMP)).isNotNull();
    assertEquals(eventType, jsonObj.get(SESSION_MESSAGE_TYPE).getAsInt());
    assertEquals(from, jsonObj.get(FROM).getAsString());
    assertEquals(to, jsonObj.get(TO).getAsString());
    assertEquals(msgid, jsonObj.get(MSGID).getAsString());
    assertEquals(jsonObj.size(), 7);
  }


  @Test
  public void sendMqUpdateMsg() {
    String xPacketId = UUID.randomUUID().toString();
    String cdtpHeader = "header";
    String from = "from@systoontest.com";
    String to = "to@systoontest.com";
    String owner = from;
    String msgid = "835u389";
    int eventType = SessionEventType.EVENT_TYPE_2;

    usermail2NotifyMqService.sendMqUpdateMsg(xPacketId, cdtpHeader, from, to, owner, msgid, eventType);

    ArgumentCaptor<String> mapCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(eq(usermailConfig.mqTopic), eq(from), mapCaptor.capture());
    String value = mapCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObject = parser.parse(value).getAsJsonObject();
    assertEquals(cdtpHeader, jsonObject.get(CDTP_HEADER).getAsString());
    assertEquals(xPacketId, jsonObject.get(X_PACKET_ID).getAsString());
    assertThat(jsonObject.get(TIMESTAMP)).isNotNull();
    assertEquals(eventType, jsonObject.get(SESSION_MESSAGE_TYPE).getAsInt());
    assertEquals(from, jsonObject.get(FROM).getAsString());
    assertEquals(to, jsonObject.get(TO).getAsString());
    assertEquals(owner, jsonObject.get(OWNER).getAsString());
    assertEquals(msgid, jsonObject.get(MSGID).getAsString());
    assertEquals(jsonObject.size(), 8);
  }

  @Test
  public void sendMqAfterDeleteSession() {
    String from = "from@temail";
    String to = "to@from@temail";
    boolean deleteAllMsg = true;
    int eventType = SessionEventType.EVENT_TYPE_4;

    usermail2NotifyMqService.sendMqAfterDeleteSession(headerInfo, from, to, deleteAllMsg, eventType);

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(eq(usermailConfig.mqTopic), eq(from), messageCaptor.capture());
    String value = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObject = parser.parse(value).getAsJsonObject();
    assertEquals(headerInfo.getCdtpHeader(), jsonObject.get(CDTP_HEADER).getAsString());
    assertEquals(headerInfo.getxPacketId(), jsonObject.get(X_PACKET_ID).getAsString());
    assertThat(jsonObject.get(TIMESTAMP)).isNotNull();
    assertEquals(eventType, jsonObject.get(SESSION_MESSAGE_TYPE).getAsInt());
    assertEquals(from, jsonObject.get(FROM).getAsString());
    assertEquals(to, jsonObject.get(TO).getAsString());
    assertEquals(deleteAllMsg, jsonObject.get(DELETE_ALL_MSG).getAsBoolean());
    assertEquals(jsonObject.size(), 7);
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

    usermail2NotifyMqService
        .sendMqSaveMsgReply(headerInfo, from, to, owner, msgId, toMsg, seqNo, attachmentSize, parentMsgId);

    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor.capture(), messageCaptor.capture());
    String value = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObject = parser.parse(value).getAsJsonObject();
    assertEquals(headerInfo.getCdtpHeader(), jsonObject.get(CDTP_HEADER).getAsString());
    assertEquals(headerInfo.getxPacketId(), jsonObject.get(X_PACKET_ID).getAsString());
    assertThat(jsonObject.get(TIMESTAMP)).isNotNull();
    assertEquals(SessionEventType.EVENT_TYPE_18, jsonObject.get(SESSION_MESSAGE_TYPE).getAsInt());
    assertEquals(from, jsonObject.get(FROM).getAsString());
    assertEquals(to, jsonObject.get(TO).getAsString());
    assertEquals(owner, jsonObject.get(OWNER).getAsString());
    assertEquals(parentMsgId, jsonObject.get(PARENT_MSGID).getAsString());
    assertEquals(toMsg, jsonObject.get(TO_MSG).getAsString());
    assertEquals(seqNo, jsonObject.get(SEQ_NO).getAsLong());
    assertEquals(attachmentSize, jsonObject.get(ATTACHMENT_SIZE).getAsInt());
    assertEquals(jsonObject.size(), 12);
  }


  @Test
  public void sendMqAfterUpdateMsgReply() {
    String xPacketId = UUID.randomUUID().toString();
    String cdtpHeader = "1header";
    String from = "from@temail";
    String to = "to@temail";
    String owner = to;
    String msgId = "12344";
    int eventType = 0;
    String parentMsgId = "1938";

    usermail2NotifyMqService
        .sendMqAfterUpdateMsgReply(xPacketId, cdtpHeader, from, to, owner, msgId, eventType, parentMsgId);

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(eq(usermailConfig.mqTopic), eq(from), messageCaptor.capture());
    String value = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObject = parser.parse(value).getAsJsonObject();
    assertEquals(cdtpHeader, jsonObject.get(CDTP_HEADER).getAsString());
    assertEquals(xPacketId, jsonObject.get(X_PACKET_ID).getAsString());
    assertThat(jsonObject.get(TIMESTAMP)).isNotNull();
    assertEquals(eventType, jsonObject.get(SESSION_MESSAGE_TYPE).getAsInt());
    assertEquals(from, jsonObject.get(FROM).getAsString());
    assertEquals(owner, jsonObject.get(OWNER).getAsString());
    assertEquals(msgId, jsonObject.get(MSGID).getAsString());
    assertEquals(parentMsgId, jsonObject.get(PARENT_MSGID).getAsString());
    assertEquals(jsonObject.size(), 9);
  }

  @Test
  public void sendMqAfterRemoveMsgReply() {
    String from = "from@temail";
    String to = "to@temail";
    String owner = from;
    List<String> msgIds = new ArrayList<String>();
    String msgId = "12344";
    msgIds.add(msgId);
    int eventType = 0;
    String parentMsgId = "1938";

    usermail2NotifyMqService.sendMqAfterRemoveMsgReply(headerInfo, from, to, owner, msgIds, eventType, parentMsgId);

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(eq(usermailConfig.mqTopic), eq(from), messageCaptor.capture());
    String value = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObject = parser.parse(value).getAsJsonObject();
    assertEquals(headerInfo.getCdtpHeader(), jsonObject.get(CDTP_HEADER).getAsString());
    assertEquals(headerInfo.getxPacketId(), jsonObject.get(X_PACKET_ID).getAsString());
    assertThat(jsonObject.get(TIMESTAMP)).isNotNull();
    assertEquals(eventType, jsonObject.get(SESSION_MESSAGE_TYPE).getAsInt());
    assertEquals(from, jsonObject.get(FROM).getAsString());
    assertEquals(owner, jsonObject.get(OWNER).getAsString());
    assertEquals(gson.toJson(Collections.singletonList(msgId)), jsonObject.get(MSGID).getAsString());
    assertEquals(parentMsgId, jsonObject.get(PARENT_MSGID).getAsString());
    assertEquals(jsonObject.size(), 9);
  }

  @Test
  public void sendMqAfterUpdateArchiveStatus() throws Exception {
    String from = "a@msgseal.com";
    String to = "b@msgseal.com";
    int eventType = SessionEventType.EVENT_TYPE_33;

    usermail2NotifyMqService.sendMqAfterUpdateArchiveStatus(headerInfo, from, to, eventType);

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(eq(usermailConfig.mqTopic), eq(from), messageCaptor.capture());
    String message = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObject = parser.parse(message).getAsJsonObject();
    assertEquals(headerInfo.getCdtpHeader(), jsonObject.get(CDTP_HEADER).getAsString());
    assertEquals(headerInfo.getxPacketId(), jsonObject.get(X_PACKET_ID).getAsString());
    assertThat(jsonObject.get(TIMESTAMP)).isNotNull();
    assertEquals(eventType, jsonObject.get(SESSION_MESSAGE_TYPE).getAsInt());
    assertEquals(from, jsonObject.get(FROM).getAsString());
    assertEquals(jsonObject.size(), 6);
  }

  @Test
  public void sendMqMoveTrashNotify() {
    String from = "a@msgseal.com";
    String to = "b@msgseal.com";
    String msgId = "3747475";
    List<String> msgIds = new ArrayList<>();
    msgIds.add(msgId);
    int eventType = SessionEventType.EVENT_TYPE_35;

    usermail2NotifyMqService.sendMqMoveTrashNotify(headerInfo, from, to, msgIds, eventType);

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(eq(usermailConfig.mqTopic), eq(from), messageCaptor.capture());
    String message = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObject = parser.parse(message).getAsJsonObject();
    assertEquals(headerInfo.getCdtpHeader(), jsonObject.get(CDTP_HEADER).getAsString());
    assertEquals(headerInfo.getxPacketId(), jsonObject.get(X_PACKET_ID).getAsString());
    assertThat(jsonObject.get(TIMESTAMP)).isNotNull();
    assertEquals(eventType, jsonObject.get(SESSION_MESSAGE_TYPE).getAsInt());
    assertEquals(from, jsonObject.get(FROM).getAsString());
    assertEquals(from, jsonObject.get(OWNER).getAsString());
    assertEquals(gson.toJson(Collections.singletonList(msgId)), jsonObject.get(MSGID).getAsString());
    assertEquals(jsonObject.size(), 8);
  }

  @Test
  public void sendMqTrashMsgOpratorNotify() {
    String owner = "a@msgseal.com";
    String to = "a@msgseal.com";
    String msgId = "1132";
    List<TrashMailDTO> trashMailDtos = Arrays.asList(new TrashMailDTO(owner, to, msgId));
    int eventType = SessionEventType.EVENT_TYPE_36;

    usermail2NotifyMqService.sendMqTrashMsgOpratorNotify(headerInfo, owner, trashMailDtos, eventType);

    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(eq(usermailConfig.mqTopic), eq(owner), messageCaptor.capture());
    String message = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObject = parser.parse(message).getAsJsonObject();
    assertEquals(headerInfo.getCdtpHeader(), jsonObject.get(CDTP_HEADER).getAsString());
    assertEquals(headerInfo.getxPacketId(), jsonObject.get(X_PACKET_ID).getAsString());
    assertThat(jsonObject.get(TIMESTAMP)).isNotNull();
    assertEquals(eventType, jsonObject.get(SESSION_MESSAGE_TYPE).getAsInt());
    assertEquals(owner, jsonObject.get(OWNER).getAsString());
    assertEquals(gson.toJson(trashMailDtos), jsonObject.get(TRASH_MSG_INFO).getAsString());
    assertEquals(jsonObject.size(), 6);
  }
}
