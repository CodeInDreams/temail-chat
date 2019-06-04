package com.syswin.temail.usermail.application;

import static org.mockito.Mockito.verify;

import com.syswin.temail.usermail.dto.GroupChatEventDto;
import org.junit.Test;
import org.mockito.Mockito;

public class GroupChatServiceTest {

  private final UsermailMqService usermailMqService = Mockito.mock(UsermailMqService.class);

  private final UsermailService usermailService = Mockito.mock(UsermailService.class);

  private final GroupChatService groupChatService = new GroupChatService(usermailService, usermailMqService);

  @Test
  public void saveGroupChatMailBoxInfo() {
    String from = "from@temail.com";
    String to = "to@temail.com";
    GroupChatEventDto groupChatEventDto = new GroupChatEventDto();
    groupChatEventDto.setFrom(from);
    groupChatEventDto.setTo(to);
    groupChatService.syncGroupChatMemberEvent(groupChatEventDto);
    verify(usermailService).saveUsermailBoxInfo(from, to, to);
  }

  @Test
  public void deleteGroupChatMailBoxInfo() {
    String from = "from@temail.com";
    String to = "to@temail.com";
    GroupChatEventDto groupChatEventDto = new GroupChatEventDto();
    groupChatEventDto.setFrom(from);
    groupChatEventDto.setTo(to);
    groupChatService.removeGroupChatMemeberEvent(groupChatEventDto);
    verify(usermailMqService).sendMqRemoveGroupMemberMsg(from, to);
  }
}