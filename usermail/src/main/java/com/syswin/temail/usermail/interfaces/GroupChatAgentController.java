package com.syswin.temail.usermail.interfaces;

import com.syswin.temail.usermail.application.GroupChatService;
import com.syswin.temail.usermail.core.dto.ResultDTO;
import com.syswin.temail.usermail.dto.GroupChatEventDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

  /**
   * 新增{@link GroupChatEventDTO#getFrom()}与{@link GroupChatEventDTO#getTo()} 单聊会话关系
   *
   * @param groupChatEventDto 请求参数体
   * @return 返回ResultDTO对象
   * @See ResultDTO
   */
  @ApiOperation(value = "添加群成员(0x 0001 2006)", notes = "入群事件")
  @PostMapping(value = "/groupchat/event")
  public ResultDTO syncGroupChatMemberEvent(
      @ApiParam(value = "群事件信息", required = true) @RequestBody @Valid GroupChatEventDTO groupChatEventDto) {
    groupChatService.syncGroupChatMemberEvent(groupChatEventDto);
    return new ResultDTO();
  }

  /**
   * 删除{@link GroupChatEventDTO#getFrom()}与{@link GroupChatEventDTO#getTo()} 单聊会话关系
   *
   * @param groupChatEventDto 请求参数体
   * @return 返回ResultDTO对象
   * @See ResultDTO
   */
  @ApiOperation(value = "出群事件(0x 0001 2007)", notes = "出群事件")
  @DeleteMapping(value = "/groupchat/event")
  public ResultDTO syncGroupChatMemberRemoveEvent(
      @ApiParam(value = "群事件信息", required = true) @RequestBody @Valid GroupChatEventDTO groupChatEventDto) {
    groupChatService.removeGroupChatMemeberEvent(groupChatEventDto);
    return new ResultDTO();
  }
}
