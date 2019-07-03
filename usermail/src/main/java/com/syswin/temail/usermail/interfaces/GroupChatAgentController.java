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
