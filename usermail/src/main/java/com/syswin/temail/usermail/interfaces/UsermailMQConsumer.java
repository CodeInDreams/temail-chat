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

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.syswin.temail.usermail.application.UsermailMsgReplyService;
import com.syswin.temail.usermail.application.UsermailService;
import com.syswin.temail.usermail.common.Constants.UsermailAgentEventType;
import com.syswin.temail.usermail.common.ParamsKey;
import com.syswin.temail.usermail.core.IMqConsumer;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class UsermailMQConsumer implements IMqConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(UsermailMQConsumer.class);

  private final UsermailService usermailService;

  private final UsermailMsgReplyService usermailMsgReplyService;

  @Autowired
  public UsermailMQConsumer(UsermailService usermailService, UsermailMsgReplyService usermailMsgReplyService) {
    this.usermailService = usermailService;
    this.usermailMsgReplyService = usermailMsgReplyService;
  }

  /**
   * 单聊mq异步处理消费端
   *
   * @param message mq消息
   * @return boolean
   */
  @Override
  public boolean consumer(String message) {
    JsonParser parser = new JsonParser();
    JsonObject root = parser.parse(message).getAsJsonObject();
    int eventType = root.get(ParamsKey.SessionEventKey.SESSION_MESSAGE_TYPE).getAsInt();
    String from;
    String to;
    String owner;
    String xPacketId;
    String msgId;
    String cdtpHeader;
    LOGGER.debug("mq-receicver-message->{},eventType={}", message, eventType);
    switch (eventType) {
      case UsermailAgentEventType.TRASH_REMOVE_0:
        String temail = root.get(ParamsKey.SessionEventKey.FROM).getAsString();
        JsonElement trashJsonElement = root.get(ParamsKey.SessionEventKey.TRASH_MSG_INFO);
        if (!StringUtils.isEmpty(trashJsonElement)) {
          String trashMsgInfo = trashJsonElement.getAsString();
          List<TrashMailDTO> trashMailDtos = new Gson().fromJson(trashMsgInfo, new TypeToken<List<TrashMailDTO>>() {
          }.getType());
          usermailService.removeMsgFromTrash(temail, trashMailDtos);
        } else {
          usermailService.clearMsgFromTrash(temail);
        }
        break;
      case UsermailAgentEventType.DESTROY_AFTER_READ_2:
        owner = root.get(ParamsKey.SessionEventKey.OWNER).getAsString();
        from = root.get(ParamsKey.SessionEventKey.FROM).getAsString();
        msgId = root.get(ParamsKey.SessionEventKey.MSGID).getAsString();
        to = root.get(ParamsKey.SessionEventKey.TO).getAsString();
        cdtpHeader = root.get(ParamsKey.SessionEventKey.CDTP_HEADER).getAsString();
        xPacketId = root.get(ParamsKey.SessionEventKey.X_PACKET_ID).getAsString();
        usermailService.destroyAfterRead(xPacketId, cdtpHeader, from, to, owner, msgId);
        break;
      case UsermailAgentEventType.REVERT_MSG_3:
        owner = root.get(ParamsKey.SessionEventKey.OWNER).getAsString();
        msgId = root.get(ParamsKey.SessionEventKey.MSGID).getAsString();
        from = root.get(ParamsKey.SessionEventKey.FROM).getAsString();
        to = root.get(ParamsKey.SessionEventKey.TO).getAsString();
        cdtpHeader = root.get(ParamsKey.SessionEventKey.CDTP_HEADER).getAsString();
        xPacketId = root.get(ParamsKey.SessionEventKey.X_PACKET_ID).getAsString();
        usermailService.revertMqHandler(xPacketId, cdtpHeader, from, to, owner, msgId);
        break;
      case UsermailAgentEventType.REVERT_REPLY_MSG_4:
        from = root.get(ParamsKey.SessionEventKey.FROM).getAsString();
        to = root.get(ParamsKey.SessionEventKey.TO).getAsString();
        cdtpHeader = root.get(ParamsKey.SessionEventKey.CDTP_HEADER).getAsString();
        xPacketId = root.get(ParamsKey.SessionEventKey.X_PACKET_ID).getAsString();
        owner = root.get(ParamsKey.SessionEventKey.OWNER).getAsString();
        msgId = root.get(ParamsKey.SessionEventKey.MSGID).getAsString();
        String replyMsgParentId = root.get(ParamsKey.SessionEventKey.REPLY_MSG_PARENT_ID).getAsString();
        usermailMsgReplyService.revertMsgReply(xPacketId, cdtpHeader, from, to, owner, replyMsgParentId, msgId);
        break;
      case UsermailAgentEventType.DESTROY_AFTER_READ_REPLY_MSG_5:
        from = root.get(ParamsKey.SessionEventKey.FROM).getAsString();
        to = root.get(ParamsKey.SessionEventKey.TO).getAsString();
        cdtpHeader = root.get(ParamsKey.SessionEventKey.CDTP_HEADER).getAsString();
        xPacketId = root.get(ParamsKey.SessionEventKey.X_PACKET_ID).getAsString();
        owner = root.get(ParamsKey.SessionEventKey.OWNER).getAsString();
        msgId = root.get(ParamsKey.SessionEventKey.MSGID).getAsString();
        String replyDestroyParentId = root.get(ParamsKey.SessionEventKey.REPLY_MSG_PARENT_ID).getAsString();
        usermailMsgReplyService.destroyAfterRead(xPacketId, cdtpHeader, from, to, owner, msgId, replyDestroyParentId);
        break;
      case UsermailAgentEventType.REMOVE_GROUP_CHAT_MEMBERS_6:
        String groupTemail = root.get(ParamsKey.SessionEventKey.GROUP_TEMAIL).getAsString();
        String member = root.get(ParamsKey.SessionEventKey.TEMAIL).getAsString();
        usermailService.deleteGroupChatSession(groupTemail, member);
        break;
      default:
        LOGGER.error("UsermailMQConsumer consumer eventType={}", eventType);
    }

    return true;
  }
}
