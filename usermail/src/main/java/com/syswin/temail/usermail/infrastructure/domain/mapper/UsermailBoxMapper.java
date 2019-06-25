package com.syswin.temail.usermail.infrastructure.domain.mapper;

import com.syswin.temail.usermail.domains.UsermailBoxDO;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailBoxMapper {

  /**
   * 新增单聊会话信息
   *
   * @param usermailBox 会话信息
   */
  void saveUsermailBox(UsermailBoxDO usermailBox);

  /**
   * 查找当前用户的会话列表
   *
   * @param mail 用户地址
   * @param archiveStatus 归档状态
   * @return 会话列表
   */
  List<UsermailBoxDO> listUsermailBoxsByOwner(@Param("owner") String mail, @Param("archiveStatus") int archiveStatus);

  /**
   * 删除指定会话
   *
   * @param from 发件人
   * @param to 收件人
   * @return 删除的数量
   */
  int deleteUsermailBox(@Param("owner") String from, @Param("mail2") String to);

  /**
   * 根据会话拥有者和另一位聊天者查询会话列表
   *
   * @param from 发件人
   * @param to 收件人
   * @return 会话列表
   */
  List<UsermailBoxDO> listUsermailBoxsByOwnerAndTo(@Param("owner") String from, @Param("mail2") String to);

  /**
   * 更新会话归档状态
   *
   * @param from 发件人
   * @param to 收件人
   * @param archiveStatus 归档状态
   * @return 更新的数量
   */
  int updateArchiveStatus(@Param("owner") String from, @Param("mail2") String to, @Param("archiveStatus") int archiveStatus);

  /**
   * 根据拥有者和另一位聊天者查询会话信息
   *
   * @param owner 会话拥有者
   * @param to 收件人
   * @return 会话信息
   */
  UsermailBoxDO getUsermailBox(@Param("owner") String owner, @Param("mail2") String to);
}
