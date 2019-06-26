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
  private final Gson gson = new Gson();


  public UsermailMqService(IMqAdapter mqAdapter, UsermailConfig usermailConfig) {
    this.mqAdapter = mqAdapter;
    this.usermailConfig = usermailConfig;
  }

  /**
   * 删除废纸篓消息
   *
   * @param owner 消息所属人
   * @param trashMailDtoList 被操作的消息列表
   * @param type 0:删除废纸篓消息
   */
  public void sendMqRemoveTrash(String owner,
      List<TrashMailDTO> trashMailDtoList, int type) {
    Map<String, Object> usermailMap = new HashMap<>(7);
    usermailMap.put(ParamsKey.SessionEventKey.FROM, owner);
    usermailMap.put(ParamsKey.SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    usermailMap.put(ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE, type);
    if (trashMailDtoList != null && !trashMailDtoList.isEmpty()) {
      usermailMap.put(ParamsKey.SessionEventKey.TRASH_MSG_INFO, gson.toJson(trashMailDtoList));
    }
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, gson.toJson(usermailMap));
  }

  /**
   * 单聊消息阅后即焚
   *
   * @param xPacketId 头信息中的xPacketId
   * @param cdtpHeader 头信息中的header
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所属人
   * @param msgId 消息id
   */
  public void sendMqDestroyMsg(String xPacketId, String cdtpHeader, String from, String to, String owner,
      String msgId) {
    Map<String, Object> usermailMap = new HashMap<>(12);
    usermailMap.put(ParamsKey.SessionEventKey.X_PACKET_ID, xPacketId);
    usermailMap.put(ParamsKey.SessionEventKey.CDTP_HEADER, cdtpHeader);
    usermailMap.put(ParamsKey.SessionEventKey.OWNER, owner);
    usermailMap.put(ParamsKey.SessionEventKey.FROM, from);
    usermailMap.put(ParamsKey.SessionEventKey.TO, to);
    usermailMap.put(ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.DESTROY_AFTER_READ_2);
    usermailMap.put(ParamsKey.SessionEventKey.MSGID, msgId);
    usermailMap.put(ParamsKey.SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, gson.toJson(usermailMap));
  }

  /**
   * 单聊消息撤回
   *
   * @param xPacketId 头信息中的xPacketId
   * @param cdtpHeader 头信息中的header
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所属人
   * @param msgId 消息id
   */
  public void sendMqRevertMsg(String xPacketId, String cdtpHeader, String from, String to,
      String owner, String msgId) {
    Map<String, Object> usermailMap = new HashMap<>(12);
    usermailMap.put(ParamsKey.SessionEventKey.OWNER, owner);
    usermailMap.put(ParamsKey.SessionEventKey.FROM, from);
    usermailMap.put(ParamsKey.SessionEventKey.TO, to);
    usermailMap.put(ParamsKey.SessionEventKey.MSGID, msgId);
    usermailMap.put(ParamsKey.SessionEventKey.X_PACKET_ID, xPacketId);
    usermailMap.put(ParamsKey.SessionEventKey.CDTP_HEADER, cdtpHeader);
    usermailMap.put(ParamsKey.SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    usermailMap.put(ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.REVERT_MSG_3);
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, gson.toJson(usermailMap));
  }

  /**
   * 单聊回复消息撤回
   *
   * @param xPacketId 头信息中的xPacketId
   * @param cdtpHeader 头信息中的header
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所属人
   * @param parentMsgReplyId 父消息id
   * @param msgId 消息id
   */
  public void sendMqRevertReplyMsg(String xPacketId, String cdtpHeader, String from, String to, String owner,
      String parentMsgReplyId, String msgId) {
    Map<String, Object> usermailMap = new HashMap<>(13);
    usermailMap.put(ParamsKey.SessionEventKey.X_PACKET_ID, xPacketId);
    usermailMap.put(ParamsKey.SessionEventKey.CDTP_HEADER, cdtpHeader);
    usermailMap.put(ParamsKey.SessionEventKey.OWNER, owner);
    usermailMap.put(ParamsKey.SessionEventKey.FROM, from);
    usermailMap.put(ParamsKey.SessionEventKey.TO, to);
    usermailMap.put(ParamsKey.SessionEventKey.MSGID, msgId);
    usermailMap.put(ParamsKey.SessionEventKey.REPLY_MSG_PARENT_ID, parentMsgReplyId);
    usermailMap.put(ParamsKey.SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    usermailMap.put(ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.REVERT_REPLY_MSG_4);
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, gson.toJson(usermailMap));

  }

  /**
   * 单聊回复消息阅后即焚
   *
   * @param xPacketId 头信息中的xPacketId
   * @param cdtpHeader 头信息中的header
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所属人
   * @param msgId 消息id
   * @param parentMsgId 父消息id
   */
  public void sendMqReplyMsgDestroyAfterRead(String xPacketId, String cdtpHeader, String from, String to,
      String owner, String msgId, String parentMsgId) {
    Map<String, Object> usermailMap = new HashMap<>(13);
    usermailMap.put(ParamsKey.SessionEventKey.X_PACKET_ID, xPacketId);
    usermailMap.put(ParamsKey.SessionEventKey.CDTP_HEADER, cdtpHeader);
    usermailMap.put(ParamsKey.SessionEventKey.FROM, from);
    usermailMap.put(ParamsKey.SessionEventKey.TO, to);
    usermailMap.put(ParamsKey.SessionEventKey.OWNER, owner);
    usermailMap.put(ParamsKey.SessionEventKey.MSGID, msgId);
    usermailMap.put(ParamsKey.SessionEventKey.REPLY_MSG_PARENT_ID, parentMsgId);
    usermailMap.put(ParamsKey.SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    usermailMap
        .put(ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.DESTROY_AFTER_READ_REPLY_MSG_5);
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, owner, gson.toJson(usermailMap));
  }

  /**
   * 群聊移除群成员事件
   *
   * @param groupTemail 群聊地址
   * @param temail 被移除人地址
   */
  public void sendMqRemoveGroupMemberMsg(String groupTemail, String temail) {
    Map<String, Object> usermailMap = new HashMap<>(7);
    usermailMap.put(ParamsKey.SessionEventKey.GROUP_TEMAIL, groupTemail);
    usermailMap.put(ParamsKey.SessionEventKey.TEMAIL, temail);
    usermailMap.put(ParamsKey.SessionEventKey.TIMESTAMP, System.currentTimeMillis());
    usermailMap.put(ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE, UsermailAgentEventType.REMOVE_GROUP_CHAT_MEMBERS_6);
    mqAdapter.sendMessage(usermailConfig.mqUserMailAgentTopic, groupTemail, gson.toJson(usermailMap));
  }
}
