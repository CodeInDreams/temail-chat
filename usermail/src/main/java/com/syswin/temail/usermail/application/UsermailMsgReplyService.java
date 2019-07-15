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

import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.PACKET_ID_SUFFIX;

import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.common.Constants.TemailType;
import com.syswin.temail.usermail.common.ReplyCountEnum;
import com.syswin.temail.usermail.common.ResultCodeEnum;
import com.syswin.temail.usermail.common.SessionEventType;
import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import com.syswin.temail.usermail.core.util.MsgCompressor;
import com.syswin.temail.usermail.core.util.SeqIdFilter;
import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.domains.UsermailMsgReplyDO;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import com.syswin.temail.usermail.infrastructure.domain.IUsermailMsgReplyDB;
import com.syswin.temail.usermail.infrastructure.domain.IUsermailMsgDB;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
public class UsermailMsgReplyService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UsermailMsgReplyService.class);
  private final IUsermailMsgDB usermailMsgDB;
  private final IUsermailAdapter usermailAdapter;
  private final IUsermailMsgReplyDB usermailMsgReplyDB;
  private final Usermail2NotifyMqService usermail2NotifyMqService;
  private final UsermailSessionService usermailSessionService;
  private final MsgCompressor msgCompressor;
  private final UsermailMqService usermailMqService;
  private final ConvertMsgService convertMsgService;

  @Autowired
  public UsermailMsgReplyService(IUsermailMsgDB usermailMsgDB, IUsermailAdapter usermailAdapter,
      IUsermailMsgReplyDB usermailMsgReplyDB, Usermail2NotifyMqService usermail2NotifyMqService,
      UsermailSessionService usermailSessionService,
      MsgCompressor msgCompressor, UsermailMqService usermailMqService,
      ConvertMsgService convertMsgService) {
    this.usermailMsgDB = usermailMsgDB;
    this.usermailAdapter = usermailAdapter;
    this.usermailMsgReplyDB = usermailMsgReplyDB;
    this.usermail2NotifyMqService = usermail2NotifyMqService;
    this.usermailSessionService = usermailSessionService;
    this.msgCompressor = msgCompressor;
    this.usermailMqService = usermailMqService;
    this.convertMsgService = convertMsgService;
  }

  /**
   * 发送单聊回复消息
   *
   * @param cdtpHeaderDto 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param message 发送的消息
   * @param msgId 消息id
   * @param parentMsgId 父消息id
   * @param type 消息类型
   * @param attachmentSize 附件大小
   * @param owner 消息所属人
   * @return map(key包括 : msgId 、 seqId)
   */
  @Transactional
  public Map<String, Object> createMsgReply(CdtpHeaderDTO cdtpHeaderDto, String from, String to, String message,
      String msgId, String parentMsgId, int type, int attachmentSize, String owner) {
    this.msgReplyTypeValidate(parentMsgId, owner);
    String sessionid = usermailSessionService.getSessionID(from, to);
    long msgReplySeqNo = usermailAdapter.getMsgReplySeqNo(parentMsgId, owner);
    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO(usermailAdapter.getMsgReplyPkID(), parentMsgId, msgId,
        from, to, msgReplySeqNo, "", TemailStatus.STATUS_NORMAL_0, type, owner, sessionid,
        msgCompressor.zipWithDecode(message));
    usermailMsgReplyDB.insert(usermailMsgReply);

    // 更新最新回复消息id
    usermailMsgDB.updateReplyCountAndLastReplyMsgid(parentMsgId, owner, ReplyCountEnum.INCR.value(), msgId);
    LOGGER.debug("new rely created, update msgId={} lastReplyMsgid={}", parentMsgId, msgId);
    usermail2NotifyMqService
        .sendMqSaveMsgReply(cdtpHeaderDto, from, to, owner, msgId, message, msgReplySeqNo, attachmentSize,
            parentMsgId);
    Map<String, Object> result = new HashMap<>(4);
    final String msgIdKey = "msgId";
    final String seqIdKey = "seqId";
    result.put(msgIdKey, msgId);
    result.put(seqIdKey, msgReplySeqNo);
    return result;
  }

  /**
   * 撤回单聊回复消息(内部consumer处理方法)
   *
   * @param xPacketId 头信息中的xPacketId
   * @param cdtpHeader 头信息中的header
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所属人
   * @param replyMsgParentId 父消息id
   * @param msgId 消息id
   */
  @Transactional
  public void revertMsgReply(String xPacketId, String cdtpHeader, String from, String to, String owner,
      String replyMsgParentId, String msgId) {
    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
    usermailMsgReply.setMsgid(msgId);
    usermailMsgReply.setStatus(TemailStatus.STATUS_REVERT_1);
    usermailMsgReply.setOwner(owner);
    int count = usermailMsgReplyDB.updateRevertUsermailReply(usermailMsgReply);
    if (count <= 0) {
      LOGGER.warn(
          "Message Reply revert failed, xPacketId is {}, cdtpHeader is {}, from is {}, to is {}, msgId is {}, parentMsgId is {}, owner is {}",
          xPacketId, cdtpHeader, from, to, msgId, replyMsgParentId, owner);
      return;
    }
    UsermailDO usermail = usermailMsgDB.selectByMsgidAndOwner(replyMsgParentId, owner);
    if (usermail != null) {
      this.updateUsermailLastReplyId(usermail, replyMsgParentId, msgId);
      usermail2NotifyMqService
          .sendMqAfterUpdateMsgReply(xPacketId, cdtpHeader, from, to, owner, msgId, SessionEventType.EVENT_TYPE_19,
              replyMsgParentId);
    } else {
      LOGGER.warn("label-mq-revertMsgReply, parentMsgid={}, not exist.", replyMsgParentId);
    }
  }

  /**
   * 撤回单聊回复消息
   *
   * @param cdtpHeaderDto 头信息（header和xPacketId）
   * @param parentMsgReplyId 父消息id
   * @param msgId 消息id
   * @param from 发件人
   * @param to 收件人
   */
  @Transactional
  public void revertMsgReply(CdtpHeaderDTO cdtpHeaderDto, String parentMsgReplyId, String msgId, String from,
      String to) {
    this.msgReplyTypeValidate(parentMsgReplyId);
    usermailMqService.sendMqRevertReplyMsg(cdtpHeaderDto.getxPacketId(), cdtpHeaderDto.getCdtpHeader(), from, to, to,
        parentMsgReplyId, msgId);
    usermailMqService
        .sendMqRevertReplyMsg(cdtpHeaderDto.getxPacketId() + PACKET_ID_SUFFIX, cdtpHeaderDto.getCdtpHeader(), from, to,
            from, parentMsgReplyId, msgId);
  }

  /**
   * 删除单聊回复消息
   *
   * @param cdtpHeaderDto 头信息（header和xPacketId）
   * @param parentMsgReplyId 父消息id
   * @param msgIds 消息id列表
   * @param from 发件人
   * @param to 收件人
   */
  @Transactional
  public void removeMsgReplys(CdtpHeaderDTO cdtpHeaderDto, String parentMsgReplyId, List<String> msgIds, String from,
      String to) {
    if (CollectionUtils.isEmpty(msgIds)) {
      throw new IllegalGmArgsException(ResultCodeEnum.ERROR_REQUEST_PARAM);
    }
    UsermailDO usermail = this.msgReplyTypeValidate(parentMsgReplyId, from);
    LOGGER.info("Label-delete-usermail-reply: delete reply messages，from = {},to = {},msgIds = {}", from, to, msgIds);
    int count = usermailMsgReplyDB.deleteMsgReplysByMsgIds(from, msgIds);
    String lastReplyMsgId = usermail.getLastReplyMsgId();
    // 如果删除的消息中包含最新的回复消息，这需要将最新回复消息回滚到上一条
    if (!StringUtils.isEmpty(lastReplyMsgId) && msgIds.contains(lastReplyMsgId)) {
      UsermailMsgReplyDO lastUsermailMsgReply = usermailMsgReplyDB
          .selectLastUsermailReply(parentMsgReplyId, usermail.getOwner(), TemailStatus.STATUS_NORMAL_0);
      if (lastUsermailMsgReply != null) {
        lastReplyMsgId = lastUsermailMsgReply.getMsgid();
      } else {
        // 删除了所有消息的场景
        lastReplyMsgId = "";
      }
    }
    usermailMsgDB.updateReplyCountAndLastReplyMsgid(parentMsgReplyId, usermail.getOwner(), -count, lastReplyMsgId);
    usermail2NotifyMqService
        .sendMqAfterRemoveMsgReply(cdtpHeaderDto, from, to, from, msgIds, SessionEventType.EVENT_TYPE_20,
            parentMsgReplyId);
  }


  /**
   * 拉取单聊回复消息
   *
   * @param parentMsgid 父消息id
   * @param pageSize 分页大小
   * @param seqId 回复消息序列号、上次消息拉取seqId
   * @param signal 向前向后拉取标识，before向前拉取，after向后拉取，默认before
   * @param owner 消息所属人
   * @param filterSeqIds 过滤断层seqId
   * @return 拉取到的单聊回复消息列表
   */

  public List<UsermailMsgReplyDO> getMsgReplys(String parentMsgid, int pageSize, long seqId, String signal,
      String owner, String filterSeqIds) {
    msgReplyTypeValidate(parentMsgid, owner);
    QueryMsgReplyDTO dto = new QueryMsgReplyDTO();
    dto.setFromSeqNo(seqId);
    dto.setPageSize(pageSize);
    dto.setParentMsgid(parentMsgid);
    dto.setSignal(signal);
    dto.setOwner(owner);
    List<UsermailMsgReplyDO> data = this.convertMsgService.convertReplyMsg(usermailMsgReplyDB.listMsgReplys(dto));
    List<UsermailMsgReplyDO> dataFilter = new ArrayList<>();
    if (StringUtils.isNotEmpty(filterSeqIds) && !CollectionUtils.isEmpty(data)) {
      boolean isAfter = "after".equals(signal);
      long beginSeqId = data.get(0).getSeqNo();
      long endSeqId = data.get(data.size() - 1).getSeqNo();
      SeqIdFilter filter = new SeqIdFilter(filterSeqIds, isAfter, beginSeqId, endSeqId);
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

  /**
   * 阅后即焚回复消息
   *
   * @param xPacketId 头信息中的xPacketId
   * @param cdtpHeader 头信息中的header
   * @param from 收件人
   * @param to 发件人
   * @param owner 消息所属人
   * @param msgId 消息id
   * @param replyMsgParentId 父消息id
   */
  @Transactional
  public void destroyAfterRead(String xPacketId, String cdtpHeader, String from, String to, String owner, String msgId,
      String replyMsgParentId) {
    int count = usermailMsgReplyDB.updateDestroyAfterRead(owner, msgId, TemailStatus.STATUS_DESTROY_AFTER_READ_2);
    if (count <= 0) {
      LOGGER.warn(
          "Message updateDestroyAfterRead failed, xPacketId is {}, cdtpHeader is {}, from is {}, to is {}, msgId is {}, owner is {}",
          xPacketId, cdtpHeader, from, to, msgId, owner);
      return;
    }
    UsermailDO usermail = usermailMsgDB.selectByMsgidAndOwner(replyMsgParentId, owner);
    if (usermail != null) {
      updateUsermailLastReplyId(usermail, replyMsgParentId, msgId);
      usermail2NotifyMqService
          .sendMqUpdateMsg(xPacketId, cdtpHeader, to, from, owner, msgId, SessionEventType.EVENT_TYPE_26);
    } else {
      LOGGER.warn("label-mq-destroy-after-read: parentMsgId={},not-exist", replyMsgParentId);
    }
  }

  /**
   * 阅后即焚回复消息
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param msgId 消息id
   */
  @Transactional
  public void destroyAfterRead(CdtpHeaderDTO headerInfo, String from, String to, String msgId) {
    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
    usermailMsgReply.setOwner(from);
    usermailMsgReply.setMsgid(msgId);
    UsermailMsgReplyDO msgReply = usermailMsgReplyDB.selectMsgReplyByCondition(usermailMsgReply);
    if (msgReply != null && msgReply.getType() == TemailType.TYPE_DESTROY_AFTER_READ_1) {
      String parentMsgId = msgReply.getParentMsgid();
      usermailMqService
          .sendMqReplyMsgDestroyAfterRead(headerInfo.getxPacketId(), headerInfo.getCdtpHeader(), from, to, to, msgId,
              parentMsgId);
      usermailMqService
          .sendMqReplyMsgDestroyAfterRead(headerInfo.getxPacketId(), headerInfo.getCdtpHeader() + PACKET_ID_SUFFIX,
              from, to, from, msgId,
              parentMsgId);
    } else {
      LOGGER.warn("msgReply-updateDestroyAfterRead-illegal-msgId={}", msgId);
    }

  }


  /**
   * 校验源消息类型是否为合法
   *
   * @param parentMsgId 父消息id
   * @return 单聊对象列表
   */
  private List<UsermailDO> msgReplyTypeValidate(String parentMsgId) {
    List<UsermailDO> usermails = usermailMsgDB.listUsermailsByMsgid(parentMsgId);
    if (CollectionUtils.isEmpty(usermails)) {
      LOGGER.warn("parentMsgId status is error:{}", parentMsgId);
      throw new IllegalGmArgsException(ResultCodeEnum.ERROR_ILLEGAL_PARENT_MSG_ID);
    }
    return usermails;
  }

  /**
   * 校验源消息类型是否为合法
   *
   * @param parentMsgId 父消息id
   * @param owner 消息所属人
   * @return 单聊对象信息
   */
  private UsermailDO msgReplyTypeValidate(String parentMsgId, String owner) {

    UsermailDO usermailByMsgid = usermailMsgDB.selectByMsgidAndOwner(parentMsgId, owner);
    if (usermailByMsgid == null || (usermailByMsgid.getStatus() != TemailStatus.STATUS_NORMAL_0
        && usermailByMsgid.getStatus() != TemailStatus.STATUS_TRASH_4)) {
      LOGGER.warn("parentMsgId is {}", parentMsgId);
      throw new IllegalGmArgsException(ResultCodeEnum.ERROR_ILLEGAL_PARENT_MSG_ID);
    }
    return usermailByMsgid;
  }

  /**
   * 更新最新回复消息
   * @param usermail 源消息
   * @param parentMsgId 源消息ID
   * @param msgId 最新消息ID
   */
  private void updateUsermailLastReplyId(UsermailDO usermail, String parentMsgId, String msgId) {
    String lastReplyMsgId = usermail.getLastReplyMsgId();
    // 如果撤回或者阅后即焚的消息是最新的回复消息需要将最新回复消息回滚到上一条
    if (!StringUtils.isEmpty(lastReplyMsgId) && msgId.equals(lastReplyMsgId)) {
      UsermailMsgReplyDO lastUsermailMsgReply = usermailMsgReplyDB
          .selectLastUsermailReply(parentMsgId, usermail.getOwner(), TemailStatus.STATUS_NORMAL_0);
      if (lastUsermailMsgReply != null) {
        lastReplyMsgId = lastUsermailMsgReply.getMsgid();
      } else {
        // 删除了所有消息的场景
        lastReplyMsgId = "";
      }
    }
    usermailMsgDB
        .updateReplyCountAndLastReplyMsgid(parentMsgId, usermail.getOwner(), ReplyCountEnum.DECR.value(),
            lastReplyMsgId);
  }

}
