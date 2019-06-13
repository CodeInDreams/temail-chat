package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.UsermailBox;
import java.util.List;

public interface UsermailBoxRepo {

  /**
   * 新增单聊会话信息
   */
  void saveUsermailBox(UsermailBox usermailBox);

  /**
   * 查找当前用户的会话列表
   */
  List<UsermailBox> getUsermailBoxByOwner(String mail, int archiveStatus);

  /**
   * 删除指定会话
   */
  int deleteByOwnerAndTo(String from, String to);

  /**
   * 根据会话拥有者和另一位聊天者查询会话列表
   */
  List<UsermailBox> selectByOwnerAndTo(String from, String to);

  /**
   * 更新会话归档状态
   */
  int updateArchiveStatus(String from, String to, int archiveStatus);

  /**
   * 根据拥有者和另一位聊天者查询会话信息
   */
  UsermailBox selectUsermailBox(String owner, String to);

}
