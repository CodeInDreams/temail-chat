package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.UsermailBox;
import java.util.List;

public interface UsermailBoxRepo {

  /**
   * @param usermailBox 会话信息
   * @description 新增单聊会话信息
   */
  void saveUsermailBox(UsermailBox usermailBox);

  /**
   * @param mail 用户地址
   * @param archiveStatus 归档状态
   * @description 查找当前用户的会话列表
   */
  List<UsermailBox> getUsermailBoxByOwner(String mail, int archiveStatus);

  /**
   * @param from 发件人
   * @param to 收件人
   * @description 删除指定会话
   */
  int deleteByOwnerAndTo(String from, String to);

  /**
   * @param from 发件人
   * @param to 收件人
   * @description 根据会话拥有者和另一位聊天者查询会话列表
   */
  List<UsermailBox> selectByOwnerAndTo(String from, String to);

  /**
   * @param from 发件人
   * @param to 收件人
   * @param archiveStatus 归档状态
   * @description 更新会话归档状态
   */
  int updateArchiveStatus(String from, String to, int archiveStatus);

  /**
   * @param owner 会话拥有者
   * @param to 收件人
   * @description 根据拥有者和另一位聊天者查询会话信息
   */
  UsermailBox selectUsermailBox(String owner, String to);

}
