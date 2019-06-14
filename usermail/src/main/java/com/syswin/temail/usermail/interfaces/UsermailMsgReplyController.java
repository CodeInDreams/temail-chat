package com.syswin.temail.usermail.interfaces;

import static com.syswin.temail.usermail.common.ParamsKey.HttpHeaderKey.CDTP_HEADER;
import static com.syswin.temail.usermail.common.ParamsKey.HttpHeaderKey.X_PACKET_ID;

import com.syswin.temail.usermail.application.UmBlacklistProxy;
import com.syswin.temail.usermail.application.UsermailMsgReplyService;
import com.syswin.temail.usermail.common.ResultCodeEnum;
import com.syswin.temail.usermail.common.Constants.TemailStoreType;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDTO;
import com.syswin.temail.usermail.core.dto.ResultDTO;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import com.syswin.temail.usermail.domains.UsermailMsgReply;
import com.syswin.temail.usermail.dto.ReplyDestoryDTO;
import com.syswin.temail.usermail.dto.UsermailMsgReplyDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsermailMsgReplyController {

  private static final Logger LOGGER = LoggerFactory.getLogger(UsermailMsgReplyController.class);
  private final UmBlacklistProxy umBlacklistProxy;
  private final UsermailMsgReplyService usermailMsgReplyService;

  public UsermailMsgReplyController(UmBlacklistProxy umBlacklistProxy,
      UsermailMsgReplyService usermailMsgReplyService) {
    this.umBlacklistProxy = umBlacklistProxy;
    this.usermailMsgReplyService = usermailMsgReplyService;
  }

  /**
   * 发送单聊回复消息
   *
   * @param msgReplyDto 请求参数{@link UsermailMsgReplyDTO}
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @return 返回ResultDTO对象
   * @See ResultDTO
   */
  @ApiOperation(value = "发送单聊回复消息(0x 0001 1005)", notes = "发送单聊回复消息")
  @PostMapping(value = "/usermail/msg/reply")
  public ResultDTO sendMailMsgReply(@RequestBody @Valid UsermailMsgReplyDTO msgReplyDto,
      HttpServletRequest request) {
    ResultDTO resultDto = new ResultDTO();
    umBlacklistProxy.checkInBlacklist(msgReplyDto.getFrom(), msgReplyDto.getTo());
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    int storeType = msgReplyDto.getStoreType();
    String owner;
    if (storeType == TemailStoreType.STORE_TYPE_TO_1) {
      // owner消息所属人 store_type_to_1表示存收件人收件箱，所以owner为to即为收件人
      owner = msgReplyDto.getTo();
    } else if (storeType == TemailStoreType.STORE_TYPE_FROM_2) {
      owner = msgReplyDto.getFrom();
    } else {
      LOGGER.warn("storeType is error:{}", storeType);
      throw new IllegalGmArgsException(ResultCodeEnum.ERROR_ILLEGAL_STORE_TYPE);
    }
    Map data = usermailMsgReplyService
        .createMsgReply(cdtpHeaderDto, msgReplyDto.getFrom(), msgReplyDto.getTo(), msgReplyDto.getMsgData(),
            msgReplyDto.getMsgId(),
            msgReplyDto.getParentMsgId(), msgReplyDto.getType(), msgReplyDto.getAttachmentSize(), owner);
    resultDto.setData(data);
    return resultDto;
  }

  /**
   * 撤回单聊消息
   *
   * @param msgReplyDto 请求参数{@link UsermailMsgReplyDTO}
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @return 返回ResultDTO对象
   * @See ResultDTO
   */
  @ApiOperation(value = "撤回单聊回复消息(0x 0001 1006)", notes = "撤回单聊回复消息")
  @PutMapping(value = "/usermail/msg/reply")
  public ResultDTO revertMailMsgReply(@Valid @RequestBody UsermailMsgReplyDTO msgReplyDto, HttpServletRequest request) {
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailMsgReplyService
        .revertMsgReply(cdtpHeaderDto, msgReplyDto.getParentMsgId(), msgReplyDto.getMsgId(), msgReplyDto.getFrom(),
            msgReplyDto.getTo());
    return new ResultDTO();
  }

  /**
   * 删除单聊回复消息
   *
   * @param msgReplyDto 请求参数{@link UsermailMsgReplyDTO}
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @return 返回ResultDTO对象
   * @See ResultDTO
   */
  @ApiOperation(value = "删除单聊回复消息(0x 0001 1007)", notes = "删除单聊回复消息")
  @DeleteMapping(value = "/usermail/msg/reply")
  public ResultDTO removeMailMsgReplys(@Valid @RequestBody UsermailMsgReplyDTO msgReplyDto,
      HttpServletRequest request) {
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailMsgReplyService
        .removeMsgReplys(cdtpHeaderDto, msgReplyDto.getParentMsgId(), msgReplyDto.getMsgIds(), msgReplyDto.getFrom(),
            msgReplyDto.getTo());
    return new ResultDTO();
  }

  /**
   * 拉取单聊回复消息列表。
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param parentMsgid 单聊消息id
   * @param pageSize 分页大小
   * @param seqId 上次消息拉取seqId
   * @param from 消息所属人
   * @param signal 向前向后拉取标识
   * @param filterSeqIds 断层的seqId范围
   * @return 返回ResultDTO对象，结果包含回复消息列表{@link UsermailMsgReply}。
   * @See ResultDTO
   */
  @ApiOperation(value = "拉取单聊回复消息(0x 0001 1008)", notes = "拉取单聊回复消息")
  @GetMapping(value = "/usermail/msg/reply")
  public ResultDTO getMailMsgReplys(HttpServletRequest request,
      @ApiParam(value = "源消息msgid", required = true) @RequestParam(value = "parentMsgId", defaultValue = "") String parentMsgid,
      @ApiParam(value = "分页大小", required = true) @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
      @ApiParam(value = "上次消息拉取seqID", required = true) @RequestParam(value = "seqId", defaultValue = "0") long seqId,
      @ApiParam(value = "消息所属人", required = true) @RequestParam(value = "from") String from,
      @ApiParam(value = "向前向后拉取标识", required = true) @RequestParam(value = "signal", defaultValue = "before") String signal,
      @ApiParam(value = "断层的seqId范围") @RequestParam(value = "filterSeqIds", required = false, defaultValue = "") String filterSeqIds) {
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    List<UsermailMsgReply> data = usermailMsgReplyService
        .getMsgReplys(cdtpHeaderDto, parentMsgid, pageSize, seqId, signal, from, filterSeqIds);
    ResultDTO resultDto = new ResultDTO();
    resultDto.setData(data);
    return resultDto;
  }

  /**
   * 更新类型为阅后即焚的回复消息的状态为阅后已焚
   *
   * @param request 从HttpServletRequest中获取业务header：CDTP-header,X-PACKET-ID。
   * @param replyDestoryDto 请求参数{@link ReplyDestoryDTO}
   * @return 返回ResultDTO对象
   * @See ResultDTO
   */
  @ApiOperation(value = "阅后即焚回复消息已焚(0x 100B)", notes = "回复消息阅后即焚")
  @PutMapping(value = "/usermail/msg/reply/destory")
  public ResultDTO destoryAfterRead(HttpServletRequest request,
      @RequestBody @Valid ReplyDestoryDTO replyDestoryDto) {
    ResultDTO resultDto = new ResultDTO();
    CdtpHeaderDTO cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailMsgReplyService
        .destoryAfterRead(cdtpHeaderDto, replyDestoryDto.getFrom(), replyDestoryDto.getTo(),
            replyDestoryDto.getMsgId());
    return resultDto;
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
