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

import static org.mockito.Mockito.verify;

import com.syswin.temail.usermail.dto.GroupChatEventDTO;
import org.junit.Test;
import org.mockito.Mockito;

public class GroupChatServiceTest {

  private final UsermailMqService usermailMqService = Mockito.mock(UsermailMqService.class);

  private final UsermailService usermailService = Mockito.mock(UsermailService.class);

  private final GroupChatService groupChatService = new GroupChatService(usermailService, usermailMqService);

  @Test
  public void saveGroupChatMailBoxInfoTest() {
    String from = "from@temail.com";
    String to = "to@temail.com";
    String sessionExtData = "sessionExtData";
    GroupChatEventDTO groupChatEventDto = new GroupChatEventDTO();
    groupChatEventDto.setFrom(from);
    groupChatEventDto.setTo(to);
    groupChatEventDto.setSessionExtData(sessionExtData);
    groupChatService.syncGroupChatMemberEvent(groupChatEventDto);
    verify(usermailService).saveUsermailBoxInfo(from, to, to, sessionExtData);
  }

  @Test
  public void deleteGroupChatMailBoxInfo() {
    String from = "from@temail.com";
    String to = "to@temail.com";
    GroupChatEventDTO groupChatEventDto = new GroupChatEventDTO();
    groupChatEventDto.setFrom(from);
    groupChatEventDto.setTo(to);
    groupChatService.removeGroupChatMemeberEvent(groupChatEventDto);
    verify(usermailMqService).sendMqRemoveGroupMemberMsg(from, to);
  }
}