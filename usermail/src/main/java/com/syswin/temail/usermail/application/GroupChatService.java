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

import com.syswin.temail.usermail.dto.GroupChatEventDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupChatService {

  private final UsermailService usermailService;

  private final UsermailMqService usermailMqService;

  public GroupChatService(UsermailService usermailService, UsermailMqService usermailMqService) {
    this.usermailService = usermailService;
    this.usermailMqService = usermailMqService;
  }


  /**
   * 群聊入群事件，新建会话
   *
   * @param dto 入群事件参数(from:群 to:群成员 sessionExtData:会话头像昵称)
   */
  @Transactional
  public void syncGroupChatMemberEvent(GroupChatEventDTO dto) {
    String groupTemail = dto.getFrom();
    String temail = dto.getTo();
    usermailService.saveUsermailBoxInfo(groupTemail, temail, temail, dto.getSessionExtData());
  }


  /**
   * 群聊出群事件，删除会话
   *
   * @param dto 出群事件参数(from:群 to:群成员)
   */
  @Transactional
  public void removeGroupChatMemeberEvent(GroupChatEventDTO dto) {
    String groupTemail = dto.getFrom();
    String to = dto.getTo();
    usermailMqService.sendMqRemoveGroupMemberMsg(groupTemail, to);
  }

}
