package com.syswin.temail.usermail.interfaces;

import com.google.gson.Gson;
import com.syswin.temail.usermail.application.UsermailMsgReplyService;
import com.syswin.temail.usermail.application.UsermailService;
import com.syswin.temail.usermail.common.Constants.SessionEventKey;
import com.syswin.temail.usermail.common.Constants.UsermailAgentEventType;
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
    Mockito.verify(usermailService).revert(xPacketId, cdtpHeader, from, to, owner, msgId);
  }

  @Test
  public void consume4Test(){
    usermailMQConsumer.consumer(getTestMessage(UsermailAgentEventType.REVERT_REPLY_MSG_4));
    Mockito.verify(usermailMsgReplyService).revertMsgReply(xPacketId, cdtpHeader, from, to, owner, parentMsgId, msgId);
  }

  @Test
  public void consume5Test(){
    usermailMQConsumer.consumer(getTestMessage(UsermailAgentEventType.DESTROY_AFTER_READ_REPLY_MSG_5));
    Mockito.verify(usermailMsgReplyService).destoryAfterRead(xPacketId, cdtpHeader, from, to, owner, msgId, parentMsgId);
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
    map.put(SessionEventKey.CDTP_HEADER, cdtpHeader);
    map.put(SessionEventKey.X_PACKET_ID, xPacketId);
    map.put(SessionEventKey.OWNER, owner);
    map.put(SessionEventKey.FROM, from);
    map.put(SessionEventKey.MSGID, msgId);
    map.put(SessionEventKey.TO, to);
    map.put(SessionEventKey.GROUP_TEMAIL,from);
    map.put(SessionEventKey.TEMAIL, to);
    map.put(SessionEventKey.REPLY_MSG_PARENT_ID, parentMsgId);
    map.put(SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    map.put(SessionEventKey.SESSION_MESSAGE_TYPE, type);
    map.put(SessionEventKey.TRASH_MSG_INFO, gson.toJson(trashMailDtos));
    return gson.toJson(map);
  }
}
