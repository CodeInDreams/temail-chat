package com.syswin.temail.usermail.infrastructure.domain.impl;

import com.syswin.temail.usermail.domains.UsermailBox;
import com.syswin.temail.usermail.infrastructure.domain.UsermailBoxRepo;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailBoxMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsermailBoxRepoImpl implements UsermailBoxRepo {

  private final UsermailBoxMapper usermailBoxMapper;

  @Autowired
  public UsermailBoxRepoImpl(UsermailBoxMapper usermailBoxMapper) {
    this.usermailBoxMapper = usermailBoxMapper;
  }

  /**
   * @param usermailBox 会话信息
   * @description 新增单聊会话信息
   */
  @Override
  public void saveUsermailBox(UsermailBox usermailBox) {
    usermailBoxMapper.saveUsermailBox(usermailBox);
  }

  /**
   * @param mail 用户地址
   * @param archiveStatus 归档状态
   * @description 查找当前用户的会话列表
   */
  @Override
  public List<UsermailBox> getUsermailBoxByOwner(String mail, int archiveStatus) {
    return usermailBoxMapper.getUsermailBoxByOwner(mail, archiveStatus);
  }

  /**
   * @param from 发件人
   * @param to 收件人
   * @description 删除指定会话
   */
  @Override
  public int deleteByOwnerAndTo(String from, String to) {
    return usermailBoxMapper.deleteByOwnerAndTo(from, to);
  }

  /**
   * @param from 发件人
   * @param to 收件人
   * @description 根据会话拥有者和另一位聊天者查询会话列表
   */
  @Override
  public List<UsermailBox> selectByOwnerAndTo(String from, String to) {
    return usermailBoxMapper.selectByOwnerAndTo(from, to);
  }

  /**
   * @param from 发件人
   * @param to 收件人
   * @param archiveStatus 归档状态
   * @description 更新会话归档状态
   */
  @Override
  public int updateArchiveStatus(String from, String to, int archiveStatus) {
    return usermailBoxMapper.updateArchiveStatus(from, to, archiveStatus);
  }

  /**
   * @param owner 会话拥有者
   * @param to 收件人
   * @description 根据拥有者和另一位聊天者查询会话信息
   */
  @Override
  public UsermailBox selectUsermailBox(String owner, String to) {
    return usermailBoxMapper.selectUsermailBox(owner, to);
  }

}