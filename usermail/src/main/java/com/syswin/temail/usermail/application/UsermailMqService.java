package com.syswin.temail.usermail.application;

import com.google.gson.Gson;
import com.syswin.temail.usermail.common.Contants.SessionEventKey;
import com.syswin.temail.usermail.common.Contants.UsermailAgentEventType;
import com.syswin.temail.usermail.configuration.UsermailConfig;
import com.syswin.temail.usermail.core.IMqAdapter;
import com.syswin.temail.usermail.dto.TrashMailDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class UsermailMqService {

  private final IMqAdapter mqAdapter;
  private final UsermailConfig usermailConfig;


  public UsermailMqService(IMqAdapter mqAdapter, UsermailConfig usermailConfig) {
    this.mqAdapter = mqAdapter;
    this.usermailConfig = usermailConfig;
  }

  /**
   * 废纸篓消息还原、删除
   */
  public void sendMqRemoveTrash(String owner,
      List<TrashMailDto> trashMailDtoList, int type) {
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>();
    map.put(SessionEventKey.FROM, owner);
    map.put(SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    map.put(SessionEventKey.SESSION_MESSAGE_TYPE, type);
    if (trashMailDtoList != null && !trashMailDtoList.isEmpty()) {
      map.put(SessionEventKey.TRASH_MSG_INFO, gs.toJson(trashMailDtoList));
    }
    String s = gs.toJson(map);
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, s);
  }

  public void sendMqDestroyMsg(String xPacketId, String cdtpHeader,  String from, String to, String owner, String msgId) {
    Map<String, Object> map = new HashMap<>(4);
    map.put(SessionEventKey.X_PACKET_ID, xPacketId);
    map.put(SessionEventKey.CDTP_HEADER, cdtpHeader);
    map.put(SessionEventKey.OWNER, owner);
    map.put(SessionEventKey.FROM, from);
    map.put(SessionEventKey.TO, to);
    map.put(SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.DESTROY_AFTER_READ_2);
    map.put(SessionEventKey.MSGID, msgId);
    map.put(SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    Gson gs = new Gson();
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, gs.toJson(map));
  }

  public void sendMqRevertMsg(String xPacketId, String cdtpHeader,  String from, String to, String owner, String msgid) {
    Map<String, Object> map = new HashMap<>();
    map.put(SessionEventKey.OWNER, owner);
    map.put(SessionEventKey.FROM, from);
    map.put(SessionEventKey.TO, to);
    map.put(SessionEventKey.MSGID, msgid);
    map.put(SessionEventKey.X_PACKET_ID, xPacketId);
    map.put(SessionEventKey.CDTP_HEADER, cdtpHeader);
    map.put(SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    map.put(SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.REVERT_MSG_3);
    Gson gson = new Gson();
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, gson.toJson(map));
  }

  public void sendMqRevertReplyMsg(String xPacketId, String cdtpHeader,  String from, String to, String owner, String parentMsgReplyId, String msgId) {
    Map<String, Object> map = new HashMap<>();
    map.put(SessionEventKey.X_PACKET_ID, xPacketId);
    map.put(SessionEventKey.CDTP_HEADER, cdtpHeader);
    map.put(SessionEventKey.OWNER, owner);
    map.put(SessionEventKey.FROM, from);
    map.put(SessionEventKey.TO, to);
    map.put(SessionEventKey.MSGID, msgId);
    map.put(SessionEventKey.REPLY_MSG_PARENT_ID, parentMsgReplyId);
    map.put(SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    map.put(SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.REVERT_REPLY_MSG_4);
    Gson gson = new Gson();
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, gson.toJson(map));

  }

  public void sendMqReplyMsgDestoryAfterRead(String xPacketId, String cdtpHeader,  String from, String to, String owner, String msgId, String parentMsgId) {

    Map<String, Object> map = new HashMap<>();
    map.put(SessionEventKey.X_PACKET_ID, xPacketId);
    map.put(SessionEventKey.CDTP_HEADER, cdtpHeader);
    map.put(SessionEventKey.FROM, from);
    map.put(SessionEventKey.TO, to);
    map.put(SessionEventKey.OWNER, owner);
    map.put(SessionEventKey.MSGID, msgId);
    map.put(SessionEventKey.REPLY_MSG_PARENT_ID, parentMsgId);
    map.put(SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    map.put(SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.DESTROY_AFTER_READ_REPLY_MSG_5);
    Gson gson = new Gson();
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, gson.toJson(map));
  }

  public void sendMqRemoveGroupMemberMsg(String groupTemail, String temail) {
    Map<String, Object> map = new HashMap<>();
    map.put(SessionEventKey.GROUP_TEMAIL, groupTemail);
    map.put(SessionEventKey.TEMAIL, temail);
    map.put(SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    map.put(SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.REMOVE_GROUP_CHAT_MEMBERS_6);
    Gson gson = new Gson();
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, groupTemail, gson.toJson(map));
  }
}
