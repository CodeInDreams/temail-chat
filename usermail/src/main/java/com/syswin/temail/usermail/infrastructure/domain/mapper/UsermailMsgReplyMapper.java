package com.syswin.temail.usermail.infrastructure.domain.mapper;


import com.syswin.temail.usermail.domains.UsermailMsgReply;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailMsgReplyMapper {

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
  int deleteBatchMsgReplyStatus(@Param("owner") String owner, @Param("msgIds") List<String> msgIds);

  /**
   * 根据sessionId批量删除回复消息
   */
  int batchDeleteBySessionId(@Param("sessionid") String sessionId, @Param("owner") String owner);

  /**
   * 根据父消息删除回复消息
   */
  int deleteMsgByParentIdAndOwner(@Param("owner") String owner, @Param("parentMsgIds") List<String> parentMsgIds);

  /**
   * 根据条件查询回复消息列表
   */
  UsermailMsgReply getMsgReplyByCondition(UsermailMsgReply usermailMsgReply);

  /**
   * 回复消息阅后即焚
   */
  int destoryAfterRead(@Param("owner") String owner, @Param("msgid") String msgid, @Param("status") int status);

  /**
   * 根据父消息批量修改消息状态
   */
  int batchUpdateByParentMsgIds(@Param("owner") String owner, @Param("parentMsgIds") List<String> parentMsgIds,
      @Param("status") int status);

  /**
   * 根据用户和消息状态批量删除消息
   */
  int batchDeleteByStatus(@Param("owner") String owner, @Param("status") int status);

  /**
   * 获取最近一条回复消息
   */
  UsermailMsgReply getLastUsermailReply(@Param("parentMsgid") String parentMsgid, @Param("owner") String owner,
      @Param("status") int status);

  /**
   * 撤回用户回复的消息
   */
  int revertUsermailReply(UsermailMsgReply usermailMsgReply);
}