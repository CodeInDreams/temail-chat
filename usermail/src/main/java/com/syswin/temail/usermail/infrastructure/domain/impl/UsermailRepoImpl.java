package com.syswin.temail.usermail.infrastructure.domain.impl;

import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.infrastructure.domain.UsermailRepo;
import com.syswin.temail.usermail.dto.QueryTrashDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import com.syswin.temail.usermail.dto.UmQueryDTO;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsermailRepoImpl implements UsermailRepo {

  private final UsermailMapper usermailMapper;

  @Autowired
  public UsermailRepoImpl(UsermailMapper usermailMapper) {
    this.usermailMapper = usermailMapper;
  }

  /**
   * @param usermail 单聊消息
   * @description 保存单聊消息
   */
  @Override
  public void saveUsermail(Usermail usermail) {
    usermailMapper.saveUsermail(usermail);
  }

  /**
   * @param umQueryDto 消息列表查询条件
   * @description 根据用户会话id查询消息列表
   */
  @Override
  public List<Usermail> getUsermail(UmQueryDTO umQueryDto) {
    return usermailMapper.getUsermail(umQueryDto);
  }

  /**
   * @param msgid 消息id
   * @param owner 消息拥有者
   * @description 根据msgId获取消息
   */
  @Override
  public Usermail getUsermailByMsgid(String msgid, String owner) {
    return usermailMapper.getUsermailByMsgid(msgid, owner);
  }

  /**
   * @param umQueryDto 查询条件
   * @description 获取用户最新一条消息
   */
  @Override
  public List<Usermail> getLastUsermail(UmQueryDTO umQueryDto) {
    return usermailMapper.getLastUsermail(umQueryDto);
  }

  /**
   * @param umQueryDto 撤回条件
   * @description 撤回消息
   */
  @Override
  public int revertUsermail(UmQueryDTO umQueryDto) {
    return usermailMapper.revertUsermail(umQueryDto);
  }

  /**
   * @param msgIds 消息列表
   * @param owner 消息拥有者
   * @description 批量删除消息
   */
  @Override
  public int removeMsg(List<String> msgIds, String owner) {
    return usermailMapper.removeMsg(msgIds, owner);
  }

  /**
   * @param owner 消息拥有者
   * @param msgid 消息id
   * @param status 消息状态
   * @description 阅后即焚
   */
  @Override
  public void destoryAfterRead(String owner, String msgid, int status) {
    usermailMapper.destoryAfterRead(owner, msgid, status);
  }

  /**
   * @param sessionId 会话id
   * @param owner 拥有者
   * @description 根据会话id批量删除消息
   */
  @Override
  public int batchDeleteBySessionId(String sessionId, String owner) {
    return usermailMapper.batchDeleteBySessionId(sessionId, owner);
  }

  /**
   * @param msgid 消息id
   * @description 根据msgId获取用户消息列表
   */
  @Override
  public List<Usermail> getUsermailListByMsgid(String msgid) {
    return usermailMapper.getUsermailListByMsgid(msgid);
  }

  /**
   * @param from 发件人
   * @param msgIds 消息列表
   * @description 根据msgIds获取消息列表
   */
  @Override
  public List<Usermail> getUsermailByFromToMsgIds(String from, List<String> msgIds) {
    return usermailMapper.getUsermailByFromToMsgIds(from, msgIds);
  }

  /**
   * @param msgid 消息id
   * @param owner 拥有者
   * @param count 要增加的数量
   * @param lastReplyMsgid 最近的msgId
   * @description 更新消息的回复数
   */
  @Override
  public void updateReplyCountAndLastReplyMsgid(String msgid, String owner, int count, String lastReplyMsgid) {
    usermailMapper.updateReplyCountAndLastReplyMsgid(msgid, owner, count, lastReplyMsgid);
  }

  /**
   * @param msgIds 消息id列表
   * @param owner 拥有者
   * @param status 消息状态
   * @description 根据msgIds批量更新消息状态
   */
  @Override
  public int updateStatusByMsgIds(List<String> msgIds, String owner, int status) {
    return usermailMapper.updateStatusByMsgIds(msgIds, owner, status);
  }

  /**
   * @param trashMails 要删除的消息列表
   * @param owner 消息拥有者
   * @param status 消息状态
   * @description 根据用户的消息状态批量删除消息
   */
  @Override
  public int removeMsgByStatus(List<TrashMailDTO> trashMails, String owner, int status) {
    return usermailMapper.removeMsgByStatus(trashMails, owner, status);
  }

  /**
   * @param trashMails 要删除的消息列表
   * @param owner 消息拥有着
   * @param status 消息状态
   * @description 根据msgIds批量移入废纸篓
   */
  @Override
  public int updateStatusByTemail(List<TrashMailDTO> trashMails, String owner, int status) {
    return usermailMapper.updateStatusByTemail(trashMails, owner, status);
  }

  /**
   * @param queryDto 查询条件
   * @description 查询指定状态的消息列表
   */
  @Override
  public List<Usermail> getUsermailByStatus(QueryTrashDTO queryDto) {
    return usermailMapper.getUsermailByStatus(queryDto);
  }

}