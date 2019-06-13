package com.syswin.temail.usermail.infrastructure.domain.mapper;

import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.dto.QueryTrashDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import com.syswin.temail.usermail.dto.UmQueryDTO;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailMapper {

  /**
   * 保存单聊消息
   */
  void saveUsermail(Usermail usermail);

  /**
   * 根据用户会话id查询消息列表
   */
  List<Usermail> getUsermail(UmQueryDTO umQueryDto);

  /**
   * 根据msgId获取消息
   */
  Usermail getUsermailByMsgid(@Param("msgid") String msgid, @Param("owner") String owner);

  /**
   * 获取用户最新一条消息
   */
  List<Usermail> getLastUsermail(UmQueryDTO umQueryDto);

  /**
   * 撤回消息
   */
  int revertUsermail(UmQueryDTO umQueryDto);

  /**
   * 批量删除消息
   */
  int removeMsg(@Param("msgIds") List<String> msgIds, @Param("owner") String owner);

  /**
   * 阅后即焚
   */
  void destoryAfterRead(@Param("owner") String owner, @Param("msgid") String msgid, @Param("status") int status);

  /**
   * 根据会话id批量删除消息
   */
  int batchDeleteBySessionId(@Param("sessionid") String sessionId, @Param("owner") String owner);

  /**
   * 根据msgId获取用户消息列表
   */
  List<Usermail> getUsermailListByMsgid(@Param("msgid") String msgid);

  /**
   * 根据msgIds获取消息列表
   */
  List<Usermail> getUsermailByFromToMsgIds(@Param("from") String from, @Param("msgIds") List<String> msgIds);

  /**
   * 更新消息的回复数
   */
  void updateReplyCountAndLastReplyMsgid(@Param("msgid") String msgid, @Param("owner") String owner,
      @Param("count") int count, @Param("lastReplyMsgid") String lastReplyMsgid);

  /**
   * 根据msgIds批量更新消息状态
   */
  int updateStatusByMsgIds(@Param("msgIds") List<String> msgIds, @Param("owner") String owner,
      @Param("status") int status);

  /**
   * 根据用户的消息状态批量删除消息
   */
  int removeMsgByStatus(@Param("trashMails") List<TrashMailDTO> trashMails, @Param("owner") String owner,
      @Param("status") int status);

  /**
   * 根据msgIds批量移入废纸篓
   */
  int updateStatusByTemail(@Param("trashMails") List<TrashMailDTO> trashMails, @Param("owner") String owner,
      @Param("status") int status);

  /**
   * 查询指定状态的消息列表
   */
  List<Usermail> getUsermailByStatus(QueryTrashDTO queryDto);

}