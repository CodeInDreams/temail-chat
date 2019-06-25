package com.syswin.temail.usermail.infrastructure.domain.impl;

import com.syswin.temail.usermail.domains.UsermailBoxDO;
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
   *
   * @param usermailBox 会话信息
   */
  @Override
  public void saveUsermailBox(UsermailBoxDO usermailBox) {
    usermailBoxMapper.saveUsermailBox(usermailBox);
  }

  /**
   * 查找当前用户的会话列表
   *
   * @param mail 用户地址
   * @param archiveStatus 归档状态
   * @return 会话列表
   */
  @Override
  public List<UsermailBoxDO> listUsermailBoxsByOwner(String mail, int archiveStatus) {
    return usermailBoxMapper.listUsermailBoxsByOwner(mail, archiveStatus);
  }

  /**
   * 删除指定会话
   *
   * @param from 发件人
   * @param to 收件人
   * @return 删除的数量
   */
  @Override
  public int deleteUsermailBox(String from, String to) {
    return usermailBoxMapper.deleteUsermailBox(from, to);
  }

  /**
   * 根据会话拥有者和另一位聊天者查询会话列表
   *
   * @param from 发件人
   * @param to 收件人
   * @return 会话列表
   */
  @Override
  public List<UsermailBoxDO> listUsermailBoxsByOwnerAndTo(String from, String to) {
    return usermailBoxMapper.listUsermailBoxsByOwnerAndTo(from, to);
  }

  /**
   * 更新会话归档状态
   *
   * @param from 发件人
   * @param to 收件人
   * @param archiveStatus 归档状态
   * @return 更新的数量
   */
  @Override
  public int updateArchiveStatus(String from, String to, int archiveStatus) {
    return usermailBoxMapper.updateArchiveStatus(from, to, archiveStatus);
  }

  /**
   * 根据拥有者和另一位聊天者查询会话信息
   *
   * @param owner 会话拥有者
   * @param to 收件人
   * @return 会话信息
   */
  @Override
  public UsermailBoxDO getUsermailBox(String owner, String to) {
    return usermailBoxMapper.getUsermailBox(owner, to);
  }

}