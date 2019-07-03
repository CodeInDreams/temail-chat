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

import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.FROM;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.MSGID;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.OWNER;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.REPLY_MSG_PARENT_ID;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.TEMAIL;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.syswin.temail.usermail.common.Constants.UsermailAgentEventType;
import com.syswin.temail.usermail.configuration.UsermailConfig;
import com.syswin.temail.usermail.core.IMqAdapter;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class UsermailMqServiceTest {

  private final IMqAdapter mqAdapter = Mockito.mock(IMqAdapter.class);
  private final UsermailConfig usermailConfig = new UsermailConfig();
  private final UsermailMqService usermailMqService = new UsermailMqService(mqAdapter, usermailConfig);

  @Test
  public void sendMqRemoveTrash() {
    String temail = "from@msgseal.com";
    String to = "to@msgseal.com";
    List<TrashMailDTO> trashMailDtos = Arrays.asList(
        new TrashMailDTO(temail, to, "12"),
        new TrashMailDTO(temail, to, "122")
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
  public void sendMqReplyMsgDestroyAfterRead() {
    String xPacketId = UUID.randomUUID().toString();
    String header = "CDTP-header";
    String owner = "owner@msgseal.com";
    String to = "to@t.email";
    String msgId = "23456";
    String parentMsgReplyId = "1235";

    usermailMqService.sendMqReplyMsgDestroyAfterRead(xPacketId, header, owner, to, owner, msgId, parentMsgReplyId);
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

  @Test
  public void sendMqRemoveGroupMemberMsg() {
    String groupTemail = "g.kk@syswin.com";
    String temail = "temail@syswin.com";
    usermailMqService.sendMqRemoveGroupMemberMsg(groupTemail, temail);

    ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> groupTemailCaptor = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> messageCaptor = ArgumentCaptor.forClass(String.class);
    verify(mqAdapter)
        .sendMessage(topicCaptor.capture(), groupTemailCaptor.capture(), messageCaptor.capture());
    String value = messageCaptor.getValue();
    JsonParser parser = new JsonParser();
    JsonObject jsonObject = parser.parse(value).getAsJsonObject();
    assertEquals(groupTemail, groupTemailCaptor.getValue());
    assertEquals(temail, jsonObject.get(TEMAIL).getAsString());
    assertEquals(UsermailAgentEventType.REMOVE_GROUP_CHAT_MEMBERS_6, jsonObject.get(SESSION_MESSAGE_TYPE).getAsInt());
  }

}