package com.syswin.temail.usermail.application;

import com.google.gson.Gson;
import com.syswin.temail.usermail.common.Contants.SessionEventKey;
import com.syswin.temail.usermail.common.Contants.SessionEventType;
import com.syswin.temail.usermail.configuration.UsermailConfig;
import com.syswin.temail.usermail.core.IMqAdapter;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Usermail2NotfyMqService implements SessionEventType, SessionEventKey {


  private final IMqAdapter mqAdapter;
  private final UsermailConfig usermailConfig;

  @Autowired
  public Usermail2NotfyMqService(IMqAdapter mqAdapter, UsermailConfig usermailConfig) {
    this.mqAdapter = mqAdapter;
    this.usermailConfig = usermailConfig;
  }

  public void sendMqMsgSaveMail(CdtpHeaderDTO headerInfo, String from, String to, String owner, String msgid,
      String toMsg, long seqNo, int eventType, int attachmentSize, String author, List<String> filter) {
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>(18);
    map.put(CDTP_HEADER, headerInfo.getCdtpHeader());
    map.put(X_PACKET_ID, headerInfo.getxPacketId());
    map.put(FROM, from);
    map.put(TO, to);
    map.put(OWNER, owner);
    map.put(MSGID, msgid);
    // 会话消息已接收 (未读+1)
    map.put(SESSION_MESSAGE_TYPE, eventType);
    map.put(TO_MSG, toMsg);
    map.put(TIMESTAMP, System.currentTimeMillis());
    map.put(SEQ_NO, seqNo);
    map.put(ATTACHMENT_SIZE, attachmentSize);
    map.put(AUTHOR, author);
    map.put(FILTER, filter);
    String s = gs.toJson(map);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, s);
  }

  /**
   * 消息状态变化时，通知MQ
   */
  public void sendMqAfterUpdateStatus(CdtpHeaderDTO headerInfo, String from, String to,
      String msgid, int type) {
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>(10);
    map.put(CDTP_HEADER, headerInfo.getCdtpHeader());
    map.put(X_PACKET_ID, headerInfo.getxPacketId());
    map.put(FROM, from);
    map.put(TO, to);
    map.put(TIMESTAMP, System.currentTimeMillis());
    map.put(SESSION_MESSAGE_TYPE, type);
    map.put(MSGID, msgid);
    String s = gs.toJson(map);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, s);
  }

  /**
   * 消息状态变化时，通知MQ
   */
  public void sendMqUpdateMsg(String xPacketId,String cdtpHeader, String from, String to,
      String owner, String msgid, int type) {
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>(12);
    map.put(CDTP_HEADER, cdtpHeader);
    map.put(X_PACKET_ID, xPacketId);
    map.put(FROM, from);
    map.put(TO, to);
    map.put(OWNER, owner);
    map.put(TIMESTAMP, System.currentTimeMillis());
    map.put(SESSION_MESSAGE_TYPE, type);
    map.put(MSGID, msgid);
    String s = gs.toJson(map);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, s);
  }

  /**
   * 删除会话
   */
  public void sendMqAfterDeleteSession(CdtpHeaderDTO headerInfo, String from, String to, boolean deleteAllMsg,
      int eventType) {
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>(11);
    map.put(CDTP_HEADER, headerInfo.getCdtpHeader());
    map.put(X_PACKET_ID, headerInfo.getxPacketId());
    map.put(FROM, from);
    map.put(TO, to);
    map.put(SESSION_MESSAGE_TYPE, eventType);
    map.put(TIMESTAMP, System.currentTimeMillis());
    map.put(DELETE_ALL_MSG, deleteAllMsg);
    String s = gs.toJson(map);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, s);
  }

  /**
   * 发送消息回复
   */
  public void sendMqSaveMsgReply(CdtpHeaderDTO headerInfo, String from, String to, String owner, String msgId,
      String toMsg, long seqNo, int attachmentSize, String parentMsgId) {
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>(17);
    map.put(CDTP_HEADER, headerInfo.getCdtpHeader());
    map.put(X_PACKET_ID, headerInfo.getxPacketId());
    map.put(FROM, from);
    map.put(TO, to);
    map.put(OWNER, owner);
    map.put(PARENT_MSGID, parentMsgId);
    map.put(MSGID, msgId);
    // 会话消息已接收 (未读+1)
    map.put(SESSION_MESSAGE_TYPE, EVENT_TYPE_18);
    map.put(TO_MSG, toMsg);
    map.put(TIMESTAMP, System.currentTimeMillis());
    map.put(SEQ_NO, seqNo);
    map.put(ATTACHMENT_SIZE, attachmentSize);
    String s = gs.toJson(map);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, s);
  }

  /**
   * 消息回复更新状态时，通知MQ
   */
  public void sendMqAfterUpdateMsgReply(String xPacketId,String cdtpHeader, String from, String to,
      String owner, String msgId, int type, String parentMsgId) {
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>(13);
    map.put(CDTP_HEADER, cdtpHeader);
    map.put(X_PACKET_ID, xPacketId);
    map.put(FROM, from);
    map.put(TO, to);
    map.put(OWNER, owner);
    map.put(TIMESTAMP, System.currentTimeMillis());
    map.put(SESSION_MESSAGE_TYPE, type);
    map.put(MSGID, msgId);
    map.put(PARENT_MSGID, parentMsgId);
    String s = gs.toJson(map);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, s);
  }

  /**
   * 消息回复更新状态时，通知MQ
   */
  public void sendMqAfterUpdateMsgReply(CdtpHeaderDTO headerInfo, String from, String to,
      String msgId, int type, String parentMsgId) {
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>(12);
    map.put(CDTP_HEADER, headerInfo.getCdtpHeader());
    map.put(X_PACKET_ID, headerInfo.getxPacketId());
    map.put(FROM, from);
    map.put(TO, to);
    map.put(TIMESTAMP, System.currentTimeMillis());
    map.put(SESSION_MESSAGE_TYPE, type);
    map.put(MSGID, msgId);
    map.put(PARENT_MSGID, parentMsgId);
    String s = gs.toJson(map);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, s);
  }

  /**
   * 消息回复删除时，通知MQ
   */
  public void sendMqAfterRemoveMsgReply(CdtpHeaderDTO headerInfo, String from, String to, String owner,
      List<String> msgIds, int type, String parentMsgId) {
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>(13);
    map.put(CDTP_HEADER, headerInfo.getCdtpHeader());
    map.put(X_PACKET_ID, headerInfo.getxPacketId());
    map.put(FROM, from);
    map.put(TO, to);
    map.put(OWNER, owner);
    map.put(TIMESTAMP, System.currentTimeMillis());
    map.put(SESSION_MESSAGE_TYPE, type);
    map.put(MSGID, gs.toJson(msgIds));
    map.put(PARENT_MSGID, parentMsgId);
    String s = gs.toJson(map);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, s);
  }

  /**
   * 单聊归档
   */
  public void sendMqAfterUpdateArchiveStatus(CdtpHeaderDTO headerInfo, String from, String to,int type) {
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>(9);
    map.put(CDTP_HEADER, headerInfo.getCdtpHeader());
    map.put(X_PACKET_ID, headerInfo.getxPacketId());
    map.put(FROM, from);
    map.put(TO, to);
    map.put(TIMESTAMP, System.currentTimeMillis());
    map.put(SESSION_MESSAGE_TYPE, type);
    String s = gs.toJson(map);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, s);
  }

  /**
   * 消息移入废纸篓
   */
  public void sendMqMoveTrashNotify(CdtpHeaderDTO headerInfo, String from, String to,
      List<String> msgids, int type) {
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>(12);
    map.put(CDTP_HEADER, headerInfo.getCdtpHeader());
    map.put(X_PACKET_ID, headerInfo.getxPacketId());
    map.put(FROM, from);
    map.put(TO, to);
    map.put(OWNER,from);
    map.put(TIMESTAMP, System.currentTimeMillis());
    map.put(SESSION_MESSAGE_TYPE, type);
    map.put(MSGID, gs.toJson(msgids));
    String s = gs.toJson(map);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, s);
  }

  /**
   * 废纸篓消息还原、删除
   */
  public void sendMqTrashMsgOpratorNotify(CdtpHeaderDTO headerInfo, String owner,
      List<TrashMailDTO> trashMailDtoList, int type) {
    Gson gs = new Gson();
    Map<String, Object> map = new HashMap<>(9);
    map.put(CDTP_HEADER, headerInfo.getCdtpHeader());
    map.put(X_PACKET_ID, headerInfo.getxPacketId());
    map.put(OWNER,owner);
    map.put(TIMESTAMP, System.currentTimeMillis());
    map.put(SESSION_MESSAGE_TYPE, type);
    if(trashMailDtoList != null && !trashMailDtoList.isEmpty()) {
      map.put(SessionEventKey.TRASH_MSG_INFO, gs.toJson(trashMailDtoList));
    }
    String s = gs.toJson(map);
    mqAdapter.sendMessage(usermailConfig.mqTopic, owner, s);
  }

}
