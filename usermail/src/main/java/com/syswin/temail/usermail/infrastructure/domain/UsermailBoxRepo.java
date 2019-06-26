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

package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.UsermailBoxDO;
import java.util.List;

public interface UsermailBoxRepo {

  /**
   * 新增单聊会话信息
   *
   * @param usermailBox 会话信息
   */
  void saveUsermailBox(UsermailBoxDO usermailBox);

  /**
   * 查找当前用户的会话列表
   *
   * @param mail 用户地址
   * @param archiveStatus 归档状态
   * @return 会话列表
   */
  List<UsermailBoxDO> listUsermailBoxsByOwner(String mail, int archiveStatus);

  /**
   * 删除指定会话
   *
   * @param from 发件人
   * @param to 收件人
   * @return 删除的数量
   */
  int deleteByOwnerAndMail2(String from, String to);

  /**
   * 根据会话拥有者和另一位聊天者查询会话列表
   *
   * @param from 发件人
   * @param to 收件人
   * @return 会话列表
   */
  List<UsermailBoxDO> listUsermailBoxsByOwnerAndTo(String from, String to);

  /**
   * 更新会话归档状态
   *
   * @param from 发件人
   * @param to 收件人
   * @param archiveStatus 归档状态
   * @return 更新的数量
   */
  int updateArchiveStatus(String from, String to, int archiveStatus);

  /**
   * 根据拥有者和另一位聊天者查询会话信息
   *
   * @param owner 会话拥有者
   * @param to 收件人
   * @return 会话信息
   */
  UsermailBoxDO selectByOwnerAndMail2(String owner, String to);

}
