package com.syswin.temail.usermail.infrastructure.domain.mapper;

import com.syswin.temail.usermail.domains.UsermailBox;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailBoxMapper {

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
  List<UsermailBox> getUsermailBoxByOwner(@Param("owner") String mail, @Param("archiveStatus") int archiveStatus);

  /**
   * @param from 发件人
   * @param to 收件人
   * @description 删除指定会话
   */
  int deleteByOwnerAndTo(@Param("owner") String from, @Param("mail2") String to);

  /**
   * @param from 发件人
   * @param to 收件人
   * @description 根据会话拥有者和另一位聊天者查询会话列表
   */
  List<UsermailBox> selectByOwnerAndTo(@Param("owner") String from, @Param("mail2") String to);

  /**
   * @param from 发件人
   * @param to 收件人
   * @param archiveStatus 归档状态
   * @description 更新会话归档状态
   */
  int updateArchiveStatus(@Param("owner") String from, @Param("mail2") String to, @Param("archiveStatus") int archiveStatus);

  /**
   * @param owner 会话拥有者
   * @param to 收件人
   * @description 根据拥有者和另一位聊天者查询会话信息
   */
  UsermailBox selectUsermailBox(@Param("owner") String owner, @Param("mail2") String to);
}
