/*
 * MIT License
 *
 * Copyright (c) 2019 Syswin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.syswin.temail.usermail.infrastructure.domain.impl;

import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.dto.QueryTrashDTO;
import com.syswin.temail.usermail.dto.RevertMailDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import com.syswin.temail.usermail.dto.UmQueryDTO;
import com.syswin.temail.usermail.infrastructure.domain.UsermailRepo;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailMapper;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class UsermailRepoImpl implements UsermailRepo {

  private final UsermailMapper usermailMapper;

  @Autowired
  public UsermailRepoImpl(UsermailMapper usermailMapper) {
    this.usermailMapper = usermailMapper;
  }

  /**
   * 保存单聊消息
   *
   * @param usermail 单聊消息
   */
  @Override
  public void insertUsermail(UsermailDO usermail) {
    usermailMapper.insertUsermail(usermail);
  }

  /**
   * 根据用户会话id查询消息列表
   *
   * @param umQueryDto 消息列表查询条件
   * @return 消息列表
   */
  @Override
  public List<UsermailDO> listUsermails(UmQueryDTO umQueryDto) {
    return usermailMapper.listUsermails(umQueryDto);
  }

  /**
   * 根据msgId获取消息
   *
   * @param msgid 消息id
   * @param owner 消息拥有者
   * @return 消息信息
   */
  @Override
  public UsermailDO selectByMsgidAndOwner(String msgid, String owner) {
    return usermailMapper.selectByMsgidAndOwner(msgid, owner);
  }

  /**
   * 获取用户最新一条消息
   *
   * @param umQueryDto 查询条件
   * @return 消息列表
   */
  @Override
  public List<UsermailDO> listLastUsermails(UmQueryDTO umQueryDto) {
    return usermailMapper.listLastUsermails(umQueryDto);
  }

  /**
   * 撤回消息
   *
   * @param revertMail 撤回条件
   * @return 撤回的数量
   */
  @Override
  public int countRevertUsermail(RevertMailDTO revertMail) {
    return usermailMapper.countRevertUsermail(revertMail);
  }

  /**
   * 批量删除消息
   *
   * @param msgIds 消息列表
   * @param owner 消息拥有者
   * @return 删除的数量
   */
  @Override
  public int deleteMsg(List<String> msgIds, String owner) {
    return usermailMapper.deleteMsg(msgIds, owner);
  }

  /**
   * 阅后即焚
   *  @param owner 消息拥有者
   * @param msgid 消息id
   * @param status 消息状态
   */
  @Override
  public void updateDestroyAfterReadStatus(String owner, String msgid, int status) {
    usermailMapper.updateDestroyAfterReadStatus(owner, msgid, status);
  }

  /**
   * 根据会话id批量删除消息
   *
   * @param sessionId 会话id
   * @param owner 拥有者
   * @return 删除的数量
   */
  @Override
  public int deleteBySessionIdAndOwner(String sessionId, String owner) {
    return usermailMapper.deleteBySessionIdAndOwner(sessionId, owner);
  }

  /**
   * 根据msgId获取用户消息列表
   *
   * @param msgid 消息id
   * @return 消息列表
   */
  @Override
  public List<UsermailDO> listUsermailsByMsgid(String msgid) {
    return usermailMapper.listUsermailsByMsgid(msgid);
  }

  /**
   * 根据msgIds获取消息列表
   *
   * @param from 发件人
   * @param msgIds 消息列表
   * @return 消息列表
   */
  @Override
  public List<UsermailDO> listUsermailsByFromToMsgIds(String from, List<String> msgIds) {
    return usermailMapper.listUsermailsByFromToMsgIds(from, msgIds);
  }

  /**
   * 更新消息的回复数
   *
   * @param msgid 消息id
   * @param owner 拥有者
   * @param count 要增加的数量
   * @param lastReplyMsgid 最近的msgId
   */
  @Override
  public void updateReplyCountAndLastReplyMsgid(String msgid, String owner, int count, String lastReplyMsgid) {
    usermailMapper.updateReplyCountAndLastReplyMsgid(msgid, owner, count, lastReplyMsgid);
  }

  /**
   * 根据msgIds批量更新消息状态
   *
   * @param msgIds 消息id列表
   * @param owner 拥有者
   * @param status 消息状态
   * @return 更新的数量
   */
  @Override
  public int updateStatusByMsgIds(List<String> msgIds, String owner, int status) {
    return usermailMapper.updateStatusByMsgIds(msgIds, owner, status);
  }

  /**
   * 根据用户的消息状态批量删除消息
   *
   * @param trashMails 要删除的消息列表
   * @param owner 消息拥有者
   * @param status 消息状态
   * @return 删除的数量
   */
  @Override
  public int deleteMsgByStatus(List<TrashMailDTO> trashMails, String owner, int status) {
    return usermailMapper.deleteMsgByStatus(trashMails, owner, status);
  }

  /**
   * 还原废纸篓消息
   *
   * @param trashMails 废纸篓消息
   * @param owner 消息拥有着
   * @param status 消息状态
   * @return 更新的数量
   */
  @Override
  public int updateRevertMsgFromTrashStatus(List<TrashMailDTO> trashMails, String owner, int status) {
    return usermailMapper.updateRevertMsgFromTrashStatus(trashMails, owner, status, TemailStatus.STATUS_TRASH_4);
  }

  /**
   * 查询指定状态的消息列表
   *
   * @param queryDto 查询条件
   * @return 消息列表
   */
  @Override
  public List<UsermailDO> listUsermailsByStatus(QueryTrashDTO queryDto) {
    return usermailMapper.listUsermailsByStatus(queryDto);
  }

  /**
   * 清除指定时间以前的数据，并限制清除量
   *
   * @param createTime 指定时间点
   * @param batchNum 最多删除的数量
   * @return 实际清除数量
   */
  @Override
  public int deleteMsgLessThan(Timestamp createTime, int batchNum) {
    return usermailMapper.deleteUseMsgLessThan(createTime, batchNum);
  }

  /**
   * 分页清理指定域数据
   *
   * @param domain 域
   * @param pageSize 页面大小
   */
  @Transactional
  @Override
  public void removeDomain(String domain, int pageSize) throws InterruptedException {

    int count = 0;
    do {
      count = usermailMapper.removeDomain(domain, pageSize);
      Thread.sleep(1000);
    } while (count != 0);

  }

}