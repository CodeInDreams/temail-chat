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

package com.syswin.temail.usermail.interfaces;

import com.google.gson.Gson;
import com.syswin.temail.usermail.application.UsermailMsgReplyService;
import com.syswin.temail.usermail.application.UsermailService;
import com.syswin.temail.usermail.common.Constants.UsermailAgentEventType;
import com.syswin.temail.usermail.common.ParamsKey;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.Test;
import org.mockito.Mockito;

public class UsermailMQConsumerTest {

  private UsermailService usermailService = Mockito.mock(UsermailService.class);

  private UsermailMsgReplyService usermailMsgReplyService = Mockito.mock(UsermailMsgReplyService.class);

  private UsermailMQConsumer usermailMQConsumer = new UsermailMQConsumer(usermailService, usermailMsgReplyService);

  private String xPacketId = UUID.randomUUID().toString();

  private String cdtpHeader = "{'packetId':'12112'}";

  private String from = "from@t.email";

  private String to = "to@t.email";

  private String msgId = xPacketId;

  private String parentMsgId = xPacketId;

  private String owner = from;

  @Test
  public void consume0Test(){

    TrashMailDTO trashMailDto = new TrashMailDTO();
    trashMailDto.setFrom(from);
    trashMailDto.setTo(to);
    trashMailDto.setMsgId(msgId);
    List<TrashMailDTO> trashMailDtos = new ArrayList<>();
    trashMailDtos.add(trashMailDto);
    usermailMQConsumer.consumer(getTestMessage(UsermailAgentEventType.TRASH_REMOVE_0));
    Mockito.verify(usermailService).removeMsgFromTrash(from, trashMailDtos);
  }

  @Test
  public void consume2Test(){
    usermailMQConsumer.consumer(getTestMessage(UsermailAgentEventType.DESTROY_AFTER_READ_2));
    Mockito.verify(usermailService).destroyAfterRead(xPacketId, cdtpHeader, from, to, owner, msgId);
  }

  @Test
  public void consume3Test(){
    usermailMQConsumer.consumer(getTestMessage(UsermailAgentEventType.REVERT_MSG_3));
    Mockito.verify(usermailService).revertMqHandler(xPacketId, cdtpHeader, from, to, owner, msgId);
  }

  @Test
  public void consume4Test(){
    usermailMQConsumer.consumer(getTestMessage(UsermailAgentEventType.REVERT_REPLY_MSG_4));
    Mockito.verify(usermailMsgReplyService).revertMsgReply(xPacketId, cdtpHeader, from, to, owner, parentMsgId, msgId);
  }

  @Test
  public void consume5Test(){
    usermailMQConsumer.consumer(getTestMessage(UsermailAgentEventType.DESTROY_AFTER_READ_REPLY_MSG_5));
    Mockito.verify(usermailMsgReplyService).destroyAfterRead(xPacketId, cdtpHeader, from, to, owner, msgId, parentMsgId);
  }

  @Test
  public void consume6Test(){
    usermailMQConsumer.consumer(getTestMessage(UsermailAgentEventType.REMOVE_GROUP_CHAT_MEMBERS_6));
    Mockito.verify(usermailService).deleteGroupChatSession(from, to);
  }

  private String getTestMessage(int type){
    Gson gson = new Gson();
    TrashMailDTO trashMailDto = new TrashMailDTO();
    trashMailDto.setFrom(from);
    trashMailDto.setTo(to);
    trashMailDto.setMsgId(msgId);
    List<TrashMailDTO> trashMailDtos = new ArrayList<>();
    trashMailDtos.add(trashMailDto);
    Map<String, Object> map = new HashMap<>();
    map.put(ParamsKey.SessionEventKey.CDTP_HEADER, cdtpHeader);
    map.put(ParamsKey.SessionEventKey.X_PACKET_ID, xPacketId);
    map.put(ParamsKey.SessionEventKey.OWNER, owner);
    map.put(ParamsKey.SessionEventKey.FROM, from);
    map.put(ParamsKey.SessionEventKey.MSGID, msgId);
    map.put(ParamsKey.SessionEventKey.TO, to);
    map.put(ParamsKey.SessionEventKey.GROUP_TEMAIL,from);
    map.put(ParamsKey.SessionEventKey.TEMAIL, to);
    map.put(ParamsKey.SessionEventKey.REPLY_MSG_PARENT_ID, parentMsgId);
    map.put(ParamsKey.SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    map.put(ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE, type);
    map.put(ParamsKey.SessionEventKey.TRASH_MSG_INFO, gson.toJson(trashMailDtos));
    return gson.toJson(map);
  }
}
