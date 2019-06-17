package com.syswin.temail.usermail.interfaces;

import static com.syswin.temail.usermail.common.ParamsKey.HttpHeaderKey.CDTP_HEADER;
import static com.syswin.temail.usermail.common.ParamsKey.HttpHeaderKey.X_PACKET_ID;
import static com.syswin.temail.usermail.common.ResultCodeEnum.ERROR_REQUEST_PARAM;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syswin.temail.usermail.application.UmBlacklistProxy;
import com.syswin.temail.usermail.application.UsermailService;
import com.syswin.temail.usermail.common.Constants.TemailStoreType;
import com.syswin.temail.usermail.common.ResultCodeEnum;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.core.dto.ResultDTO;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.dto.CreateUsermailDTO;
import com.syswin.temail.usermail.dto.DeleteMailBoxQueryDTO;
import com.syswin.temail.usermail.dto.MailboxDTO;
import com.syswin.temail.usermail.dto.MoveTrashMailDTO;
import com.syswin.temail.usermail.dto.TrashMailsDTO;
import com.syswin.temail.usermail.dto.UmDeleteMailDTO;
import com.syswin.temail.usermail.dto.UpdateArchiveDTO;
import com.syswin.temail.usermail.dto.UsermailDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsermailAgentController {

  private static final Logger LOGGER = LoggerFactory.getLogger(UsermailAgentController.class);
  private final UsermailService usermailService;
  private final UmBlacklistProxy umBlacklistProxy;

  @Autowired
  public UsermailAgentController(UsermailService usermailService,
      UmBlacklistProxy umBlacklistProxy) {
    this.usermailService = usermailService;
    this.umBlacklistProxy = umBlacklistProxy;
  }

  /**
   * from向to发送单聊消息
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param usermail 请求参数，包含发送者{@link CreateUsermailDTO#getFrom()},接收者{@link CreateUsermailDTO#getTo()}等。
   * @return 返回ResultDTO对象，返回内容包括该消息的msgid与序列号seqId。
   * @See ResultDTO
   */
  @ApiOperation(value = "发送消息(0x 0001)", notes = "[from]发送消息给[to]")
  @PostMapping(value = "/usermail")
  public ResultDTO createUsermail(
      HttpServletRequest request,
      @Valid @RequestBody CreateUsermailDTO usermail) {
    umBlacklistProxy.checkInBlacklist(usermail.getFrom(), usermail.getTo());
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    int storeType = usermail.getStoreType();
    String owner;
    String other;
    if (storeType == TemailStoreType.STORE_TYPE_TO_1) {
      // owner消息所属人 store_type_to_1表示存收件人收件箱，所以owner为to即为收件人
      owner = usermail.getTo();
      other = usermail.getFrom();
    } else if (storeType == TemailStoreType.STORE_TYPE_FROM_2) {
      owner = usermail.getFrom();
      other = usermail.getTo();
    } else {
      LOGGER.warn("storeType is error:usermail:{},storeType={}", usermail, storeType);
      throw new IllegalGmArgsException(ResultCodeEnum.ERROR_ILLEGAL_STORE_TYPE);
    }
    Map result = usermailService.sendMail(cdtpHeaderDto, usermail, owner, other);
    resultDto.setData(result);
    return resultDto;
  }

  /**
   * 发送者拉取指定单聊会话消息，该消息存储在发送者收件箱。
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param from 发送者
   * @param to 接收者
   * @param pageSize 会话消息列表每页最大数量。
   * @param seqId 指定会话消息从seqId开始进行拉取。
   * @param signal 会话消息从seqId开始向前或者向后拉取，如果signal等于before,则小于seqId拉取，如果signal等于after,则大于seqId拉取。
   * @param filterSeqIds 指定seqId的消息会被返回，默认为空，不做过滤。
   * @return 返回ResultDTO对象，包含会话消息列表。
   * @See ResultDTO
   */
  @ApiOperation(value = "同步单聊会话消息(0x 0003)", notes = "接收者拉取邮件及附件相关信息")
  @GetMapping(value = "/usermail")
  public ResultDTO getMails(
      HttpServletRequest request,
      @ApiParam(value = "发送者", required = true) @RequestParam(value = "from", defaultValue = "") String from,
      @ApiParam(value = "接收者", required = true) @RequestParam(value = "to", defaultValue = "") String to,
      @ApiParam(value = "分页`大小", required = true) @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
      @ApiParam(value = "上次消息拉取SeqNo", required = true) @RequestParam(value = "seqId", defaultValue = "0") long seqId,
      @ApiParam(value = "向前向后拉取标识", required = true, defaultValue = "before") @RequestParam(value = "signal",
          defaultValue = "before") String signal,
      @ApiParam(value = "过滤的seqId") @RequestParam(value = "filterSeqIds", required = false, defaultValue = "") String filterSeqIds) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    List<UsermailDO> temailList = usermailService
        .getMails(cdtpHeaderDto, from, to, seqId, pageSize, filterSeqIds, signal);

    resultDto.setData(temailList);
    return resultDto;
  }

  /**
   * 撤回已发送消息
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param usermail 根据{@link UsermailDTO#getFrom()}，{@link UsermailDTO#getFrom()}，{@link UsermailDTO#getMsgId()}撤回该消息。
   * @return 返回ResultDTO对象
   * @See ResultDTO
   */
  @ApiOperation(value = "撤回消息(0x 0005)", notes = "撤回消息")
  @PutMapping(value = "/revert")
  public ResultDTO revert(
      HttpServletRequest request,
      @RequestBody @Valid UsermailDTO usermail) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService
        .revert(cdtpHeaderDto, usermail.getFrom(), usermail.getTo(), usermail.getMsgId());
    return resultDto;
  }

  /**
   * 增量拉取发送者会话列表。可根据会话的归档状态进行拉取，也可通过本地会话列表参数进行增量拉取。
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param from 发送者
   * @param archiveStatus 会话归档状态，0 正常 1 已归档 -1 全部（默认）。
   * @param usermailBoxes 本地会话列表
   * @return 返回ResultDTO对象，返回内容包含会话列表{@link MailboxDTO}
   * @See ResultDTO
   */
  @ApiOperation(value = "同步会话列表(0x 0002)", notes = "同步[from]会话列表")
  @GetMapping(value = "/usermail/mailboxes")
  public ResultDTO mailboxes(
      HttpServletRequest request,
      @ApiParam(value = "发送者mail", required = true) @RequestParam(value = "from", defaultValue = "") String from,
      @ApiParam(value = "归档状态 0 正常 1 已归档 -1 全部（默认）") @RequestParam(value = "archiveStatus", defaultValue = "-1") int archiveStatus,
      @ApiParam(value = "拉取列表") @RequestParam(value = "localMailboxes", defaultValue = "") String usermailBoxes) {
    ResultDTO resultDto = new ResultDTO();
    Map<String, String> mailboxMap;
    Gson gson = new GsonBuilder().create();
    if (StringUtils.isEmpty(usermailBoxes)) {
      mailboxMap = null;
    } else {
      mailboxMap = gson.fromJson(usermailBoxes, Map.class);
    }
    List<MailboxDTO> list = usermailService.mailboxes(from, archiveStatus, mailboxMap);
    resultDto.setData(list);
    return resultDto;
  }

  /**
   * 通过发送者{@link UmDeleteMailDTO#getFrom()}，接收者{@link UmDeleteMailDTO#getTo()}，消息id{@link
   * UmDeleteMailDTO#getMsgIds()}批量删除单聊消息。
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param usermail 请求参数
   * @return 返回ResultDTO对象
   * @See ResultDTO
   */
  @ApiOperation(value = "删除消息(0x 0004)", notes = "删除消息")
  @PutMapping(value = "/usermail/msg/remove")
  public ResultDTO removeMsg(HttpServletRequest request,
      @RequestBody @Valid UmDeleteMailDTO usermail) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService
        .removeMsg(cdtpHeaderDto, usermail.getFrom(), usermail.getTo(), usermail.getMsgIds());
    return resultDto;
  }

  /**
   * 更新类型为阅后即焚的消息的状态为阅后已焚。
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param usermail 请求参数
   * @return 返回ResultDTO对象。
   * @See ResultDTO
   */
  @ApiOperation(value = "阅后即焚消息已焚(0x 0006)", notes = "阅后即焚")
  @PutMapping(value = "/usermail/msg/destory")
  public ResultDTO destroyAfterRead(HttpServletRequest request,
      @Valid @RequestBody UsermailDTO usermail) {
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService
        .destroyAfterRead(cdtpHeaderDto, usermail.getFrom(), usermail.getTo(), usermail.getMsgId());
    return new ResultDTO();
  }

  /**
   * 删除{@link DeleteMailBoxQueryDTO#getFrom()}与{@link DeleteMailBoxQueryDTO#getTo()}的单聊会话关系，可以通过参数{@link
   * DeleteMailBoxQueryDTO#isDeleteAllMsg()}控制是否删除会话消息。
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param queryDto 请求参数。
   * @return 返回ResultDTO对象。
   * @See ResultDTO
   */
  @ApiOperation(value = "删除会话(0x 1004)")
  @DeleteMapping(value = "/usermail/session")
  public ResultDTO deleteSession(HttpServletRequest request, @RequestBody @Valid DeleteMailBoxQueryDTO queryDto) {
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService.deleteSession(cdtpHeaderDto, queryDto);
    return new ResultDTO();
  }

  /**
   * 批量查询发送者与接收者会话消息。
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param from 发送者。
   * @param to 接收者。
   * @param msgIds 批量的消息id。
   * @return 返回ResultDTO对象，返回内容包含批量消息内容{@link UsermailDO}。
   * @See ResultDTO
   */
  @ApiOperation(value = "批量查询消息(0x 1009)")
  @GetMapping(value = "/usermail/msg")
  public ResultDTO batchQueryMsgs(HttpServletRequest request,
      @ApiParam(value = "发送者", required = true) @RequestParam(value = "from", defaultValue = "") String from,
      @ApiParam(value = "接收者", required = true) @RequestParam(value = "to", defaultValue = "") String to,
      @ApiParam(value = "msgId列表", required = true) @RequestParam(value = "msgIds", defaultValue = "") List<String> msgIds) {
    if (CollectionUtils.isEmpty(msgIds)) {
      LOGGER.warn("batchQueryMsgs msgIds is empty & from ={} & to={}", from, to);
      return new ResultDTO();
    }
    List<UsermailDO> usermailList = usermailService.batchQueryMsgs(getHeaderInfoFromRequest(request), from, to, msgIds);
    ResultDTO resultDto = new ResultDTO();
    resultDto.setData(usermailList);
    return resultDto;
  }

  /**
   * 批量查询发送者与接收者会话消息的回复消息总数。
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param from 发送者。
   * @param to 接收者。
   * @param msgIds 批量的消息id。
   * @return 返回ResultDTO对象，返回内容包含批量消息内容{@link UsermailDO}。
   * @See ResultDTO
   */
  @ApiOperation(value = "批量查询消息回复总数(0x 100A)")
  @GetMapping(value = "/usermail/replyCount")
  public ResultDTO batchQueryMsgsReplyCount(HttpServletRequest request,
      @ApiParam(value = "发送者", required = true) @RequestParam(value = "from", defaultValue = "") String from,
      @ApiParam(value = "接收者", required = true) @RequestParam(value = "to", defaultValue = "") String to,
      @ApiParam(value = "msgId列表", required = true) @RequestParam(value = "msgIds", defaultValue = "") List<String> msgIds) {
    if (CollectionUtils.isEmpty(msgIds)) {
      LOGGER.warn("batchQueryMsgs msgIds is empty & from ={} & to={}", from, to);
      return new ResultDTO();
    }
    List<UsermailDO> usermailList = usermailService
        .batchQueryMsgsReplyCount(getHeaderInfoFromRequest(request), from, to, msgIds);
    ResultDTO resultDto = new ResultDTO();
    resultDto.setData(usermailList);
    return resultDto;
  }

  /**
   * 将发送者与接收者单聊会话消息批量移入废纸篓。
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param trashMailDto 发送者{@link MoveTrashMailDTO#getFrom()},接收者{@link MoveTrashMailDTO#getTo()}， 消息id{@link
   * MoveTrashMailDTO#getMsgIds()}
   * @return 返回ResultDTO对象。
   * @See ResultDTO
   */
  @ApiOperation(value = "移送消息到废纸篓(0x 2000)", notes = "移送废纸篓")
  @PostMapping(value = "/usermail/msg/trash")
  public ResultDTO moveMsgToTrash(HttpServletRequest request,
      @RequestBody @Valid MoveTrashMailDTO trashMailDto) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService
        .moveMsgToTrash(cdtpHeaderDto, trashMailDto.getFrom(), trashMailDto.getTo(), trashMailDto.getMsgIds());
    return resultDto;
  }

  /**
   * 将发送者与接收者单聊会话消息批量从废纸篓还原。
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param revertTrashMailDto 发送者{@link TrashMailsDTO#getFrom()},需要从废纸篓还原的消息{@link TrashMailsDTO#getTrashMails()}。
   * @return 返回ResultDTO对象。
   * @See ResultDTO
   */
  @ApiOperation(value = "还原废纸篓消息(0x 2001)", notes = "还原废纸篓消息")
  @PutMapping(value = "/usermail/msg/trash")
  public ResultDTO revertMsgFromTrash(HttpServletRequest request,
      @RequestBody @Valid TrashMailsDTO revertTrashMailDto) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService
        .revertMsgFromTrash(cdtpHeaderDto, revertTrashMailDto.getFrom(), revertTrashMailDto.getTrashMails());
    return resultDto;
  }

  /**
   * 将发送者与接收者单聊会话消息批量从废纸篓中删除
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param revertTrashMailDto 发送者{@link TrashMailsDTO#getFrom()},需要从废纸篓还原的消息{@link TrashMailsDTO#getTrashMails()}。
   * @return 返回ResultDTO对象。
   * @See ResultDTO
   */
  @ApiOperation(value = "删除废纸篓消息(0x 2002)", notes = "删除废纸篓消息")
  @DeleteMapping(value = "/usermail/msg/trash")
  public ResultDTO removeMsgFromTrash(HttpServletRequest request,
      @RequestBody @Valid TrashMailsDTO revertTrashMailDto) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService
        .removeMsgFromTrash(cdtpHeaderDto, revertTrashMailDto.getFrom(), revertTrashMailDto.getTrashMails());
    return resultDto;
  }

  /**
   * 清除发送者废纸篓消息
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param from 发送者
   * @return 返回ResultDTO对象。
   * @See ResultDTO
   */
  @ApiOperation(value = "清空废纸篓消息(0x 2003)", notes = "清空废纸篓")
  @DeleteMapping(value = "/usermail/msg/trash/clear")
  public ResultDTO clearMsgFromTrash(HttpServletRequest request,
      @RequestBody String from) {

    if (StringUtils.isEmpty(from)) {
      throw new IllegalGmArgsException(ERROR_REQUEST_PARAM);
    }

    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService.removeMsgFromTrash(cdtpHeaderDto, from, null);
    return resultDto;
  }

  /**
   * 拉取发送者废纸篓消息列表
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param from 发送者
   * @param pageSize 每次拉取数量
   * @param timestamp 上次消息拉取timestamp
   * @param signal 向前向后拉取标识，值为before或者after,默认before。
   * @return 返回ResultDTO对象，包含废纸篓消息列表，具体字段见{@Link UsermailDO}。
   * @See ResultDTO
   */
  @ApiOperation(value = "同步废纸篓消息(0x 2004)", notes = "还拉取废纸篓消息")
  @GetMapping(value = "/usermail/msg/trash")
  public ResultDTO getMsgFromTrash(
      HttpServletRequest request,
      @ApiParam(value = "发送者", required = true) @RequestParam(value = "from", defaultValue = "") String from,
      @ApiParam(value = "分页大小", required = true) @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
      @ApiParam(value = "上次消息拉取timestamp", required = true) @RequestParam(value = "timestamp", defaultValue = "0") long timestamp,
      @ApiParam(value = "向前向后拉取标识", required = true) @RequestParam(value = "signal", defaultValue = "before") String signal) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    if (timestamp == 0) {
      timestamp = System.currentTimeMillis();
    }
    List<UsermailDO> temailList = usermailService.getMsgFromTrash(cdtpHeaderDto, from, timestamp, pageSize, signal);
    resultDto.setData(temailList);
    return resultDto;
  }

  /**
   * 将发送者与接收者会话，在发送者收件箱移入/移出归档列表。
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param dto 请求参数，发送者{@link UpdateArchiveDTO#getFrom()}，接收者{@link UpdateArchiveDTO#getTo()}， 归档状态{@link
   * UpdateArchiveDTO#getArchiveStatus()}，0 移出归档列表 1 移入归档列表。
   * @return 返回ResultDTO对象。
   * @See ResultDTO
   */
  @ApiOperation(value = "单聊会话归档 (0x 2005)", notes = "单聊归档")
  @PutMapping(value = "/usermail/msg/archive")
  public ResultDTO updateUsermailBoxArchiveStatus(HttpServletRequest request, @RequestBody @Valid
      UpdateArchiveDTO dto) {
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService.updateUsermailBoxArchiveStatus(cdtpHeaderDto, dto.getFrom(), dto.getTo(), dto.getArchiveStatus());
    return new ResultDTO();
  }

  /**
   * 获取HttpServletRequest指定header信息。
   *
   * @param request HttpServletRequest
   * @return CdtpHeaderDTO
   * @See CdtpHeaderDTO
   */
  private CdtpHeaderDTO getHeaderInfoFromRequest(HttpServletRequest request) {
    return new CdtpHeaderDTO(request.getHeader(CDTP_HEADER), request.getHeader(X_PACKET_ID));
  }

}
