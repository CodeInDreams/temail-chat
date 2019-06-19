package com.syswin.temail.usermail.application;

import com.google.gson.Gson;
import com.syswin.temail.usermail.common.ParamsKey;
import com.syswin.temail.usermail.common.ParamsKey.SessionEventKey;
import com.syswin.temail.usermail.common.SessionEventType;
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
public class Usermail2NotifyMqService implements SessionEventType, SessionEventKey {


  private final IMqAdapter mqAdapter;
  private final UsermailConfig usermailConfig;
  private final Gson gs = new Gson();

  @Autowired
  public Usermail2NotifyMqService(IMqAdapter mqAdapter, UsermailConfig usermailConfig) {
    this.mqAdapter = mqAdapter;
    this.usermailConfig = usermailConfig;
  }

  /**
   * 发送消息时，通知MQ
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所属人
   * @param msgId 消息id
   * @param toMsg 发送的消息内容
   * @param seqNo 会话序号
   * @param eventType 事件类型
   * @param attachmentSize 附件大小
   * @param author 消息作者
   * @param filter 能接收到此条消息的人（群聊消息的前提下，字段为空即发送给群聊中的所有成员）
   */
  public void sendMqMsgSaveMail(CdtpHeaderDTO headerInfo, String from, String to, String owner, String msgId,
      String toMsg, long seqNo, int eventType, int attachmentSize, String author, List<String> filter) {
    Map<String, Object> eventMap = new HashMap<>(18);
    combineNormalParam(headerInfo, eventType, from, to, eventMap);
    eventMap.put(OWNER, owner);
    eventMap.put(MSGID, msgId);
    // 会话消息已接收 (未读+1)
    eventMap.put(TO_MSG, toMsg);
    eventMap.put(SEQ_NO, seqNo);
    eventMap.put(ATTACHMENT_SIZE, attachmentSize);
    eventMap.put(AUTHOR, author);
    eventMap.put(FILTER, filter);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, gs.toJson(eventMap));
  }

  /**
   * 消息状态变化时，通知MQ
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param msgId 消息id
   * @param eventType 事件类型
   */
  public void sendMqAfterUpdateStatus(CdtpHeaderDTO headerInfo, String from, String to,
      String msgId, int eventType) {
    Map<String, Object> eventMap = new HashMap<>(10);
    combineNormalParam(headerInfo, eventType, from, to, eventMap);
    eventMap.put(MSGID, msgId);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, gs.toJson(eventMap));
  }

  /**
   * 消息状态变化时，通知MQ
   *
   * @param xPacketId 头信息中的xPacketId
   * @param cdtpHeader 头信息中的header
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所属人
   * @param msgId 消息id
   * @param eventType 事件类型
   */
  public void sendMqUpdateMsg(String xPacketId, String cdtpHeader, String from, String to,
      String owner, String msgId, int eventType) {
    Map<String, Object> eventMap = new HashMap<>(12);
    combineNormalParam(new CdtpHeaderDTO(cdtpHeader, xPacketId), eventType, from, to, eventMap);
    eventMap.put(OWNER, owner);
    eventMap.put(MSGID, msgId);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, gs.toJson(eventMap));
  }

  /**
   * 删除会话
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param deleteAllMsg 是否删除所有消息的标志
   * @param eventType 事件类型
   */
  public void sendMqAfterDeleteSession(CdtpHeaderDTO headerInfo, String from, String to, boolean deleteAllMsg,
      int eventType) {
    Map<String, Object> eventMap = new HashMap<>(11);
    combineNormalParam(headerInfo, eventType, from, to, eventMap);
    eventMap.put(DELETE_ALL_MSG, deleteAllMsg);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, gs.toJson(eventMap));
  }

  /**
   * 发送消息回复，通知MQ
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所属人
   * @param msgId 消息id
   * @param toMsg 发送的消息
   * @param seqNo 会话序号
   * @param attachmentSize 附件大小
   * @param parentMsgId 父消息id
   */
  public void sendMqSaveMsgReply(CdtpHeaderDTO headerInfo, String from, String to, String owner, String msgId,
      String toMsg, long seqNo, int attachmentSize, String parentMsgId) {
    Map<String, Object> eventMap = new HashMap<>(17);
    combineNormalParam(headerInfo, EVENT_TYPE_18, from, to, eventMap);
    eventMap.put(OWNER, owner);
    eventMap.put(PARENT_MSGID, parentMsgId);
    eventMap.put(MSGID, msgId);
    // 会话消息已接收 (未读+1)
    eventMap.put(TO_MSG, toMsg);
    eventMap.put(SEQ_NO, seqNo);
    eventMap.put(ATTACHMENT_SIZE, attachmentSize);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, gs.toJson(eventMap));
  }

  /**
   * 消息回复更新状态时，通知MQ
   *
   * @param xPacketId 头信息中的xPacketId
   * @param cdtpHeader 头信息中的header
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所属人
   * @param msgId 消息id
   * @param eventType 事件类型
   * @param parentMsgId 父消息id
   */
  public void sendMqAfterUpdateMsgReply(String xPacketId, String cdtpHeader, String from, String to,
      String owner, String msgId, int eventType, String parentMsgId) {
    Map<String, Object> eventMap = new HashMap<>(13);
    combineNormalParam(new CdtpHeaderDTO(cdtpHeader, xPacketId), eventType, from, to, eventMap);
    eventMap.put(OWNER, owner);
    eventMap.put(MSGID, msgId);
    eventMap.put(PARENT_MSGID, parentMsgId);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, gs.toJson(eventMap));
  }

  /**
   * 消息回复删除时，通知MQ
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所属人
   * @param msgIds 消息id列表
   * @param eventType 事件类型
   * @param parentMsgId 父消息id
   */
  public void sendMqAfterRemoveMsgReply(CdtpHeaderDTO headerInfo, String from, String to, String owner,
      List<String> msgIds, int eventType, String parentMsgId) {
    Map<String, Object> eventMap = new HashMap<>(13);
    combineNormalParam(headerInfo, eventType, from, to, eventMap);
    eventMap.put(OWNER, owner);
    eventMap.put(MSGID, gs.toJson(msgIds));
    eventMap.put(PARENT_MSGID, parentMsgId);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, gs.toJson(eventMap));
  }

  /**
   * 单聊归档状态变更时，通知MQ
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param eventType 事件类型（归档或取消归档）
   */
  public void sendMqAfterUpdateArchiveStatus(CdtpHeaderDTO headerInfo, String from, String to, int eventType) {
    Map<String, Object> eventMap = new HashMap<>(9);
    combineNormalParam(headerInfo, eventType, from, to, eventMap);
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, gs.toJson(eventMap));
  }

  /**
   * 消息移入废纸篓
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param msgIds 消息id列表
   * @param eventType 事件类型
   */
  public void sendMqMoveTrashNotify(CdtpHeaderDTO headerInfo, String from, String to,
      List<String> msgIds, int eventType) {
    Map<String, Object> eventMap = new HashMap<>(12);
    combineNormalParam(headerInfo, eventType, from, to, eventMap);
    eventMap.put(OWNER, from);
    eventMap.put(MSGID, gs.toJson(msgIds));
    mqAdapter.sendMessage(usermailConfig.mqTopic, from, gs.toJson(eventMap));
  }

  /**
   * 废纸篓消息还原、删除
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param owner 消息所属人
   * @param trashMailDtoList 废纸篓信息列表
   * @param eventType 事件类型
   */
  public void sendMqTrashMsgOpratorNotify(CdtpHeaderDTO headerInfo, String owner,
      List<TrashMailDTO> trashMailDtoList, int eventType) {
    Map<String, Object> eventMap = new HashMap<>(9);
    combineNormalParam(headerInfo, eventType, null, null, eventMap);
    eventMap.put(OWNER, owner);
    if (trashMailDtoList != null && !trashMailDtoList.isEmpty()) {
      eventMap.put(ParamsKey.SessionEventKey.TRASH_MSG_INFO, gs.toJson(trashMailDtoList));
    }
    mqAdapter.sendMessage(usermailConfig.mqTopic, owner, gs.toJson(eventMap));
  }

  /**
   * 组装公共参数
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param eventType 事件类型
   * @param from 发件人
   * @param to 收件人
   * @param eventMap 参数集合
   */
  private void combineNormalParam(CdtpHeaderDTO headerInfo, int eventType, String from, String to,
      Map<String, Object> eventMap) {
    eventMap.put(CDTP_HEADER, headerInfo.getCdtpHeader());
    eventMap.put(X_PACKET_ID, headerInfo.getxPacketId());
    eventMap.put(TIMESTAMP, System.currentTimeMillis());
    eventMap.put(SESSION_MESSAGE_TYPE, eventType);
    eventMap.put(FROM, from);
    eventMap.put(TO, to);
  }
}
