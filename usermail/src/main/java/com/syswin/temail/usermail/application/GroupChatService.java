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
   * @param dto 入群事件参数(from:群 to:群成员)
   */
  @Transactional
  public void syncGroupChatMemberEvent(GroupChatEventDTO dto) {
    String groupTemail = dto.getFrom();
    String temail = dto.getTo();
    usermailService.saveUsermailBoxInfo(groupTemail, temail, temail);
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
