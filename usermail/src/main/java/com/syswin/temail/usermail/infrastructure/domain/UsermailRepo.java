package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.dto.QueryTrashDTO;
import com.syswin.temail.usermail.dto.RevertMailDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import com.syswin.temail.usermail.dto.UmQueryDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailRepo {

  /**
   * 保存单聊消息
   *
   * @param usermail 单聊消息
   */
  void saveUsermail(Usermail usermail);

  /**
   * 根据用户会话id查询消息列表
   *
   * @param umQueryDto 消息列表查询条件
   * @return 消息列表
   */
  List<Usermail> getUsermail(UmQueryDTO umQueryDto);

  /**
   * 根据msgId获取消息
   *
   * @param msgid 消息id
   * @param owner 消息拥有者
   * @return 消息信息
   */
  Usermail getUsermailByMsgid(String msgid, String owner);

  /**
   * 获取用户最新一条消息
   *
   * @param umQueryDto 查询条件
   * @return 消息列表
   */
  List<Usermail> getLastUsermail(UmQueryDTO umQueryDto);

  /**
   * 撤回消息
   *
   * @param revertMail 撤回条件
   * @return 撤回的数量
   */
  int revertUsermail(RevertMailDTO revertMail);

  /**
   * 批量删除消息
   *
   * @param msgIds 消息列表
   * @param owner 消息拥有者
   * @return 删除的数量
   */
  int removeMsg(List<String> msgIds, String owner);

  /**
   * 阅后即焚
   *
   * @param owner 消息拥有者
   * @param msgid 消息id
   * @param status 消息状态
   */
  void destoryAfterRead(String owner, String msgid, int status);

  /**
   * 根据会话id批量删除消息
   *
   * @param sessionId 会话id
   * @param owner 拥有者
   * @return 删除的数量
   */
  int batchDeleteBySessionId(String sessionId, String owner);

  /**
   * 根据msgId获取用户消息列表
   *
   * @param msgid 消息id
   * @return 消息列表
   */
  List<Usermail> getUsermailListByMsgid(String msgid);

  /**
   * 根据msgIds获取消息列表
   *
   * @param from 发件人
   * @param msgIds 消息列表
   * @return 消息列表
   */
  List<Usermail> getUsermailByFromToMsgIds(String from, List<String> msgIds);

  /**
   * 更新消息的回复数
   *
   * @param msgid 消息id
   * @param owner 拥有者
   * @param count 要增加的数量
   * @param lastReplyMsgid 最近的msgId
   */
  void updateReplyCountAndLastReplyMsgid(String msgid, String owner, int count, String lastReplyMsgid);

  /**
   * 根据msgIds批量更新消息状态
   *
   * @param msgIds 消息id列表
   * @param owner 拥有者
   * @param status 消息状态
   * @return 更新的数量
   */
  int updateStatusByMsgIds(List<String> msgIds, String owner, int status);

  /**
   * 根据用户的消息状态批量删除消息
   *
   * @param trashMails 要删除的消息列表
   * @param owner 消息拥有者
   * @param status 消息状态
   * @return 删除的数量
   */
  int removeMsgByStatus(List<TrashMailDTO> trashMails, String owner, int status);

  /**
   * 根据msgIds批量移入废纸篓
   *
   * @param trashMails 要删除的消息列表
   * @param owner 消息拥有着
   * @param status 消息状态
   * @return 更新的数量
   */
  int updateStatusByTemail(List<TrashMailDTO> trashMails, String owner, int status);

  /**
   * 查询指定状态的消息列表
   *
   * @param queryDto 查询条件
   * @return 消息列表
   */
  List<Usermail> getUsermailByStatus(QueryTrashDTO queryDto);

}