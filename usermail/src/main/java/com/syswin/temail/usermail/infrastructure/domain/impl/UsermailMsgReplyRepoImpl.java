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
  public List<UsermailMsgReplyDO> listMsgReplys(QueryMsgReplyDTO dto) {
    return usermailMsgReplyMapper.listMsgReplys(dto);
  }

  /**
   * 根据msgId批量删除回复消息
   *
   * @param owner 消息拥有者
   * @param msgIds 消息id列表
   * @return 删除的数量
   */
  @Override
  public int deleteMsgReplysByMsgIds(String owner, List<String> msgIds) {
    return usermailMsgReplyMapper.deleteMsgReplysByMsgIds(owner, msgIds);
  }

  /**
   * 根据sessionId批量删除回复消息
   *
   * @param sessionId 会话id
   * @param owner 拥有者
   * @return 删除的数量
   */
  @Override
  public int deleteMsgReplysBySessionId(String sessionId, String owner) {
    return usermailMsgReplyMapper.deleteMsgReplysBySessionId(sessionId, owner);
  }

  /**
   * 根据父消息删除回复消息
   *
   * @param owner 拥有者
   * @param parentMsgIds 父消息id列表
   * @return 删除的数量
   */
  @Override
  public int deleteMsgReplysByParentIds(String owner, List<String> parentMsgIds) {
    return usermailMsgReplyMapper.deleteMsgReplysByParentIds(owner, parentMsgIds);
  }

  /**
   * 根据条件查询回复消息列表
   *
   * @param usermailMsgReply 查询回复消息列表条件
   * @return 回复消息信息
   */
  @Override
  public UsermailMsgReplyDO selectMsgReplyByCondition(UsermailMsgReplyDO usermailMsgReply) {
    return usermailMsgReplyMapper.selectMsgReplyByCondition(usermailMsgReply);
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
  public int updateDestroyAfterRead(String owner, String msgid, int status) {
    return usermailMsgReplyMapper.updateDestroyAfterRead(owner, msgid, status, TemailStatus.STATUS_NORMAL_0);
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
  public int updateMsgReplysByParentIds(String owner, List<String> parentMsgIds, int status) {
    return usermailMsgReplyMapper.updateMsgReplysByParentIds(owner, parentMsgIds, status);
  }

  /**
   * 根据用户和消息状态批量删除消息
   *
   * @param owner 拥有者
   * @param status 消息状态
   * @return 删除的数量
   */
  @Override
  public int deleteMsgReplysByStatus(String owner, int status) {
    return usermailMsgReplyMapper.deleteMsgReplysByStatus(owner, status);
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
  public UsermailMsgReplyDO selectLastUsermailReply(String parentMsgid, String owner, int status) {
    return usermailMsgReplyMapper.selectLastUsermailReply(parentMsgid, owner, status);
  }

  /**
   * 撤回用户回复的消息
   *
   * @param usermailMsgReply 撤回的消息信息
   * @return 撤回的数量
   */
  @Override
  public int updateRevertUsermailReply(UsermailMsgReplyDO usermailMsgReply) {
    return usermailMsgReplyMapper.updateRevertUsermailReply(usermailMsgReply, TemailStatus.STATUS_NORMAL_0);
  }
}