package com.syswin.temail.usermail.interfaces;

import com.syswin.temail.usermail.application.GroupChatService;
import com.syswin.temail.usermail.core.dto.ResultDto;
import com.syswin.temail.usermail.dto.GroupChatEventDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GroupChatAgentController {

  private final GroupChatService groupChatService;

  public GroupChatAgentController(GroupChatService groupChatService) {
    this.groupChatService = groupChatService;
  }

  @ApiOperation(value = "添加群成员(0x 0001 2006)", notes = "入群事件")
  @PostMapping(value = "/groupchat/event")
  public ResultDto syncGroupChatMemberEvent(HttpServletRequest request,
      @ApiParam(value = "群事件信息", required = true) @RequestBody @Valid GroupChatEventDTO groupChatEventDto) {
    ResultDto resultDto = new ResultDto();
    groupChatService.syncGroupChatMemberEvent(groupChatEventDto);
    return resultDto;
  }

  @ApiOperation(value = "出群事件(0x 0001 2007)", notes = "出群事件")
  @DeleteMapping(value = "/groupchat/event")
  public ResultDto syncGroupChatMemberRemoveEvent(HttpServletRequest request,
      @ApiParam(value = "群事件信息", required = true) @RequestBody @Valid GroupChatEventDTO groupChatEventDto) {
    ResultDto resultDto = new ResultDto();
    groupChatService.removeGroupChatMemeberEvent(groupChatEventDto);
    return resultDto;
  }
}
