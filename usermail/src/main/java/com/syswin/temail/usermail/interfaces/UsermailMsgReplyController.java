package com.syswin.temail.usermail.interfaces;

import static com.syswin.temail.usermail.common.Contants.HttpHeaderKey.CDTP_HEADER;
import static com.syswin.temail.usermail.common.Contants.HttpHeaderKey.X_PACKET_ID;

import com.syswin.temail.usermail.application.UmBlacklistProxy;
import com.syswin.temail.usermail.application.UsermailMsgReplyService;
import com.syswin.temail.usermail.common.Contants.RESULT_CODE;
import com.syswin.temail.usermail.common.Contants.TemailStoreType;
import com.syswin.temail.usermail.core.dto.CdtpHeaderDto;
import com.syswin.temail.usermail.core.dto.ResultDto;
import com.syswin.temail.usermail.core.exception.IllegalGMArgsException;
import com.syswin.temail.usermail.domains.UsermailMsgReply;
import com.syswin.temail.usermail.dto.ReplyDestoryDto;
import com.syswin.temail.usermail.dto.UsermailMsgReplyDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

  @ApiOperation(value = "发送单聊回复消息(0x 0001 1005)", notes = "发送单聊回复消息")
  @RequestMapping(value = "/usermail/msg/reply", method = RequestMethod.POST)
  public ResultDto sendMailMsgReply(@RequestBody @Valid UsermailMsgReplyDto msgReplyDto,
      HttpServletRequest request) {
    ResultDto resultDto = new ResultDto();
    umBlacklistProxy.checkInBlacklist(msgReplyDto.getFrom(), msgReplyDto.getTo());
    CdtpHeaderDto cdtpHeaderDto = getHeaderInfoFromRequest(request);
    int storeType = msgReplyDto.getStoreType();
    String owner;
    if (storeType == TemailStoreType.STORE_TYPE_TO_1) {
      owner = msgReplyDto.getTo();//owner消息所属人 store_type_to_1表示存收件人收件箱，所以owner为to即为收件人
    } else if (storeType == TemailStoreType.STORE_TYPE_FROM_2) {
      owner = msgReplyDto.getFrom();
    } else {
      LOGGER.warn("storeType is error:{}", storeType);
      throw new IllegalGMArgsException(RESULT_CODE.ERROR_ILLEGAL_STORE_TYPE);
    }
    Map data = usermailMsgReplyService
        .createMsgReply(cdtpHeaderDto, msgReplyDto.getFrom(), msgReplyDto.getTo(), msgReplyDto.getMsgData(),
            msgReplyDto.getMsgId(),
            msgReplyDto.getParentMsgId(), msgReplyDto.getType(), msgReplyDto.getAttachmentSize(), owner);
    resultDto.setData(data);
    return resultDto;
  }

  @ApiOperation(value = "撤回单聊回复消息(0x 0001 1006)", notes = "撤回单聊回复消息")
  @RequestMapping(value = "/usermail/msg/reply", method = RequestMethod.PUT)
  public ResultDto revertMailMsgReply(@Valid @RequestBody UsermailMsgReplyDto msgReplyDto, HttpServletRequest request) {
    CdtpHeaderDto cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailMsgReplyService
        .revertMsgReply(cdtpHeaderDto, msgReplyDto.getParentMsgId(), msgReplyDto.getMsgId(), msgReplyDto.getFrom(),
            msgReplyDto.getTo());
    return new ResultDto();
  }

  @ApiOperation(value = "删除单聊回复消息(0x 0001 1007)", notes = "删除单聊回复消息")
  @RequestMapping(value = "/usermail/msg/reply", method = RequestMethod.DELETE)
  public ResultDto removeMailMsgReplys(@Valid @RequestBody UsermailMsgReplyDto msgReplyDto,
      HttpServletRequest request) {
    CdtpHeaderDto cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailMsgReplyService
        .removeMsgReplys(cdtpHeaderDto, msgReplyDto.getParentMsgId(), msgReplyDto.getMsgIds(), msgReplyDto.getFrom(),
            msgReplyDto.getTo());
    return new ResultDto();
  }


  @ApiOperation(value = "拉取单聊回复消息(0x 0001 1008)", notes = "拉取单聊回复消息")
  @RequestMapping(value = "/usermail/msg/reply", method = RequestMethod.GET)
  public ResultDto getMailMsgReplys(HttpServletRequest request,
      @ApiParam(value = "源消息msgid", required = true) @RequestParam(value = "parentMsgId", defaultValue = "") String parentMsgid,
      @ApiParam(value = "分页大小", required = true) @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
      @ApiParam(value = "上次消息拉取seqID", required = true) @RequestParam(value = "seqId", defaultValue = "0") long seqId,
      @ApiParam(value = "消息所属人", required = true) @RequestParam(value = "from") String from,
      @ApiParam(value = "向前向后拉取标识", required = true) @RequestParam(value = "signal", defaultValue = "before") String signal,
      @ApiParam(value = "断层的seqId范围") @RequestParam(value = "filterSeqIds", required = false, defaultValue = "") String filterSeqIds) {
    CdtpHeaderDto cdtpHeaderDto = getHeaderInfoFromRequest(request);
    List<UsermailMsgReply> data = usermailMsgReplyService
        .getMsgReplys(cdtpHeaderDto, parentMsgid, pageSize, seqId, signal, from, filterSeqIds);
    ResultDto resultDto = new ResultDto();
    resultDto.setData(data);
    return resultDto;
  }

  @ApiOperation(value = "阅后即焚回复消息已焚(0x 100B)", notes = "回复消息阅后即焚")
  @RequestMapping(value = "/usermail/msg/reply/destory", method = RequestMethod.PUT)
  public ResultDto destoryAfterRead(HttpServletRequest request,
      @RequestBody @Valid ReplyDestoryDto replyDestoryDto) {
    ResultDto resultDto = new ResultDto();
    CdtpHeaderDto cdtpHeaderDto = getHeaderInfoFromRequest(request);
    usermailMsgReplyService
        .destoryAfterRead(cdtpHeaderDto, replyDestoryDto.getFrom(), replyDestoryDto.getTo(),
            replyDestoryDto.getMsgId());
    return resultDto;
  }

  private CdtpHeaderDto getHeaderInfoFromRequest(HttpServletRequest request) {
    return new CdtpHeaderDto(request.getHeader(CDTP_HEADER), request.getHeader(X_PACKET_ID));
  }
}
