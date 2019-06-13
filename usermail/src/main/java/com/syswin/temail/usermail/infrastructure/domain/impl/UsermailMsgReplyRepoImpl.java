package com.syswin.temail.usermail.infrastructure.domain.impl;

import com.syswin.temail.usermail.domains.UsermailMsgReply;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import com.syswin.temail.usermail.infrastructure.domain.UsermailMsgReplyRepo;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailMsgReplyMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsermailMsgReplyRepoImpl implements UsermailMsgReplyRepo {

  private final UsermailMsgReplyMapper usermailMsgReplyMapper;

  @Autowired
  public UsermailMsgReplyRepoImpl(UsermailMsgReplyMapper usermailMsgReplyMapper) {
    this.usermailMsgReplyMapper = usermailMsgReplyMapper;
  }

  /**
   * @param record 回复消息信息
   * @description 新增回复消息
   */
  @Override
  public int insert(UsermailMsgReply record) {
    return usermailMsgReplyMapper.insert(record);
  }

  /**
   * @param dto 查询消息条件
   * @description 查询指定消息的回复消息列表
   */
  @Override
  public List<UsermailMsgReply> getMsgReplys(QueryMsgReplyDTO dto) {
    return usermailMsgReplyMapper.getMsgReplys(dto);
  }

  /**
   * @param owner 消息拥有者
   * @param msgIds 消息id列表
   * @description 根据msgId批量删除回复消息
   */
  @Override
  public int deleteBatchMsgReplyStatus(String owner, List<String> msgIds) {
    return usermailMsgReplyMapper.deleteBatchMsgReplyStatus(owner, msgIds);
  }

  /**
   * @param sessionId 会话id
   * @param owner 拥有者
   * @description 根据sessionId批量删除回复消息
   */
  @Override
  public int batchDeleteBySessionId(String sessionId, String owner) {
    return usermailMsgReplyMapper.batchDeleteBySessionId(sessionId, owner);
  }

  /**
   * @param owner 拥有者
   * @param parentMsgIds 父消息id列表
   * @description 根据父消息删除回复消息
   */
  @Override
  public int deleteMsgByParentIdAndOwner(String owner, List<String> parentMsgIds) {
    return usermailMsgReplyMapper.deleteMsgByParentIdAndOwner(owner, parentMsgIds);
  }

  /**
   * @param usermailMsgReply 查询回复消息列表条件
   * @description 根据条件查询回复消息列表
   */
  @Override
  public UsermailMsgReply getMsgReplyByCondition(UsermailMsgReply usermailMsgReply) {
    return usermailMsgReplyMapper.getMsgReplyByCondition(usermailMsgReply);
  }

  /**
   * @param owner 拥有者
   * @param msgid 消息id
   * @param status 消息状态
   * @description 回复消息阅后即焚
   */
  @Override
  public int destoryAfterRead(String owner, String msgid, int status) {
    return usermailMsgReplyMapper.destoryAfterRead(owner, msgid, status);
  }

  /**
   * @param owner 拥有者
   * @param parentMsgIds 父消息id列表
   * @param status 消息状态
   * @description 根据父消息批量修改消息状态
   */
  @Override
  public int batchUpdateByParentMsgIds(String owner, List<String> parentMsgIds, int status) {
    return usermailMsgReplyMapper.batchUpdateByParentMsgIds(owner, parentMsgIds, status);
  }

  /**
   * @param owner 拥有者
   * @param status 消息状态
   * @description 根据用户和消息状态批量删除消息
   */
  @Override
  public int batchDeleteByStatus(String owner, int status) {
    return usermailMsgReplyMapper.batchDeleteByStatus(owner, status);
  }

  /**
   * @param parentMsgid 父消息id
   * @param owner 拥有者
   * @param status 消息状态
   * @description 获取最近一条回复消息
   */
  @Override
  public UsermailMsgReply getLastUsermailReply(String parentMsgid, String owner, int status) {
    return usermailMsgReplyMapper.getLastUsermailReply(parentMsgid, owner, status);
  }

  /**
   * @param usermailMsgReply 撤回的消息信息
   * @description 撤回用户回复的消息
   */
  @Override
  public int revertUsermailReply(UsermailMsgReply usermailMsgReply) {
    return usermailMsgReplyMapper.revertUsermailReply(usermailMsgReply);
  }
}