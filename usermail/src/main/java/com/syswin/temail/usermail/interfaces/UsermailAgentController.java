package com.syswin.temail.usermail.interfaces;

import static com.syswin.temail.usermail.common.Constants.HttpHeaderKey.CDTP_HEADER;
import static com.syswin.temail.usermail.common.Constants.HttpHeaderKey.X_PACKET_ID;
import static com.syswin.temail.usermail.common.Constants.RESULT_CODE.ERROR_REQUEST_PARAM;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syswin.temail.usermail.application.UmBlacklistProxy;
import com.syswin.temail.usermail.application.UsermailService;
import com.syswin.temail.usermail.common.Constants.RESULT_CODE;
import com.syswin.temail.usermail.common.Constants.TemailStoreType;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.core.dto.ResultDTO;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import com.syswin.temail.usermail.domains.Usermail;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

  @ApiOperation(value = "发送消息(0x 0001)", notes = "[from]发送消息给[to]")
  @RequestMapping(value = "/usermail", method = RequestMethod.POST)
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
      throw new IllegalGmArgsException(RESULT_CODE.ERROR_ILLEGAL_STORE_TYPE);
    }
    Map result = usermailService.sendMail(cdtpHeaderDto, usermail, owner, other);
    resultDto.setData(result);
    return resultDto;
  }


  @ApiOperation(value = "同步单聊会话消息(0x 0003)", notes = "接收者拉取邮件及附件相关信息")
  @RequestMapping(value = "/usermail", method = RequestMethod.GET)
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
    List<Usermail> temailList = usermailService
        .getMails(cdtpHeaderDto, from, to, seqId, pageSize, filterSeqIds, signal);

    resultDto.setData(temailList);
    return resultDto;
  }


  @ApiOperation(value = "撤回消息(0x 0005)", notes = "撤回消息")
  @RequestMapping(value = "/revert", method = RequestMethod.PUT)
  public ResultDTO revert(
      HttpServletRequest request,
      @RequestBody @Valid UsermailDTO usermail) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService
        .revert(cdtpHeaderDto, usermail.getFrom(), usermail.getTo(), usermail.getMsgId());
    return resultDto;
  }


  @ApiOperation(value = "同步会话列表(0x 0002)", notes = "同步[from]会话列表")
  @RequestMapping(value = "/usermail/mailboxes", method = RequestMethod.GET)
  public ResultDTO mailboxes(
      HttpServletRequest request,
      @ApiParam(value = "发送者mail", required = true) @RequestParam(value = "from", defaultValue = "") String from,
      @ApiParam(value = "归档状态 0 正常 1 已归档 -1 全部（默认）") @RequestParam(value = "archiveStatus", defaultValue = "-1") int archiveStatus,
      @ApiParam(value = "拉取列表") @RequestParam(value = "localMailboxes", defaultValue = "") String usermailBoxes) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    Map<String, String> mailboxMap;
    Gson gson = new GsonBuilder().create();
    if (StringUtils.isEmpty(usermailBoxes)) {
      mailboxMap = null;
    } else {
      mailboxMap = gson.fromJson(usermailBoxes, Map.class);
    }
    List<MailboxDTO> list = usermailService.mailboxes(cdtpHeaderDto, from, archiveStatus, mailboxMap);
    resultDto.setData(list);
    return resultDto;
  }

  @ApiOperation(value = "删除消息(0x 0004)", notes = "删除消息")
  @RequestMapping(value = "/usermail/msg/remove", method = RequestMethod.PUT)
  public ResultDTO removeMsg(HttpServletRequest request,
      @RequestBody @Valid UmDeleteMailDTO usermail) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService
        .removeMsg(cdtpHeaderDto, usermail.getFrom(), usermail.getTo(), usermail.getMsgIds());
    return resultDto;
  }

  @ApiOperation(value = "阅后即焚消息已焚(0x 0006)", notes = "阅后即焚")
  @RequestMapping(value = "/usermail/msg/destory", method = RequestMethod.PUT)
  public ResultDTO destroyAfterRead(HttpServletRequest request,
      @Valid @RequestBody UsermailDTO usermail) {
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService
        .destroyAfterRead(cdtpHeaderDto, usermail.getFrom(), usermail.getTo(), usermail.getMsgId());
    return new ResultDTO();
  }

  @ApiOperation(value = "删除会话(0x 1004)")
  @RequestMapping(value = "/usermail/session", method = RequestMethod.DELETE)
  public ResultDTO deleteSession(HttpServletRequest request, @RequestBody @Valid DeleteMailBoxQueryDTO queryDto) {
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService.deleteSession(cdtpHeaderDto, queryDto);
    return new ResultDTO();
  }

  @ApiOperation(value = "批量查询消息(0x 1009)")
  @RequestMapping(value = "/usermail/msg", method = RequestMethod.GET)
  public ResultDTO batchQueryMsgs(HttpServletRequest request,
      @ApiParam(value = "发送者", required = true) @RequestParam(value = "from", defaultValue = "") String from,
      @ApiParam(value = "接收者", required = true) @RequestParam(value = "to", defaultValue = "") String to,
      @ApiParam(value = "msgId列表", required = true) @RequestParam(value = "msgIds", defaultValue = "") List<String> msgIds) {
    if (CollectionUtils.isEmpty(msgIds)) {
      LOGGER.warn("batchQueryMsgs msgIds is empty & from ={} & to={}", from, to);
      return new ResultDTO();
    }
    List<Usermail> usermailList = usermailService.batchQueryMsgs(getHeaderInfoFromRequest(request), from, to, msgIds);
    ResultDTO resultDto = new ResultDTO();
    resultDto.setData(usermailList);
    return resultDto;
  }

  @ApiOperation(value = "批量查询消息回复总数(0x 100A)")
  @RequestMapping(value = "/usermail/replyCount", method = RequestMethod.GET)
  public ResultDTO batchQueryMsgsReplyCount(HttpServletRequest request,
      @ApiParam(value = "发送者", required = true) @RequestParam(value = "from", defaultValue = "") String from,
      @ApiParam(value = "接收者", required = true) @RequestParam(value = "to", defaultValue = "") String to,
      @ApiParam(value = "msgId列表", required = true) @RequestParam(value = "msgIds", defaultValue = "") List<String> msgIds) {
    if (CollectionUtils.isEmpty(msgIds)) {
      LOGGER.warn("batchQueryMsgs msgIds is empty & from ={} & to={}", from, to);
      return new ResultDTO();
    }
    List<Usermail> usermailList = usermailService
        .batchQueryMsgsReplyCount(getHeaderInfoFromRequest(request), from, to, msgIds);
    ResultDTO resultDto = new ResultDTO();
    resultDto.setData(usermailList);
    return resultDto;
  }

  @ApiOperation(value = "移送消息到废纸篓(0x 2000)", notes = "移送废纸篓")
  @RequestMapping(value = "/usermail/msg/trash", method = RequestMethod.POST)
  public ResultDTO moveMsgToTrash(HttpServletRequest request,
      @RequestBody @Valid MoveTrashMailDTO trashMailDto) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService
        .moveMsgToTrash(cdtpHeaderDto, trashMailDto.getFrom(), trashMailDto.getTo(), trashMailDto.getMsgIds());
    return resultDto;
  }

  @ApiOperation(value = "还原废纸篓消息(0x 2001)", notes = "还原废纸篓消息")
  @RequestMapping(value = "/usermail/msg/trash", method = RequestMethod.PUT)
  public ResultDTO revertMsgToTrash(HttpServletRequest request,
      @RequestBody @Valid TrashMailsDTO revertTrashMailDto) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService
        .revertMsgToTrash(cdtpHeaderDto, revertTrashMailDto.getFrom(), revertTrashMailDto.getTrashMails());
    return resultDto;
  }

  @ApiOperation(value = "删除废纸篓消息(0x 2002)", notes = "删除废纸篓消息")
  @RequestMapping(value = "/usermail/msg/trash", method = RequestMethod.DELETE)
  public ResultDTO removeMsgFromTrash(HttpServletRequest request,
      @RequestBody @Valid TrashMailsDTO revertTrashMailDto) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService
        .removeMsgFromTrash(cdtpHeaderDto, revertTrashMailDto.getFrom(), revertTrashMailDto.getTrashMails());
    return resultDto;
  }

  @ApiOperation(value = "清空废纸篓消息(0x 2003)", notes = "清空废纸篓")
  @RequestMapping(value = "/usermail/msg/trash/clear", method = RequestMethod.DELETE)
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

  @ApiOperation(value = "同步废纸篓消息(0x 2004)", notes = "还拉取废纸篓消息")
  @RequestMapping(value = "/usermail/msg/trash", method = RequestMethod.GET)
  public ResultDTO getMsgFromTrash(
      HttpServletRequest request,
      @ApiParam(value = "发送者", required = true) @RequestParam(value = "from", defaultValue = "") String from,
      @ApiParam(value = "分页`大小", required = true) @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
      @ApiParam(value = "上次消息拉取SeqNo", required = true) @RequestParam(value = "timestamp", defaultValue = "0") long timestamp,
      @ApiParam(value = "向前向后拉取标识", required = true) @RequestParam(value = "signal", defaultValue = "before") String signal) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    if (timestamp == 0) {
      timestamp = System.currentTimeMillis();
    }
    List<Usermail> temailList = usermailService.getMsgFromTrash(cdtpHeaderDto, from, timestamp, pageSize, signal);
    resultDto.setData(temailList);
    return resultDto;
  }

  @ApiOperation(value = "单聊会话归档 (0x 2005)", notes = "单聊归档")
  @RequestMapping(value = "/usermail/msg/archive", method = RequestMethod.PUT)
  public ResultDTO updateUsermailBoxArchiveStatus(HttpServletRequest request, @RequestBody @Valid
      UpdateArchiveDTO dto) {
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailService.updateUsermailBoxArchiveStatus(cdtpHeaderDto, dto.getFrom(), dto.getTo(), dto.getArchiveStatus());
    return new ResultDTO();
  }

  private CdtpHeaderDTO getHeaderInfoFromRequest(HttpServletRequest request) {
    return new CdtpHeaderDTO(request.getHeader(CDTP_HEADER), request.getHeader(X_PACKET_ID));
  }

}
