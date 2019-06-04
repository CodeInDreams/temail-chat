package com.syswin.temail.usermail.application;

import static com.syswin.temail.usermail.common.Contants.SessionEventKey.FROM;
import static com.syswin.temail.usermail.common.Contants.SessionEventKey.MSGID;
import static com.syswin.temail.usermail.common.Contants.SessionEventKey.OWNER;
import static com.syswin.temail.usermail.common.Contants.SessionEventKey.REPLY_MSG_PARENT_ID;
import static com.syswin.temail.usermail.common.Contants.SessionEventKey.SESSION_MESSAGE_TYPE;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.syswin.temail.usermail.common.Contants.UsermailAgentEventType;
import com.syswin.temail.usermail.configuration.UsermailConfig;
import com.syswin.temail.usermail.core.IMqAdapter;
import com.syswin.temail.usermail.dto.TrashMailDto;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class UsermailUsermail2NotfyMqServiceTest {

  private final IMqAdapter mqAdapter = Mockito.mock(IMqAdapter.class);
  private final UsermailConfig usermailConfig = new UsermailConfig();
  private final UsermailMqService usermailMqService = new UsermailMqService(mqAdapter, usermailConfig);

  @Test
  public void sendMqRemoveTrash() {
    String temail = "from@msgseal.com";
    String to = "to@msgseal.com";
    List<TrashMailDto> trashMailDtos = Arrays.asList(
        new TrashMailDto(temail, to, "12"),
        new TrashMailDto(temail, to, "122")
    );
    int type = UsermailAgentEventType.TRASH_REMOVE_0;
    usermailMqService.sendMqRemoveTrash(temail, trashMailDtos, type);

    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor.capture(), messageCaptor.capture());
    String message = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject object = parser.parse(message).getAsJsonObject();
    assertEquals(temail, tagCaptor.getValue());
    assertEquals(temail, object.get(FROM).getAsString());
    assertEquals(type, object.get(SESSION_MESSAGE_TYPE).getAsInt());
  }

  @Test
  public void sendMqDestroyMsg() {
    String xPacketId = UUID.randomUUID().toString();
    String header = "CDTP-header";
    String owner = "owner@msgseal.com";
    String to = "to@t.email";
    String msgId = "1345";
    usermailMqService.sendMqDestroyMsg(xPacketId, header, owner, to, owner, msgId);

    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor.capture(), messageCaptor.capture());
    String message = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject object = parser.parse(message).getAsJsonObject();
    assertEquals(owner, tagCaptor.getValue());
    assertEquals(msgId, object.get(MSGID).getAsString());
    assertEquals(owner, object.get(FROM).getAsString());
    assertEquals(UsermailAgentEventType.DESTROY_AFTER_READ_2, object.get(SESSION_MESSAGE_TYPE).getAsInt());
  }

  @Test
  public void sendMqRevertMsg() {
    String xPacketId = UUID.randomUUID().toString();
    String header = "CDTP-header";
    String owner = "owner@msgseal.com";
    String to = "to@t.email";
    String msgId = "1345";
    usermailMqService.sendMqRevertMsg(xPacketId, header, owner, to, owner, msgId);

    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor.capture(), messageCaptor.capture());
    String message = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject object = parser.parse(message).getAsJsonObject();
    assertEquals(owner, tagCaptor.getValue());
    assertEquals(msgId, object.get(MSGID).getAsString());
    assertEquals(owner, object.get(OWNER).getAsString());
    assertEquals(UsermailAgentEventType.REVERT_MSG_3, object.get(SESSION_MESSAGE_TYPE).getAsInt());
  }

  @Test
  public void sendMqRevertReplyMsg() {
    String xPacketId = UUID.randomUUID().toString();
    String header = "CDTP-header";
    String owner = "owner@msgseal.com";
    String from = owner;
    String to = "to@t.email";
    String msgId = "23456";
    String parentMsgReplyId = "1235";

    usermailMqService.sendMqRevertReplyMsg(xPacketId, header, from, to, owner, parentMsgReplyId, msgId);
    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor.capture(), messageCaptor.capture());
    String message = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject object = parser.parse(message).getAsJsonObject();
    assertEquals(msgId, object.get(MSGID).getAsString());
    assertEquals(owner, object.get(OWNER).getAsString());
    assertEquals(parentMsgReplyId, object.get(REPLY_MSG_PARENT_ID).getAsString());
    assertEquals(UsermailAgentEventType.REVERT_REPLY_MSG_4, object.get(SESSION_MESSAGE_TYPE).getAsInt());
  }

  @Test
  public void sendMqReplyMsgDestoryAfterRead() {
    String xPacketId = UUID.randomUUID().toString();
    String header = "CDTP-header";
    String owner = "owner@msgseal.com";
    String to = "to@t.email";
    String msgId = "23456";
    String parentMsgReplyId = "1235";

    usermailMqService.sendMqReplyMsgDestoryAfterRead(xPacketId, header, owner, to, owner, msgId, parentMsgReplyId);
    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> tagCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter).sendMessage(topicCaptor.capture(), tagCaptor.capture(), messageCaptor.capture());
    String message = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject object = parser.parse(message).getAsJsonObject();
    assertEquals(msgId, object.get(MSGID).getAsString());
    assertEquals(owner, object.get(OWNER).getAsString());
    assertEquals(parentMsgReplyId, object.get(REPLY_MSG_PARENT_ID).getAsString());
    assertEquals(UsermailAgentEventType.DESTROY_AFTER_READ_REPLY_MSG_5, object.get(SESSION_MESSAGE_TYPE).getAsInt());
  }

}