package com.syswin.temail.usermail.application;

import static com.syswin.temail.usermail.common.Contants.SessionEventKey.PACKET_ID_SUFFIX;

import com.syswin.temail.transactional.TemailShardingTransactional;
import com.syswin.temail.usermail.common.Contants.RESULT_CODE;
import com.syswin.temail.usermail.common.Contants.ReplyCountStatus;
import com.syswin.temail.usermail.common.Contants.SessionEventType;
import com.syswin.temail.usermail.common.Contants.TemailStatus;
import com.syswin.temail.usermail.common.Contants.TemailType;
import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import com.syswin.temail.usermail.core.util.MsgCompressor;
import com.syswin.temail.usermail.core.util.SeqIdFilter;
import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.domains.UsermailMsgReply;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import com.syswin.temail.usermail.domains.UsermailRepo;
import com.syswin.temail.usermail.domains.UsermailMsgReplyRepo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class UsermailMsgReplyService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UsermailMsgReplyService.class);
  private final UsermailRepo usermailRepo;
  private final IUsermailAdapter usermailAdapter;
  private final UsermailMsgReplyRepo usermailMsgReplyRepo;
  private final Usermail2NotfyMqService usermail2NotfyMqService;
  private final UsermailSessionService usermailSessionService;
  private final MsgCompressor msgCompressor;
  private final UsermailMqService usermailMqService;
  private final ConvertMsgService convertMsgService;

  @Autowired
  public UsermailMsgReplyService(UsermailRepo usermailRepo, IUsermailAdapter usermailAdapter,
      UsermailMsgReplyRepo usermailMsgReplyRepo, Usermail2NotfyMqService usermail2NotfyMqService,
      UsermailSessionService usermailSessionService,
      MsgCompressor msgCompressor, UsermailMqService usermailMqService,
      ConvertMsgService convertMsgService) {
    this.usermailRepo = usermailRepo;
    this.usermailAdapter = usermailAdapter;
    this.usermailMsgReplyRepo = usermailMsgReplyRepo;
    this.usermail2NotfyMqService = usermail2NotfyMqService;
    this.usermailSessionService = usermailSessionService;
    this.msgCompressor = msgCompressor;
    this.usermailMqService = usermailMqService;
    this.convertMsgService = convertMsgService;
  }

  @TemailShardingTransactional( shardingField = "#owner")
  public Map createMsgReply(CdtpHeaderDTO cdtpHeaderDto, String from, String to, String message, String msgId,
      String parentMsgId, int type, int attachmentSize, String owner) {
    String sessionid = usermailSessionService.getSessionID(from, to);
    msgReplyTypeValidate(parentMsgId, owner);
    Map result = new HashMap(2);
    long msgReplySeqNo = usermailAdapter.getMsgReplySeqNo(parentMsgId, owner);
    UsermailMsgReply usermailMsgReply = new UsermailMsgReply(usermailAdapter.getMsgReplyPkID(), parentMsgId, msgId,
        from, to, msgReplySeqNo, "", TemailStatus.STATUS_NORMAL_0, type, owner, sessionid,
        msgCompressor.zipWithDecode(message));
    usermailMsgReplyRepo.insert(usermailMsgReply);

    // 更新最新回复消息id
    String lastReplyMsgid = usermailMsgReply.getMsgid();
    usermailRepo.updateReplyCountAndLastReplyMsgid(parentMsgId, owner, ReplyCountStatus.INCR.value(), lastReplyMsgid);
    LOGGER.debug("new rely created, update msgId={} lastReplyMsgid={}", parentMsgId, lastReplyMsgid);

    usermail2NotfyMqService
        .sendMqSaveMsgReply(cdtpHeaderDto, from, to, owner, msgId, message, msgReplySeqNo, attachmentSize,
            parentMsgId);
    result.put("msgId", msgId);
    result.put("seqId", msgReplySeqNo);
    return result;
  }

  @TemailShardingTransactional( shardingField = "#owner")
  public void revertMsgReply(String xPacketId, String cdtpHeader, String from, String to, String owner,
      String replyMsgParentId, String msgId) {
    UsermailMsgReply usermailMsgReply = new UsermailMsgReply();
    usermailMsgReply.setMsgid(msgId);
    usermailMsgReply.setStatus(TemailStatus.STATUS_REVERT_1);
    usermailMsgReply.setOwner(owner);
    int count = usermailMsgReplyRepo.revertUsermailReply(usermailMsgReply);
    if (count <= 0) {
      LOGGER.warn(
          "Message Reply revert failed, xPacketId is {}, cdtpHeader is {}, from is {}, to is {}, msgId is {}, parentMsgId is {}, owner is {}",
          xPacketId, cdtpHeader, from, to, msgId, replyMsgParentId, owner);
      return;
    }
    Usermail usermail = usermailRepo.getUsermailByMsgid(replyMsgParentId, owner);
    if (usermail != null) {
      updateUsermailLastReplyId(usermail, replyMsgParentId, msgId);
      usermail2NotfyMqService
          .sendMqAfterUpdateMsgReply(xPacketId, cdtpHeader, from, to, owner, msgId, SessionEventType.EVENT_TYPE_19,
              replyMsgParentId);
    } else {
      LOGGER.warn("label-mq-revertMsgReply, parentMsgid={}, not exist.", replyMsgParentId);
    }
  }

  @TemailShardingTransactional( shardingField = "#from")
  public void revertMsgReply(CdtpHeaderDTO cdtpHeaderDto, String parentMsgReplyId, String msgId, String from,
      String to) {
    msgReplyTypeValidate(parentMsgReplyId);
    usermailMqService.sendMqRevertReplyMsg(cdtpHeaderDto.getxPacketId(), cdtpHeaderDto.getCdtpHeader(), from, to, to,
        parentMsgReplyId, msgId);
    usermailMqService
        .sendMqRevertReplyMsg(cdtpHeaderDto.getxPacketId() + PACKET_ID_SUFFIX, cdtpHeaderDto.getCdtpHeader(), from, to,
            from,
            parentMsgReplyId, msgId);
  }

  @TemailShardingTransactional( shardingField = "#from")
  public void removeMsgReplys(CdtpHeaderDTO cdtpHeaderDto, String parentMsgReplyId, List<String> msgIds, String from,
      String to) {
    Usermail usermail = msgReplyTypeValidate(parentMsgReplyId, from);
    if (CollectionUtils.isEmpty(msgIds)) {
      throw new IllegalGmArgsException(RESULT_CODE.ERROR_REQUEST_PARAM);
    }
    LOGGER.info("Label-delete-usermail-reply: delete reply messages，from = {},to = {},msgIds = {}", from, to, msgIds);
    int count = usermailMsgReplyRepo.deleteBatchMsgReplyStatus(from, msgIds);
    String lastReplyMsgId = usermail.getLastReplyMsgId();
    //如果删除的消息中包含最新的回复消息，这需要将最新回复消息回滚到上一条
    if (!StringUtils.isEmpty(lastReplyMsgId) && msgIds.contains(lastReplyMsgId)) {
      UsermailMsgReply lastUsermailMsgReply = usermailMsgReplyRepo
          .getLastUsermailReply(parentMsgReplyId, usermail.getOwner(), TemailStatus.STATUS_NORMAL_0);
      if (lastUsermailMsgReply != null) {
        lastReplyMsgId = lastUsermailMsgReply.getMsgid();
      } else {
        //删除了所有消息的场景
        lastReplyMsgId = "";
      }
    }
    usermailRepo.updateReplyCountAndLastReplyMsgid(parentMsgReplyId, usermail.getOwner(), -count, lastReplyMsgId);
    usermail2NotfyMqService.sendMqAfterRemoveMsgReply(cdtpHeaderDto, from, to, from, msgIds, SessionEventType.EVENT_TYPE_20,
        parentMsgReplyId);
  }

  @TemailShardingTransactional( shardingField = "#owner")
  public List<UsermailMsgReply> getMsgReplys(CdtpHeaderDTO cdtpHeaderDto, String parentMsgid, int pageSize, long seqId,
      String signal, String owner, String filterSeqIds) {
    msgReplyTypeValidate(parentMsgid, owner);
    QueryMsgReplyDTO dto = new QueryMsgReplyDTO();
    dto.setFromSeqNo(seqId);
    dto.setPageSize(pageSize);
    dto.setParentMsgid(parentMsgid);
    dto.setSignal(signal);
    dto.setOwner(owner);
    List<UsermailMsgReply> data = this.convertMsgService.convertReplyMsg(usermailMsgReplyRepo.getMsgReplys(dto));
    List<UsermailMsgReply> dataFilter = new ArrayList<>();
    if (StringUtils.isNotEmpty(filterSeqIds) && !CollectionUtils.isEmpty(data)) {
      boolean isAfter = "after".equals(signal);
      SeqIdFilter filter = new SeqIdFilter(filterSeqIds, isAfter);
      for (int i = 0; i < data.size(); i++) {
        if (filter.filter(data.get(i).getSeqNo())) {
          dataFilter.add(data.get(i));
        }
      }
    } else {
      dataFilter = data;
    }
    return dataFilter;
  }

  @TemailShardingTransactional( shardingField = "#owner")
  public void destoryAfterRead(String xPacketId, String cdtpHeader, String from, String to, String owner, String msgId,
      String replyMsgParentId) {
    int count = usermailMsgReplyRepo.destoryAfterRead(owner, msgId, TemailStatus.STATUS_DESTORY_AFTER_READ_2);
    if (count <= 0) {
      LOGGER.warn(
          "Message destoryAfterRead failed, xPacketId is {}, cdtpHeader is {}, from is {}, to is {}, msgId is {}, owner is {}",
          xPacketId, cdtpHeader, from, to, msgId, owner);
      return;
    }
    Usermail usermail = usermailRepo.getUsermailByMsgid(replyMsgParentId, owner);
    if (usermail != null) {
      updateUsermailLastReplyId(usermail, replyMsgParentId, msgId);
      usermail2NotfyMqService.sendMqUpdateMsg(xPacketId, cdtpHeader, to, from, owner, msgId, SessionEventType.EVENT_TYPE_26);
    } else {
      LOGGER.warn("label-mq-destory-after-read: parentMsgId={},not-exist", replyMsgParentId);
    }
  }

  @TemailShardingTransactional( shardingField = "#from")
  public void destoryAfterRead(CdtpHeaderDTO headerInfo, String from, String to, String msgId) {
    UsermailMsgReply usermailMsgReply = new UsermailMsgReply();
    usermailMsgReply.setOwner(from);
    usermailMsgReply.setMsgid(msgId);
    UsermailMsgReply msgReply = usermailMsgReplyRepo.getMsgReplyByCondition(usermailMsgReply);
    if (msgReply != null && msgReply.getType() == TemailType.TYPE_DESTORY_AFTER_READ_1) {
      String parentMsgId = msgReply.getParentMsgid();
      usermailMqService
          .sendMqReplyMsgDestoryAfterRead(headerInfo.getxPacketId(), headerInfo.getCdtpHeader(), from, to, to, msgId,
              parentMsgId);
      usermailMqService
          .sendMqReplyMsgDestoryAfterRead(headerInfo.getxPacketId(), headerInfo.getCdtpHeader() + PACKET_ID_SUFFIX,
              from, to, from, msgId,
              parentMsgId);
    } else {
      LOGGER.warn("msgReply-destoryAfterRead-illegal-msgid={}", msgId);
    }

  }


  /**
   * 校验源消息类型是否为合法
   */
  private List<Usermail> msgReplyTypeValidate(String parentMsgId) {
    List<Usermail> usermails = usermailRepo.getUsermailListByMsgid(parentMsgId);
    if (CollectionUtils.isEmpty(usermails)) {
      LOGGER.warn("parentMsgId status is error:{}", parentMsgId);
      throw new IllegalGmArgsException(RESULT_CODE.ERROR_ILLEGAL_PARENT_MSG_ID);
    }
    return usermails;
  }

  /**
   * 校验源消息类型是否为合法
   */
  public Usermail msgReplyTypeValidate(String parentMsgId, String owner) {

    Usermail usermailByMsgid = usermailRepo.getUsermailByMsgid(parentMsgId, owner);
    if (usermailByMsgid == null || (usermailByMsgid.getStatus() != TemailStatus.STATUS_NORMAL_0
        && usermailByMsgid.getStatus() != TemailStatus.STATUS_TRASH_4)) {
      LOGGER.warn("parentMsgId is {}", parentMsgId);
      throw new IllegalGmArgsException(RESULT_CODE.ERROR_ILLEGAL_PARENT_MSG_ID);
    }
    return usermailByMsgid;
  }

  private void updateUsermailLastReplyId(Usermail usermail, String parentMsgId, String msgId) {
    String lastReplyMsgId = usermail.getLastReplyMsgId();
    //如果撤回或者阅后即焚的消息是最新的回复消息需要将最新回复消息回滚到上一条
    if (!StringUtils.isEmpty(lastReplyMsgId) && msgId.equals(lastReplyMsgId)) {
      UsermailMsgReply lastUsermailMsgReply = usermailMsgReplyRepo
          .getLastUsermailReply(parentMsgId, usermail.getOwner(), TemailStatus.STATUS_NORMAL_0);
      if (lastUsermailMsgReply != null) {
        lastReplyMsgId = lastUsermailMsgReply.getMsgid();
      } else {
        //删除了所有消息的场景
        lastReplyMsgId = "";
      }
    }
    usermailRepo
        .updateReplyCountAndLastReplyMsgid(parentMsgId, usermail.getOwner(), ReplyCountStatus.DECR.value(),
            lastReplyMsgId);
  }

}
