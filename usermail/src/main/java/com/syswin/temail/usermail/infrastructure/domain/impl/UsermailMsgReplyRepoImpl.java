package com.syswin.temail.usermail.infrastructure.domain.impl;

import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.domains.UsermailMsgReplyDO;
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
   * 新增回复消息
   *
   * @param record 回复消息信息
   * @return 插入的数量
   */
  @Override
  public int insert(UsermailMsgReplyDO record) {
    return usermailMsgReplyMapper.insert(record);
  }

  /**
   * 查询指定消息的回复消息列表
   *
   * @param dto 查询消息条件
   * @return 回复消息列表
   */
  @Override
  public List<UsermailMsgReplyDO> getMsgReplys(QueryMsgReplyDTO dto) {
    return usermailMsgReplyMapper.getMsgReplys(dto);
  }

  /**
   * 根据msgId批量删除回复消息
   *
   * @param owner 消息拥有者
   * @param msgIds 消息id列表
   * @return 删除的数量
   */
  @Override
  public int deleteBatchMsgReplyStatus(String owner, List<String> msgIds) {
    return usermailMsgReplyMapper.deleteBatchMsgReplyStatus(owner, msgIds);
  }

  /**
   * 根据sessionId批量删除回复消息
   *
   * @param sessionId 会话id
   * @param owner 拥有者
   * @return 删除的数量
   */
  @Override
  public int batchDeleteBySessionId(String sessionId, String owner) {
    return usermailMsgReplyMapper.batchDeleteBySessionId(sessionId, owner);
  }

  /**
   * 根据父消息删除回复消息
   *
   * @param owner 拥有者
   * @param parentMsgIds 父消息id列表
   * @return 删除的数量
   */
  @Override
  public int deleteMsgByParentIdAndOwner(String owner, List<String> parentMsgIds) {
    return usermailMsgReplyMapper.deleteMsgByParentIdAndOwner(owner, parentMsgIds);
  }

  /**
   * 根据条件查询回复消息列表
   *
   * @param usermailMsgReply 查询回复消息列表条件
   * @return 回复消息信息
   */
  @Override
  public UsermailMsgReplyDO getMsgReplyByCondition(UsermailMsgReplyDO usermailMsgReply) {
    return usermailMsgReplyMapper.getMsgReplyByCondition(usermailMsgReply);
  }

  /**
   * 回复消息阅后即焚
   *
   * @param owner 拥有者
   * @param msgid 消息id
   * @param status 消息状态
   * @return 更新的数量
   */
  @Override
  public int destroyAfterRead(String owner, String msgid, int status) {
    return usermailMsgReplyMapper.destroyAfterRead(owner, msgid, status, TemailStatus.STATUS_NORMAL_0);
  }

  /**
   * 根据父消息批量修改消息状态
   *
   * @param owner 拥有者
   * @param parentMsgIds 父消息id列表
   * @param status 消息状态
   * @return 更新的数量
   */
  @Override
  public int batchUpdateByParentMsgIds(String owner, List<String> parentMsgIds, int status) {
    return usermailMsgReplyMapper.batchUpdateByParentMsgIds(owner, parentMsgIds, status);
  }

  /**
   * 根据用户和消息状态批量删除消息
   *
   * @param owner 拥有者
   * @param status 消息状态
   * @return 删除的数量
   */
  @Override
  public int batchDeleteByStatus(String owner, int status) {
    return usermailMsgReplyMapper.batchDeleteByStatus(owner, status);
  }

  /**
   * 获取最近一条回复消息
   *
   * @param parentMsgid 父消息id
   * @param owner 拥有者
   * @param status 消息状态
   * @return 回复消息的信息
   */
  @Override
  public UsermailMsgReplyDO getLastUsermailReply(String parentMsgid, String owner, int status) {
    return usermailMsgReplyMapper.getLastUsermailReply(parentMsgid, owner, status);
  }

  /**
   * 撤回用户回复的消息
   *
   * @param usermailMsgReply 撤回的消息信息
   * @return 撤回的数量
   */
  @Override
  public int revertUsermailReply(UsermailMsgReplyDO usermailMsgReply) {
    return usermailMsgReplyMapper.revertUsermailReply(usermailMsgReply, TemailStatus.STATUS_NORMAL_0);
  }
}