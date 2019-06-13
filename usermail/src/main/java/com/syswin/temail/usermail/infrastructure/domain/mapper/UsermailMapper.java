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
   * @param usermail 单聊消息
   * @description 保存单聊消息
   */
  void saveUsermail(Usermail usermail);

  /**
   * @param umQueryDto 消息列表查询条件
   * @description 根据用户会话id查询消息列表
   */
  List<Usermail> getUsermail(UmQueryDTO umQueryDto);

  /**
   * @param msgid 消息id
   * @param owner 消息拥有者
   * @description 根据msgId获取消息
   */
  Usermail getUsermailByMsgid(@Param("msgid") String msgid, @Param("owner") String owner);

  /**
   * @param umQueryDto 查询条件
   * @description 获取用户最新一条消息
   */
  List<Usermail> getLastUsermail(UmQueryDTO umQueryDto);

  /**
   * @param umQueryDto 撤回条件
   * @description 撤回消息
   */
  int revertUsermail(UmQueryDTO umQueryDto);

  /**
   * @param msgIds 消息列表
   * @param owner 消息拥有者
   * @description 批量删除消息
   */
  int removeMsg(@Param("msgIds") List<String> msgIds, @Param("owner") String owner);

  /**
   * @param owner 消息拥有者
   * @param msgid 消息id
   * @param status 消息状态
   * @description 阅后即焚
   */
  void destoryAfterRead(@Param("owner") String owner, @Param("msgid") String msgid, @Param("status") int status);

  /**
   * @param sessionId 会话id
   * @param owner 拥有者
   * @description 根据会话id批量删除消息
   */
  int batchDeleteBySessionId(@Param("sessionid") String sessionId, @Param("owner") String owner);

  /**
   * @param msgid 消息id
   * @description 根据msgId获取用户消息列表
   */
  List<Usermail> getUsermailListByMsgid(@Param("msgid") String msgid);

  /**
   * @param from 发件人
   * @param msgIds 消息列表
   * @description 根据msgIds获取消息列表
   */
  List<Usermail> getUsermailByFromToMsgIds(@Param("from") String from, @Param("msgIds") List<String> msgIds);

  /**
   * @param msgid 消息id
   * @param owner 拥有者
   * @param count 要增加的数量
   * @param lastReplyMsgid 最近的msgId
   * @description 更新消息的回复数
   */
  void updateReplyCountAndLastReplyMsgid(@Param("msgid") String msgid, @Param("owner") String owner,
      @Param("count") int count, @Param("lastReplyMsgid") String lastReplyMsgid);

  /**
   * @param msgIds 消息id列表
   * @param owner 拥有者
   * @param status 消息状态
   * @description 根据msgIds批量更新消息状态
   */
  int updateStatusByMsgIds(@Param("msgIds") List<String> msgIds, @Param("owner") String owner,
      @Param("status") int status);

  /**
   * @param trashMails 要删除的消息列表
   * @param owner 消息拥有者
   * @param status 消息状态
   * @description 根据用户的消息状态批量删除消息
   */
  int removeMsgByStatus(@Param("trashMails") List<TrashMailDTO> trashMails, @Param("owner") String owner,
      @Param("status") int status);

  /**
   * @param trashMails 要删除的消息列表
   * @param owner 消息拥有着
   * @param status 消息状态
   * @description 根据msgIds批量移入废纸篓
   */
  int updateStatusByTemail(@Param("trashMails") List<TrashMailDTO> trashMails, @Param("owner") String owner,
      @Param("status") int status);

  /**
   * @param queryDto 查询条件
   * @description 查询指定状态的消息列表
   */
  List<Usermail> getUsermailByStatus(QueryTrashDTO queryDto);

}