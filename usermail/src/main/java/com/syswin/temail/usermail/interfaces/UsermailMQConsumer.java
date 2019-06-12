package com.syswin.temail.usermail.interfaces;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.syswin.temail.usermail.application.UsermailMsgReplyService;
import com.syswin.temail.usermail.application.UsermailService;
import com.syswin.temail.usermail.common.Constants.SessionEventKey;
import com.syswin.temail.usermail.common.Constants.UsermailAgentEventType;
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
  public UsermailMQConsumer(UsermailService usermailService, UsermailMsgReplyService usermailMsgReplyService){
    this.usermailService = usermailService;
    this.usermailMsgReplyService = usermailMsgReplyService;
  }

  @Override
  public boolean consumer(String message) {
    JsonParser parser = new JsonParser();
    JsonObject root = parser.parse(message).getAsJsonObject();
    int eventType = root.get(SessionEventKey.SESSION_MESSAGE_TYPE).getAsInt();
    String from;
    String to;
    String owner;
    String xPacketId;
    String msgId;
    String cdtpHeader;
    LOGGER.debug("mq-receicver-message->{},eventType={}", message, eventType);
    switch (eventType) {
      case UsermailAgentEventType.TRASH_REMOVE_0:
        String temail = root.get(SessionEventKey.FROM).getAsString();
        JsonElement trashJsonElement = root.get(SessionEventKey.TRASH_MSG_INFO);
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
        owner = root.get(SessionEventKey.OWNER).getAsString();
        from = root.get(SessionEventKey.FROM).getAsString();
        msgId = root.get(SessionEventKey.MSGID).getAsString();
        to = root.get(SessionEventKey.TO).getAsString();
        cdtpHeader = root.get(SessionEventKey.CDTP_HEADER).getAsString();
        xPacketId = root.get(SessionEventKey.X_PACKET_ID).getAsString();
        usermailService.destroyAfterRead(xPacketId, cdtpHeader, from, to, owner, msgId);
        break;
      case UsermailAgentEventType.REVERT_MSG_3:
        owner = root.get(SessionEventKey.OWNER).getAsString();
        msgId = root.get(SessionEventKey.MSGID).getAsString();
        from = root.get(SessionEventKey.FROM).getAsString();
        to = root.get(SessionEventKey.TO).getAsString();
        cdtpHeader = root.get(SessionEventKey.CDTP_HEADER).getAsString();
        xPacketId = root.get(SessionEventKey.X_PACKET_ID).getAsString();
        usermailService.revert(xPacketId, cdtpHeader, from, to, owner, msgId);
        break;
      case UsermailAgentEventType.REVERT_REPLY_MSG_4:
        from = root.get(SessionEventKey.FROM).getAsString();
        to = root.get(SessionEventKey.TO).getAsString();
        cdtpHeader = root.get(SessionEventKey.CDTP_HEADER).getAsString();
        xPacketId = root.get(SessionEventKey.X_PACKET_ID).getAsString();
        owner = root.get(SessionEventKey.OWNER).getAsString();
        msgId = root.get(SessionEventKey.MSGID).getAsString();
        String replyMsgParentId = root.get(SessionEventKey.REPLY_MSG_PARENT_ID).getAsString();
        usermailMsgReplyService.revertMsgReply(xPacketId, cdtpHeader, from, to, owner, replyMsgParentId, msgId);
        break;
      case UsermailAgentEventType.DESTROY_AFTER_READ_REPLY_MSG_5:
        from = root.get(SessionEventKey.FROM).getAsString();
        to = root.get(SessionEventKey.TO).getAsString();
        cdtpHeader = root.get(SessionEventKey.CDTP_HEADER).getAsString();
        xPacketId = root.get(SessionEventKey.X_PACKET_ID).getAsString();
        owner = root.get(SessionEventKey.OWNER).getAsString();
        msgId = root.get(SessionEventKey.MSGID).getAsString();
        String replyDestroyParentId = root.get(SessionEventKey.REPLY_MSG_PARENT_ID).getAsString();
        usermailMsgReplyService.destoryAfterRead(xPacketId, cdtpHeader, from, to, owner, msgId, replyDestroyParentId);
        break;
      case UsermailAgentEventType.REMOVE_GROUP_CHAT_MEMBERS_6:
        String groupTemail = root.get(SessionEventKey.GROUP_TEMAIL).getAsString();
        String member = root.get(SessionEventKey.TEMAIL).getAsString();
        usermailService.deleteGroupChatSession(groupTemail, member);
        break;
      default:
        LOGGER.error("UsermailMQConsumer consumer eventType={}", eventType);
    }

    return true;
  }
}
