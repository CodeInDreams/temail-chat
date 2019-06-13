package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.dto.QueryTrashDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import com.syswin.temail.usermail.dto.UmQueryDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailRepo {

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
  Usermail getUsermailByMsgid(String msgid, String owner);

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
  int removeMsg(List<String> msgIds, String owner);

  /**
   * 阅后即焚
   */
  void destoryAfterRead(String owner, String msgid, int status);

  /**
   * 根据会话id批量删除消息
   */
  int batchDeleteBySessionId(String sessionId, String owner);

  /**
   * 根据msgId获取用户消息列表
   */
  List<Usermail> getUsermailListByMsgid(String msgid);

  /**
   * 根据msgIds获取消息列表
   */
  List<Usermail> getUsermailByFromToMsgIds(String from, List<String> msgIds);

  /**
   * 更新消息的回复数
   */
  void updateReplyCountAndLastReplyMsgid(String msgid, String owner, int count, String lastReplyMsgid);

  /**
   * 根据msgIds批量更新消息状态
   */
  int updateStatusByMsgIds(List<String> msgIds, String owner, int status);

  /**
   * 根据用户的消息状态批量删除消息
   */
  int removeMsgByStatus(List<TrashMailDTO> trashMails, String owner, int status);

  /**
   * 根据msgIds批量移入废纸篓
   */
  int updateStatusByTemail(List<TrashMailDTO> trashMails, String owner, int status);

  /**
   * 查询指定状态的消息列表
   */
  List<Usermail> getUsermailByStatus(QueryTrashDTO queryDto);

}