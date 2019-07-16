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

package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.domains.UsermailBlacklistDO;
import com.syswin.temail.usermail.infrastructure.domain.IUsermailBlacklistDB;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsermailBlacklistService {

  private final IUsermailBlacklistDB usermailBlacklistDB;

  private final IUsermailAdapter iUsermailAdapter;

  @Autowired
  public UsermailBlacklistService(IUsermailBlacklistDB usermailBlacklistDB, IUsermailAdapter iUsermailAdapter) {
    this.usermailBlacklistDB = usermailBlacklistDB;
    this.iUsermailAdapter = iUsermailAdapter;
  }

  /**
   * 保存单聊黑名单
   *
   * @param usermailBlacklist 黑名单参数（temailAddress:发起人 blackedAddress:被拉黑人）
   * @return 新增行数
   */
  public int save(UsermailBlacklistDO usermailBlacklist) {
    usermailBlacklist.setId(iUsermailAdapter.getUsermailBlacklistPkID());
    return usermailBlacklistDB.insertUsermailBlacklist(usermailBlacklist);
  }

  /**
   * 移除单聊黑名单
   *
   * @param usermailBlacklist 移除黑名单参数（temailAddress:发起人 blackedAddress:被移除人）
   * @return 删除行数
   */
  @Transactional
  public int remove(UsermailBlacklistDO usermailBlacklist) {
    return usermailBlacklistDB.deleteByTemailAndBlackedAddress(usermailBlacklist);
  }

  /**
   * 查询单条黑名单数据
   *
   * @param temailAddress 发起人
   * @param blackedAddress 被拉黑人
   * @return 黑名单数据（temailAddress:发起人 blackedAddress:被拉黑人）
   */
  public UsermailBlacklistDO findByAddresses(String temailAddress, String blackedAddress) {
    return usermailBlacklistDB.selectByTemailAndBlackedAddress(temailAddress, blackedAddress);
  }

  /**
   * 查询黑名单列表
   *
   * @param temailAddress 发送人
   * @return 发送人黑名单列表（temailAddress:发起人 blackedAddress:被拉黑人）
   */
  public List<UsermailBlacklistDO> findByTemailAddress(String temailAddress) {
    return usermailBlacklistDB.listUsermailBlacklists(temailAddress);
  }

  /**
   * 检查发件人是否在收件人的黑名单中
   *
   * @param from 发件人
   * @param to 收件人
   * @return int  是否在黑名单中（1:在，0:不在）
   */
  public int isInBlacklist(String from, String to) {
    // from to 对应黑名单要反过来查询，确认自己是否被[to]加入黑名单
    return usermailBlacklistDB.countByAddresses(to, from);
  }
}
