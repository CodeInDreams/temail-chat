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

import com.syswin.temail.usermail.domains.UsermailBlacklistDO;
import com.syswin.temail.usermail.infrastructure.domain.IUsermailBlacklistDB;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailBlacklistMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsermailBlacklistDBImpl implements IUsermailBlacklistDB {

  private final UsermailBlacklistMapper usermailBlacklistMapper;

  @Autowired
  public UsermailBlacklistDBImpl(UsermailBlacklistMapper usermailBlacklistMapper) {
    this.usermailBlacklistMapper = usermailBlacklistMapper;
  }

  /**
   * 新增黑名单
   *
   * @param usermailBlacklist 黑名单信息
   * @return 插入的数量
   */
  @Override
  public int insertUsermailBlacklist(UsermailBlacklistDO usermailBlacklist) {
    return usermailBlacklistMapper.insertUsermailBlacklist(usermailBlacklist);
  }

  /**
   * 根据发起者和被拉黑者删除黑名单信息
   *
   * @param usermailBlacklist 黑名单信息
   * @return 删除的数量
   */
  @Override
  public int deleteByTemailAndBlackedAddress(UsermailBlacklistDO usermailBlacklist) {
    return usermailBlacklistMapper.deleteByTemailAndBlackedAddress(usermailBlacklist);
  }

  /**
   * 根据发起者和被拉黑者查找黑名单信息
   *
   * @param temailAddress 发起者地址
   * @param blackedAddress 被拉黑地址
   * @return 黑名单信息
   */
  @Override
  public UsermailBlacklistDO selectByTemailAndBlackedAddress(String temailAddress, String blackedAddress) {
    return usermailBlacklistMapper.selectByTemailAndBlackedAddress(temailAddress, blackedAddress);
  }

  /**
   * 查找当前地址的黑名单列表
   *
   * @param temailAddress 当前地址
   * @return 黑名单列表
   */
  @Override
  public List<UsermailBlacklistDO> listUsermailBlacklists(String temailAddress) {
    return usermailBlacklistMapper.listUsermailBlacklists(temailAddress);
  }

  /**
   * 判断收件人是否在发件人黑名单(0 : 不在 ; 1 : 在)
   *
   * @param temailAddress 当前地址
   * @param blackedAddress 被拉黑地址
   * @return 黑名单数量
   */
  @Override
  public int countByAddresses(String temailAddress, String blackedAddress) {
    return usermailBlacklistMapper.countByAddresses(temailAddress, blackedAddress);
  }

  /**
   * 分页清理指定域数据
   *
   * @param domain 域
   * @param pageSize 页面大小
   * @return 实际清除数量
   */
  @Override
  public int deleteDomain(String domain, int pageSize) {
    return usermailBlacklistMapper.deleteDomain(domain, pageSize);
  }

}