package com.syswin.temail.usermail.infrastructure.domain.mapper;


import com.syswin.temail.usermail.domains.UsermailMsgReplyDO;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailMsgReplyMapper {

  /**
   * 新增回复消息
   *
   * @param record 回复消息信息
   * @return 插入的数量
   */
  int insert(UsermailMsgReplyDO record);

  /**
   * 查询指定消息的回复消息列表
   *
   * @param dto 查询消息条件
   * @return 回复消息列表
   */
  List<UsermailMsgReplyDO> listMsgReplys(QueryMsgReplyDTO dto);

  /**
   * 根据msgId批量删除回复消息
   *
   * @param owner 消息拥有者
   * @param msgIds 消息id列表
   * @return 删除的数量
   */
  int deleteMsgReplysByMsgIds(@Param("owner") String owner, @Param("msgIds") List<String> msgIds);

  /**
   * 根据sessionId批量删除回复消息
   *
   * @param sessionId 会话id
   * @param owner 拥有者
   * @return 删除的数量
   */
  int deleteMsgReplysBySessionId(@Param("sessionid") String sessionId, @Param("owner") String owner);

  /**
   * 根据父消息删除回复消息
   *
   * @param owner 拥有者
   * @param parentMsgIds 父消息id列表
   * @return 删除的数量
   */
  int deleteMsgReplysByParentIds(@Param("owner") String owner, @Param("parentMsgIds") List<String> parentMsgIds);

  /**
   * 根据条件查询回复消息列表
   *
   * @param usermailMsgReply 查询回复消息列表条件
   * @return 回复消息信息
   */
  UsermailMsgReplyDO selectMsgReplyByCondition(UsermailMsgReplyDO usermailMsgReply);

  /**
   * 回复消息阅后即焚
   *
   * @param owner 拥有者
   * @param msgid 消息id
   * @param status 消息状态
   * @param originalStatus 消息初始状态
   * @return 更新的数量
   */
  int updateDestroyAfterRead(@Param("owner") String owner, @Param("msgid") String msgid, @Param("status") int status,
      @Param("originalStatus") int originalStatus);

  /**
   * 根据父消息批量修改消息状态
   *
   * @param owner 拥有者
   * @param parentMsgIds 父消息id列表
   * @param status 消息状态
   * @return 更新的数量
   */
  int updateMsgReplysByParentIds(@Param("owner") String owner, @Param("parentMsgIds") List<String> parentMsgIds,
      @Param("status") int status);

  /**
   * 根据用户和消息状态批量删除消息
   *
   * @param owner 拥有者
   * @param status 消息状态
   * @return 删除的数量
   */
  int deleteMsgReplysByStatus(@Param("owner") String owner, @Param("status") int status);

  /**
   * 获取最近一条回复消息
   *
   * @param parentMsgid 父消息id
   * @param owner 拥有者
   * @param status 消息状态
   * @return 回复消息的信息
   */
  UsermailMsgReplyDO selectLastUsermailReply(@Param("parentMsgid") String parentMsgid, @Param("owner") String owner,
      @Param("status") int status);

  /**
   * 撤回用户回复的消息
   *
   * @param usermailMsgReply 撤回的消息信息
   * @param originalStatus 当前消息状态
   * @return 撤回的数量
   */
  int updateRevertUsermailReply(@Param("usermailMsgReply") UsermailMsgReplyDO usermailMsgReply,
      @Param("originalStatus") int originalStatus);
}