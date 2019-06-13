package com.syswin.temail.usermail.infrastructure.domain.mapper;

import com.syswin.temail.usermail.domains.UsermailBox;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailBoxMapper {

  /**
   * 新增单聊会话信息
   */
  void saveUsermailBox(UsermailBox usermailBox);

  /**
   * 查找当前用户的会话列表
   */
  List<UsermailBox> getUsermailBoxByOwner(@Param("owner") String mail, @Param("archiveStatus") int archiveStatus);

  /**
   * 删除指定会话
   */
  int deleteByOwnerAndTo(@Param("owner") String from, @Param("mail2") String to);

  /**
   * 根据会话拥有者和另一位聊天者查询会话列表
   */
  List<UsermailBox> selectByOwnerAndTo(@Param("owner") String from, @Param("mail2") String to);

  /**
   * 更新会话归档状态
   */
  int updateArchiveStatus(@Param("owner") String from, @Param("mail2") String to, @Param("archiveStatus") int archiveStatus);

  /**
   * 根据拥有者和另一位聊天者查询会话信息
   */
  UsermailBox selectUsermailBox(@Param("owner") String owner, @Param("mail2") String to);
}
