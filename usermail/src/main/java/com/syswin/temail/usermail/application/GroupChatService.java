package com.syswin.temail.usermail.application;

import com.syswin.temail.transactional.TemailShardingTransactional;
import com.syswin.temail.usermail.dto.GroupChatEventDTO;
import org.springframework.stereotype.Service;

@Service
public class GroupChatService {

  private final UsermailService usermailService;

  private final UsermailMqService usermailMqService;

  public GroupChatService(UsermailService usermailService, UsermailMqService usermailMqService) {
    this.usermailService = usermailService;
    this.usermailMqService = usermailMqService;
  }

  @TemailShardingTransactional(shardingField = "#dto.to")
  public void syncGroupChatMemberEvent(GroupChatEventDTO dto) {
    String groupTemail = dto.getFrom();
    String temail = dto.getTo();
    usermailService.saveUsermailBoxInfo(groupTemail, temail, temail);
  }

  @TemailShardingTransactional(shardingField = "#dto.to")
  public void removeGroupChatMemeberEvent(GroupChatEventDTO dto) {
    String groupTemail = dto.getFrom();
    String to = dto.getTo();
    usermailMqService.sendMqRemoveGroupMemberMsg(groupTemail, to);
  }

}
