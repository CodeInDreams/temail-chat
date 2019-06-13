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
   * 新增单聊会话信息
   */
  @Override
  public void saveUsermailBox(UsermailBox usermailBox) {
    usermailBoxMapper.saveUsermailBox(usermailBox);
  }

  /**
   * 查找当前用户的会话列表
   */
  @Override
  public List<UsermailBox> getUsermailBoxByOwner(String mail, int archiveStatus) {
    return usermailBoxMapper.getUsermailBoxByOwner(mail, archiveStatus);
  }

  /**
   * 删除指定会话
   */
  @Override
  public int deleteByOwnerAndTo(String from, String to) {
    return usermailBoxMapper.deleteByOwnerAndTo(from, to);
  }

  /**
   * 根据会话拥有者和另一位聊天者查询会话列表
   */
  @Override
  public List<UsermailBox> selectByOwnerAndTo(String from, String to) {
    return usermailBoxMapper.selectByOwnerAndTo(from, to);
  }

  /**
   * 更新会话归档状态
   */
  @Override
  public int updateArchiveStatus(String from, String to, int archiveStatus) {
    return usermailBoxMapper.updateArchiveStatus(from, to, archiveStatus);
  }

  /**
   * 根据拥有者和另一位聊天者查询会话信息
   */
  @Override
  public UsermailBox selectUsermailBox(String owner, String to) {
    return usermailBoxMapper.selectUsermailBox(owner, to);
  }

}