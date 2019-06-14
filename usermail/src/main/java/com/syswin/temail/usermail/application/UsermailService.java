package com.syswin.temail.usermail.application;


import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.PACKET_ID_SUFFIX;
import static com.syswin.temail.usermail.common.ResultCodeEnum.ERROR_REQUEST_PARAM;

import com.google.gson.Gson;
import com.syswin.temail.transactional.TemailShardingTransactional;
import com.syswin.temail.usermail.common.Constants.TemailArchiveStatus;
import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.common.Constants.TemailType;
import com.syswin.temail.usermail.common.Constants.UsermailAgentEventType;
import com.syswin.temail.usermail.common.SessionEventType;
import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.core.dto.Meta;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import com.syswin.temail.usermail.core.util.MsgCompressor;
import com.syswin.temail.usermail.core.util.SeqIdFilter;
import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.domains.UsermailBox;
import com.syswin.temail.usermail.dto.CreateUsermailDTO;
import com.syswin.temail.usermail.dto.DeleteMailBoxQueryDTO;
import com.syswin.temail.usermail.dto.MailboxDTO;
import com.syswin.temail.usermail.dto.QueryTrashDTO;
import com.syswin.temail.usermail.dto.RevertMailDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import com.syswin.temail.usermail.dto.UmQueryDTO;
import com.syswin.temail.usermail.infrastructure.domain.UsermailBoxRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailMsgReplyRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailRepo;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class UsermailService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UsermailService.class);
  private final UsermailRepo usermailRepo;
  private final UsermailBoxRepo usermailBoxRepo;
  private final UsermailMsgReplyRepo usermailMsgReplyRepo;
  private final IUsermailAdapter usermailAdapter;
  private final UsermailSessionService usermailSessionService;
  private final Usermail2NotfyMqService usermail2NotfyMqService;
  private final UsermailMqService usermailMqService;
  private final MsgCompressor msgCompressor;
  private final ConvertMsgService convertMsgService;


  @Autowired
  public UsermailService(UsermailRepo usermailRepo, UsermailBoxRepo usermailBoxRepo,
      UsermailMsgReplyRepo usermailMsgReplyRepo,
      IUsermailAdapter usermailAdapter,
      UsermailSessionService usermailSessionService, Usermail2NotfyMqService usermail2NotfyMqService,
      UsermailMqService usermailMqService,
      MsgCompressor msgCompressor,
      ConvertMsgService convertMsgService) {
    this.usermailRepo = usermailRepo;
    this.usermailBoxRepo = usermailBoxRepo;
    this.usermailMsgReplyRepo = usermailMsgReplyRepo;
    this.usermailAdapter = usermailAdapter;
    this.usermailSessionService = usermailSessionService;
    this.usermail2NotfyMqService = usermail2NotfyMqService;
    this.usermailMqService = usermailMqService;
    this.msgCompressor = msgCompressor;
    this.convertMsgService = convertMsgService;
  }

  /**
   * 保存单聊会话信息
   *
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所属人
   * @return sessionId
   */
  public String saveUsermailBoxInfo(String from, String to, String owner) {
    String sessionId = usermailSessionService.getSessionID(from, to);
    // 保证mail2和owner是相反的，逐渐去掉mail1字段
    String target = owner.equals(from) ? to : from;
    UsermailBox usermailBox = usermailBoxRepo.selectUsermailBox(owner, target);
    if (usermailBox == null) {
      UsermailBox box = new UsermailBox(usermailAdapter.getPkID(), sessionId, target, owner);
      usermailBoxRepo.saveUsermailBox(box);
    }
    return sessionId;
  }

  /**
   * 发送单聊消息
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param usermail 创建temail时消息信息
   * @param owner 消息所属人
   * @param other 收件人
   */
  @TemailShardingTransactional(shardingField = "#owner")
  public Map sendMail(CdtpHeaderDTO headerInfo, CreateUsermailDTO usermail, String owner, String other) {
    String from = usermail.getFrom();
    String to = usermail.getTo();
    String msgid = usermail.getMsgId();
    String author = usermail.getAuthor();
    List<String> filter = usermail.getFilter();
    String filterStr = null;
    if (filter != null && !filter.isEmpty()) {
      filterStr = String.join(",", filter);
    }
    int attachmentSize = usermail.getAttachmentSize();
    long seqNo = usermailAdapter.getMsgSeqNo(from, to, owner);
    String sessionid = saveUsermailBoxInfo(from, to, owner);
    long pkid = usermailAdapter.getPkID();
    Usermail mail = new Usermail(pkid, usermail.getMsgId(), sessionid,
        from, to, TemailStatus.STATUS_NORMAL_0, usermail.getType(), owner, "",
        seqNo, msgCompressor.zipWithDecode(usermail.getMsgData()), author, filterStr);
    Meta meta = usermail.getMeta();
    if (meta != null) {
      BeanUtils.copyProperties(meta, mail);
    }
    usermailRepo.saveUsermail(mail);
    int eventType = 0;

    switch (usermail.getType()) {
      case TemailType.TYPE_NORMAL_0:
        eventType = SessionEventType.EVENT_TYPE_0;
        break;
      case TemailType.TYPE_DESTORY_AFTER_READ_1:
        eventType = SessionEventType.EVENT_TYPE_17;
        break;
      default:
        eventType = SessionEventType.EVENT_TYPE_51;
        break;
    }
    usermail2NotfyMqService
        .sendMqMsgSaveMail(headerInfo, from, to, owner, msgid, usermail.getMsgData(), seqNo, eventType, attachmentSize,
            author, filter);
    usermailAdapter.setLastMsgId(owner, other, msgid);
    Map<String, Object> result = new HashMap<>(4);
    result.put("msgId", msgid);
    result.put("seqId", mail.getSeqNo());
    return result;
  }

  /**
   * 同步单聊会话消息
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param fromSeqNo 消息序列号
   * @param pageSize 分页大小
   * @param filterSeqIds 过滤断层seqId
   * @param signal 向前向后拉取标识，before向前拉取，after向后拉取，默认before
   * @return 单聊会话消息
   */
  @TemailShardingTransactional(shardingField = "#from")
  public List<Usermail> getMails(CdtpHeaderDTO headerInfo, String from, String to, long fromSeqNo,
      int pageSize, String filterSeqIds, String signal) {
    UmQueryDTO umQueryDto = new UmQueryDTO();
    umQueryDto.setFromSeqNo(fromSeqNo);
    umQueryDto.setSignal(signal);
    String sessionid = usermailSessionService.getSessionID(from, to);
    umQueryDto.setSessionid(sessionid);
    umQueryDto.setPageSize(pageSize);
    umQueryDto.setOwner(from);
    List<Usermail> result = convertMsgService.convertMsg(usermailRepo.getUsermail(umQueryDto));
    List<Usermail> resultFilter = new ArrayList<>();
    if (StringUtils.isNotEmpty(filterSeqIds) && !CollectionUtils.isEmpty(result)) {
      boolean isAfter = "after".equals(signal);
      SeqIdFilter filter = new SeqIdFilter(filterSeqIds, isAfter);
      for (int i = 0; i < result.size(); i++) {
        if (filter.filter(result.get(i).getSeqNo())) {
          resultFilter.add(result.get(i));
        }
      }
    } else {
      resultFilter = result;
    }
    return resultFilter;
  }

  /**
   * 撤回单聊消息
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param msgid 消息id
   */
  @TemailShardingTransactional(shardingField = "#from")
  public void revert(CdtpHeaderDTO headerInfo, String from, String to, String msgid) {
    usermailMqService.sendMqRevertMsg(headerInfo.getxPacketId(), headerInfo.getCdtpHeader(), from, to, to, msgid);
    usermailMqService
        .sendMqRevertMsg(headerInfo.getxPacketId() + PACKET_ID_SUFFIX, headerInfo.getCdtpHeader(), from, to, from,
            msgid);
  }

  /**
   * 撤回单聊消息
   *
   * @param xPacketId 头信息中的xPacketId
   * @param cdtpHeader 头信息中的header
   * @param from 发件人
   * @param to 收件人
   * @param owner 消息所属人
   * @param msgid 消息id
   */
  @TemailShardingTransactional(shardingField = "#owner")
  public void revertMqHandler(String xPacketId, String cdtpHeader, String from, String to, String owner, String msgid) {
    int count = usermailRepo
        .revertUsermail(new RevertMailDTO(owner, msgid, TemailStatus.STATUS_NORMAL_0, TemailStatus.STATUS_REVERT_1));
    // 判断是否撤回成功，防止通知重复发送
    if (count > 0) {
      usermail2NotfyMqService
          .sendMqUpdateMsg(xPacketId, cdtpHeader, from, to, owner, msgid, SessionEventType.EVENT_TYPE_2);
    } else {
      LOGGER.warn(
          "Message revert failed, xPacketId is {}, cdtpHeader is {}, from is {}, to is {}, msgId is {}, owner is {}",
          xPacketId, cdtpHeader, from, to, msgid, owner);
    }
  }

  /**
   * 同步会话列表
   *
   * @param from 发件人
   * @param archiveStatus 归档状态（ 0代表还原归档  1代表归档）
   * @param usermailBoxes 会话
   * @return 收件箱列表
   */
  @TemailShardingTransactional(shardingField = "#from")
  public List<MailboxDTO> mailboxes(String from, int archiveStatus,
      Map<String, String> usermailBoxes) {
    Map<String, String> localMailBoxes = CollectionUtils.isEmpty(usermailBoxes) ? new HashMap<>(0) : usermailBoxes;
    List<UsermailBox> dbBoxes = usermailBoxRepo.getUsermailBoxByOwner(from, archiveStatus);
    List<MailboxDTO> mailBoxes = new ArrayList<>(dbBoxes.size());
    List<Usermail> lastUsermail;
    for (UsermailBox dbBox : dbBoxes) {
      String to = dbBox.getMail2();
      if (Objects.equals(usermailAdapter.getLastMsgId(from, to), localMailBoxes.get(to))) {
        // 最新的msgId相同，不做处理
        continue;
      }
      MailboxDTO mailBox = new MailboxDTO();
      UmQueryDTO umQuery = new UmQueryDTO(dbBox.getSessionid(), from);
      lastUsermail = convertMsgService.convertMsg(usermailRepo.getLastUsermail(umQuery));
      if (!CollectionUtils.isEmpty(lastUsermail)) {
        mailBox.setLastMsg(lastUsermail.get(0));
      }
      mailBox.setTo(dbBox.getMail2());
      mailBox.setArchiveStatus(dbBox.getArchiveStatus());
      mailBoxes.add(mailBox);
    }
    return mailBoxes;
  }

  /**
   * 删除单聊消息
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param msgIds 待删除的消息id列表
   */
  @TemailShardingTransactional(shardingField = "#from")
  public void removeMsg(CdtpHeaderDTO headerInfo, String from, String to, List<String> msgIds) {
    // msg 內容更新为空串
    LOGGER.info("Label-delete-usermail-msg: delete msg by msgIds,from is {},to is {},ids is {}", from, to, msgIds);
    usermailRepo.removeMsg(msgIds, from);
    LOGGER.info("Label-delete-usermail-msg: delete reply msg by parentMsgId, owner is {}, parentMsgId is {}", from,
        msgIds);
    usermailMsgReplyRepo.deleteMsgByParentIdAndOwner(from, msgIds);
    usermail2NotfyMqService
        .sendMqAfterUpdateStatus(headerInfo, from, to, new Gson().toJson(msgIds), SessionEventType.EVENT_TYPE_4);

    UmQueryDTO umQueryDto = new UmQueryDTO();
    umQueryDto.setOwner(from);
    umQueryDto.setSessionid(usermailSessionService.getSessionID(from, to));
    List<Usermail> usermails = usermailRepo.getLastUsermail(umQueryDto);
    if (CollectionUtils.isEmpty(usermails)) {
      usermailAdapter.deleteLastMsgId(from, to);
    } else {
      String lastMsgId = usermailAdapter.getLastMsgId(from, to);
      String newLastMsgId = usermails.get(0).getMsgid();
      if (!newLastMsgId.equals(lastMsgId)) {
        usermailAdapter.setLastMsgId(from, to, newLastMsgId);
      }
    }
  }

  /**
   * 单聊消息阅后即焚
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param msgId 消息id
   */
  @TemailShardingTransactional(shardingField = "#from")
  public void destroyAfterRead(CdtpHeaderDTO headerInfo, String from, String to, String msgId) {
    usermailMqService.sendMqDestroyMsg(headerInfo.getxPacketId(), headerInfo.getCdtpHeader(), from, to, to, msgId);
    usermailMqService
        .sendMqDestroyMsg(headerInfo.getxPacketId() + PACKET_ID_SUFFIX, headerInfo.getCdtpHeader(), from, to, from,
            msgId);
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
  @TemailShardingTransactional(shardingField = "#owner")
  public void destroyAfterRead(String xPacketId, String cdtpHeader, String from, String to, String owner,
      String msgId) {
    Usermail usermail = usermailRepo.getUsermailByMsgid(msgId, owner);
    //添加消息状态判断，防止通知重发
    if (usermail != null && usermail.getType() == TemailType.TYPE_DESTORY_AFTER_READ_1
        && usermail.getStatus() == TemailStatus.STATUS_NORMAL_0) {
      usermailRepo.destoryAfterRead(owner, msgId, TemailStatus.STATUS_DESTORY_AFTER_READ_2);
      usermail2NotfyMqService
          .sendMqUpdateMsg(xPacketId, cdtpHeader, to, from, owner, msgId, SessionEventType.EVENT_TYPE_3);
    } else {
      LOGGER.warn("destroyAfterRead method illegal param, from is {}, msgId is {}, usermail is {}", from, msgId,
          usermail);
    }
  }

  /**
   * 删除会话
   *
   * @return 删除成功的标志
   */
  @TemailShardingTransactional(shardingField = "#queryDto.from")
  public boolean deleteSession(CdtpHeaderDTO cdtpHeaderDto, DeleteMailBoxQueryDTO queryDto) {
    usermailBoxRepo.deleteByOwnerAndTo(queryDto.getFrom(), queryDto.getTo());
    LOGGER.info("Label-delete-usermail-session: delete session, params is {}", queryDto);
    if (queryDto.isDeleteAllMsg()) {
      String sessionId = usermailSessionService.getSessionID(queryDto.getTo(), queryDto.getFrom());
      usermailRepo
          .batchDeleteBySessionId(sessionId, queryDto.getFrom());
      usermailMsgReplyRepo.batchDeleteBySessionId(sessionId, queryDto.getFrom());
    }
    usermail2NotfyMqService
        .sendMqAfterDeleteSession(cdtpHeaderDto, queryDto.getFrom(), queryDto.getTo(), queryDto.isDeleteAllMsg(),
            SessionEventType.EVENT_TYPE_4);
    usermailAdapter.deleteLastMsgId(queryDto.getFrom(), queryDto.getTo());
    return true;
  }

  /**
   * 删除群聊会话
   *
   * @param groupTemail 群聊地址
   * @param owner 消息所属人
   * @return 删除成功的标志
   */
  @TemailShardingTransactional(shardingField = "#owner")
  public boolean deleteGroupChatSession(String groupTemail, String owner) {
    usermailBoxRepo.deleteByOwnerAndTo(owner, groupTemail);
    LOGGER
        .info("Label-delete-GroupChat-session: delete session, params is owner:{}, groupTemail:{}", owner, groupTemail);
    String sessionId = usermailSessionService.getSessionID(groupTemail, owner);
    usermailRepo.batchDeleteBySessionId(sessionId, owner);
    return true;
  }

  /**
   * 批量查询单聊信息
   *
   * @param cdtpHeaderDto 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param msgIds 消息id列表
   * @return 单聊对象列表
   */
  @TemailShardingTransactional(shardingField = "#from")
  public List<Usermail> batchQueryMsgs(CdtpHeaderDTO cdtpHeaderDto, String from, String to, List<String> msgIds) {
    List<Usermail> usermailList = usermailRepo.getUsermailByFromToMsgIds(from, msgIds);
    return convertMsgService.convertMsg(usermailList);
  }

  /**
   * 批量查询单聊回复消息
   *
   * @param cdtpHeaderDto 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param msgIds 消息id列表
   * @return 单聊回复消息列表
   */
  @TemailShardingTransactional(shardingField = "#from")
  public List<Usermail> batchQueryMsgsReplyCount(CdtpHeaderDTO cdtpHeaderDto, String from, String to,
      List<String> msgIds) {
    List<Usermail> usermailList = usermailRepo.getUsermailByFromToMsgIds(from, msgIds);
    for (int i = 0; i < usermailList.size(); i++) {
      usermailList.get(i).setMessage(null);
      usermailList.get(i).setZipMsg(null);
    }
    return usermailList;
  }

  /**
   * 移送消息到废纸篓
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param msgIds 待移送到废纸篓的消息id列表
   */
  @TemailShardingTransactional(shardingField = "#from")
  public void moveMsgToTrash(CdtpHeaderDTO headerInfo, String from, String to, List<String> msgIds) {
    usermailRepo.updateStatusByMsgIds(msgIds, from, TemailStatus.STATUS_TRASH_4);
    usermailMsgReplyRepo.batchUpdateByParentMsgIds(from, msgIds, TemailStatus.STATUS_TRASH_4);
    usermail2NotfyMqService.sendMqMoveTrashNotify(headerInfo, from, to, msgIds, SessionEventType.EVENT_TYPE_35);
  }

  /**
   * 还原废纸篓消息
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param temail 执行操作的temail
   * @param trashMails 待还原的废纸篓消息列表
   */
  @TemailShardingTransactional(shardingField = "#temail")
  public void revertMsgToTrash(CdtpHeaderDTO headerInfo, String temail, List<TrashMailDTO> trashMails) {
    List<String> msgIds = new ArrayList<>(trashMails.size());
    for (TrashMailDTO dto : trashMails) {
      msgIds.add(dto.getMsgId());
    }
    usermailRepo.updateStatusByTemail(trashMails, temail, TemailStatus.STATUS_NORMAL_0);
    usermailMsgReplyRepo.batchUpdateByParentMsgIds(temail, msgIds, TemailStatus.STATUS_NORMAL_0);
    usermail2NotfyMqService.sendMqTrashMsgOpratorNotify(headerInfo, temail, trashMails, SessionEventType.EVENT_TYPE_36);
  }

  /**
   * 删除废纸篓消息
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param temail 被操作的temail
   * @param trashMails 待删除的废纸篓消息列表
   */
  @TemailShardingTransactional(shardingField = "#temail")
  public void removeMsgFromTrash(CdtpHeaderDTO headerInfo, String temail, List<TrashMailDTO> trashMails) {
    usermailMqService.sendMqRemoveTrash(temail, trashMails, UsermailAgentEventType.TRASH_REMOVE_0);
    LOGGER
        .info("Label-delete-usermail-trash: Remove msg from trash, params is temail:{},msginfo:{}", temail, trashMails);
    usermail2NotfyMqService.sendMqTrashMsgOpratorNotify(headerInfo, temail, trashMails, SessionEventType.EVENT_TYPE_37);
  }

  /**
   * 删除废纸篓消息
   *
   * @param temail 被操作的temail
   * @param trashMails 待删除的废纸篓消息
   */
  @TemailShardingTransactional(shardingField = "#temail")
  public void removeMsgFromTrash(String temail, List<TrashMailDTO> trashMails) {
    List<String> msgIds = new ArrayList<>(trashMails.size());
    for (TrashMailDTO dto : trashMails) {
      msgIds.add(dto.getMsgId());
    }
    usermailRepo.removeMsgByStatus(trashMails, temail, TemailStatus.STATUS_TRASH_4);
    LOGGER
        .info("Label-delete-usermail-trash: Mq consumer remove msg from trash, params is temail:{},msginfo:{}", temail,
            trashMails);
    usermailMsgReplyRepo.deleteMsgByParentIdAndOwner(temail, msgIds);
  }

  /**
   * 清空废纸篓消息
   *
   * @param temail 被操作的temail
   */
  @TemailShardingTransactional(shardingField = "#temail")
  public void clearMsgFromTrash(String temail) {
    usermailRepo.removeMsgByStatus(null, temail, TemailStatus.STATUS_TRASH_4);
    LOGGER.info("Label-delete-usermail-trash: Mq consumer clear trash, params is temail:{}", temail);
    usermailMsgReplyRepo.batchDeleteByStatus(temail, TemailStatus.STATUS_TRASH_4);
  }

  /**
   * 同步废纸篓消息列表
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param temail 操作的temail
   * @param timestamp 时间戳
   * @param pageSize 分页大小
   * @param signal 向前向后拉取标识，before向前拉取，after向后拉取，默认before
   * @return 废纸篓消息列表
   */
  @TemailShardingTransactional(shardingField = "#temail")
  public List<Usermail> getMsgFromTrash(CdtpHeaderDTO headerInfo, String temail, long timestamp, int pageSize,
      String signal) {
    QueryTrashDTO queryDto = new QueryTrashDTO();
    queryDto.setOwner(temail);
    queryDto.setSignal(signal);
    queryDto.setPageSize(pageSize);
    queryDto.setUpdateTime(new Timestamp(timestamp));
    queryDto.setStatus(TemailStatus.STATUS_TRASH_4);
    List<Usermail> result = usermailRepo.getUsermailByStatus(queryDto);
    return convertMsgService.convertMsg(result);
  }

  /**
   * 单聊会话归档
   *
   * @param headerInfo 头信息（header和xPacketId）
   * @param from 发件人
   * @param to 收件人
   * @param archiveStatus 归档状态（ 0代表还原归档 1代表归档）
   */
  @TemailShardingTransactional(shardingField = "#from")
  public void updateUsermailBoxArchiveStatus(CdtpHeaderDTO headerInfo, String from, String to, int archiveStatus) {
    LOGGER.info("update usermail archiveStatus , from={},to={},archiveStatus={}", from, to, archiveStatus);
    if ((archiveStatus != TemailArchiveStatus.STATUS_NORMAL_0
        && archiveStatus != TemailArchiveStatus.STATUS_ARCHIVE_1)) {
      throw new IllegalGmArgsException(ERROR_REQUEST_PARAM);
    }
    usermailBoxRepo.updateArchiveStatus(from, to, archiveStatus);
    if (archiveStatus == TemailArchiveStatus.STATUS_NORMAL_0) {
      usermail2NotfyMqService.sendMqAfterUpdateArchiveStatus(headerInfo, from, to, SessionEventType.EVENT_TYPE_34);
    } else if (archiveStatus == TemailArchiveStatus.STATUS_ARCHIVE_1) {
      usermail2NotfyMqService.sendMqAfterUpdateArchiveStatus(headerInfo, from, to, SessionEventType.EVENT_TYPE_33);
    }
  }

}
