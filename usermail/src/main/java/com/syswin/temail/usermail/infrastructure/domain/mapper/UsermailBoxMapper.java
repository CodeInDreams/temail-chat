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

package com.syswin.temail.usermail.infrastructure.domain.mapper;

import com.syswin.temail.usermail.domains.UsermailBoxDO;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailBoxMapper {

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
  List<UsermailBoxDO> listUsermailBoxsByOwner(@Param("owner") String mail, @Param("archiveStatus") int archiveStatus);

  /**
   * 删除指定会话
   *
   * @param owner 发件人
   * @param mail2 收件人
   * @return 删除的数量
   */
  int deleteByOwnerAndMail2(@Param("owner") String owner, @Param("mail2") String mail2);

  /**
   * 根据会话拥有者和另一位聊天者查询会话列表
   *
   * @param from 发件人
   * @param to 收件人
   * @return 会话列表
   */
  List<UsermailBoxDO> listUsermailBoxsByOwnerAndTo(@Param("owner") String from, @Param("mail2") String to);

  /**
   * 更新会话归档状态
   *
   * @param from 发件人
   * @param to 收件人
   * @param archiveStatus 归档状态
   * @return 更新的数量
   */
  int updateArchiveStatus(@Param("owner") String from, @Param("mail2") String to,
      @Param("archiveStatus") int archiveStatus);

  /**
   * 根据拥有者和另一位聊天者查询会话信息
   *
   * @param owner 会话拥有者
   * @param to 收件人
   * @return 会话信息
   */
  UsermailBoxDO selectByOwnerAndMail2(@Param("owner") String owner, @Param("mail2") String to);

  /**
   * 拉取topN会话列表
   *
   * @param owner 会话拥有者
   * @param archiveStatus 归档状态
   * @return 会话列表
   */
  List<UsermailBoxDO> selectTopNByOwner(@Param("owner") String owner, @Param("archiveStatus") int archiveStatus);


  /**
   * 更新会话昵称头像信息
   *
   * @param usermailBoxDO 更新参数
   */
  void updateSessionExtData(UsermailBoxDO usermailBoxDO);

  /**
   * 分页清理域下数据
   *
   * @param domain 域
   * @param pageSize 页面大小
   * @return 实际删除数量
   */
  int deleteDomain(@Param("domain") String domain, @Param("pageSize") int pageSize);
}
