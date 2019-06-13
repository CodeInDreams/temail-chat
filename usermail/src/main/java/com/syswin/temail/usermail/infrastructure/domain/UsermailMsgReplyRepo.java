package com.syswin.temail.usermail.infrastructure.domain;


import com.syswin.temail.usermail.domains.UsermailMsgReply;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import java.util.List;

public interface UsermailMsgReplyRepo {

  /**
   * 新增回复消息
   */
  int insert(UsermailMsgReply record);

  /**
   * 查询指定消息的回复消息列表
   */
  List<UsermailMsgReply> getMsgReplys(QueryMsgReplyDTO dto);

  /**
   * 根据msgId批量删除回复消息
   */
  int deleteBatchMsgReplyStatus(String owner, List<String> msgIds);

  /**
   * 根据sessionId批量删除回复消息
   */
  int batchDeleteBySessionId(String sessionId, String owner);

  /**
   * 根据父消息删除回复消息
   */
  int deleteMsgByParentIdAndOwner(String owner, List<String> parentMsgIds);

  /**
   * 根据条件查询回复消息列表
   */
  UsermailMsgReply getMsgReplyByCondition(UsermailMsgReply usermailMsgReply);

  /**
   * 回复消息阅后即焚
   */
  int destoryAfterRead(String owner, String msgid, int status);

  /**
   * 根据父消息批量修改消息状态
   */
  int batchUpdateByParentMsgIds(String owner, List<String> parentMsgIds, int status);

  /**
   * 根据用户和消息状态批量删除消息
   */
  int batchDeleteByStatus(String owner, int status);

  /**
   * 获取最近一条回复消息
   */
  UsermailMsgReply getLastUsermailReply(String parentMsgid, String owner, int status);

  /**
   * 撤回用户回复的消息
   */
  int revertUsermailReply(UsermailMsgReply usermailMsgReply);
}