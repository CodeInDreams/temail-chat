package com.syswin.temail.usermail.application;

import com.google.gson.Gson;
import com.syswin.temail.usermail.common.Constants.UsermailAgentEventType;
import com.syswin.temail.usermail.common.ParamsKey;
import com.syswin.temail.usermail.configuration.UsermailConfig;
import com.syswin.temail.usermail.core.IMqAdapter;
import com.syswin.temail.usermail.dto.TrashMailDTO;
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
   * @param owner 消息所有人
   * @param trashMailDtoList 被操作的消息列表
   * @param type 0:删除废纸篓消息
   * @return void
   * @description 删除废纸篓消息
   */
  public void sendMqRemoveTrash(String owner,
      List<TrashMailDTO> trashMailDtoList, int type) {
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>(7);
    map.put(ParamsKey.SessionEventKey.FROM, owner);
    map.put(ParamsKey.SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    map.put(ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE, type);
    if (trashMailDtoList != null && !trashMailDtoList.isEmpty()) {
      map.put(ParamsKey.SessionEventKey.TRASH_MSG_INFO, gs.toJson(trashMailDtoList));
    }
    String s = gs.toJson(map);
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, s);
  }

  /**
   * @param xPacketId 包id
   * @param cdtpHeader packet头信息
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所有人
   * @param msgId 消息id
   * @return void
   * @description 单聊消息阅后即焚
   */
  public void sendMqDestroyMsg(String xPacketId, String cdtpHeader, String from, String to, String owner,
      String msgId) {
    Map<String, Object> map = new HashMap<>(12);
    map.put(ParamsKey.SessionEventKey.X_PACKET_ID, xPacketId);
    map.put(ParamsKey.SessionEventKey.CDTP_HEADER, cdtpHeader);
    map.put(ParamsKey.SessionEventKey.OWNER, owner);
    map.put(ParamsKey.SessionEventKey.FROM, from);
    map.put(ParamsKey.SessionEventKey.TO, to);
    map.put(ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.DESTROY_AFTER_READ_2);
    map.put(ParamsKey.SessionEventKey.MSGID, msgId);
    map.put(ParamsKey.SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    Gson gs = new Gson();
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, gs.toJson(map));
  }

  /**
   * @param xPacketId 包id
   * @param cdtpHeader packet头信息
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所有人
   * @param msgid 消息id
   * @return void
   * @description 单聊消息撤回
   */
  public void sendMqRevertMsg(String xPacketId, String cdtpHeader, String from, String to, String owner, String msgid) {
    Map<String, Object> map = new HashMap<>(12);
    map.put(ParamsKey.SessionEventKey.OWNER, owner);
    map.put(ParamsKey.SessionEventKey.FROM, from);
    map.put(ParamsKey.SessionEventKey.TO, to);
    map.put(ParamsKey.SessionEventKey.MSGID, msgid);
    map.put(ParamsKey.SessionEventKey.X_PACKET_ID, xPacketId);
    map.put(ParamsKey.SessionEventKey.CDTP_HEADER, cdtpHeader);
    map.put(ParamsKey.SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    map.put(ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.REVERT_MSG_3);
    Gson gson = new Gson();
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, gson.toJson(map));
  }

  /**
   * @param xPacketId 包id
   * @param cdtpHeader packet头信息
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所有人
   * @param parentMsgReplyId 父消息id
   * @param msgId 消息id
   * @return void
   * @description 单聊回复消息撤回
   */
  public void sendMqRevertReplyMsg(String xPacketId, String cdtpHeader, String from, String to, String owner,
      String parentMsgReplyId, String msgId) {
    Map<String, Object> map = new HashMap<>(13);
    map.put(ParamsKey.SessionEventKey.X_PACKET_ID, xPacketId);
    map.put(ParamsKey.SessionEventKey.CDTP_HEADER, cdtpHeader);
    map.put(ParamsKey.SessionEventKey.OWNER, owner);
    map.put(ParamsKey.SessionEventKey.FROM, from);
    map.put(ParamsKey.SessionEventKey.TO, to);
    map.put(ParamsKey.SessionEventKey.MSGID, msgId);
    map.put(ParamsKey.SessionEventKey.REPLY_MSG_PARENT_ID, parentMsgReplyId);
    map.put(ParamsKey.SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    map.put(ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.REVERT_REPLY_MSG_4);
    Gson gson = new Gson();
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, gson.toJson(map));

  }

  /**
   * @param xPacketId 包id
   * @param cdtpHeader packet头信息
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所有人
   * @param msgId 消息id
   * @param parentMsgId 父消息id
   * @return void
   * @description 单聊回复消息阅后即焚
   */
  public void sendMqReplyMsgDestoryAfterRead(String xPacketId, String cdtpHeader, String from, String to, String owner,
      String msgId, String parentMsgId) {

    Map<String, Object> map = new HashMap<>(13);
    map.put(ParamsKey.SessionEventKey.X_PACKET_ID, xPacketId);
    map.put(ParamsKey.SessionEventKey.CDTP_HEADER, cdtpHeader);
    map.put(ParamsKey.SessionEventKey.FROM, from);
    map.put(ParamsKey.SessionEventKey.TO, to);
    map.put(ParamsKey.SessionEventKey.OWNER, owner);
    map.put(ParamsKey.SessionEventKey.MSGID, msgId);
    map.put(ParamsKey.SessionEventKey.REPLY_MSG_PARENT_ID, parentMsgId);
    map.put(ParamsKey.SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    map.put(ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.DESTROY_AFTER_READ_REPLY_MSG_5);
    Gson gson = new Gson();
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, gson.toJson(map));
  }

  /**
   * @param groupTemail 群聊地址
   * @param temail 被移除人地址
   * @return void
   * @description 群聊移除群成员事件
   */
  public void sendMqRemoveGroupMemberMsg(String groupTemail, String temail) {
    Map<String, Object> map = new HashMap<>(7);
    map.put(ParamsKey.SessionEventKey.GROUP_TEMAIL, groupTemail);
    map.put(ParamsKey.SessionEventKey.TEMAIL, temail);
    map.put(ParamsKey.SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    map.put(ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.REMOVE_GROUP_CHAT_MEMBERS_6);
    Gson gson = new Gson();
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, groupTemail, gson.toJson(map));
  }
}
