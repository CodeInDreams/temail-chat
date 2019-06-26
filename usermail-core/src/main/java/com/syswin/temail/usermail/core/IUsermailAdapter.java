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

package com.syswin.temail.usermail.core;

public interface IUsermailAdapter {

  /**
   * 获取主键id
   *
   * @return PkID
   */
  long getPkID();

  /**
   * 获取单聊消息序列号
   *
   * @param from 发送者
   * @param to 接收者
   * @param owner 消息所属人
   * @return 序列号
   */
  long getMsgSeqNo(String from, String to, String owner);

  /**
   * 获取回复消息PkId
   *
   * @return PkID
   */
  long getMsgReplyPkID();

  /**
   * 获取回复消息序列号
   *
   * @param parentMsgid 父消息id
   * @param owner 消息所属人
   */
  long getMsgReplySeqNo(String parentMsgid, String owner);

  /**
   * 获取黑名单PkID
   *
   * @return PkID
   */
  long getUsermailBlacklistPkID();

  /**
   * 为收件箱中每条单聊消息设置最新回复消息id
   *
   * @param owner 消息所属人
   * @param to 接收者
   * @param lastMsgId 最新回复消息id
   */
  void setLastMsgId(String owner, String to, String lastMsgId);

  /**
   * 获取单聊消息最新回复消息id
   *
   * @param owner 消息所属人
   * @param to 收件人
   * @return 最新消息id
   */
  String getLastMsgId(String owner, String to);

  /**
   * 删除单聊消息最新回复消息id
   *
   * @param owner 消息所属人
   * @param to 收件人
   */
  void deleteLastMsgId(String owner, String to);
}
