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

package com.syswin.temail.usermail.redis.common;

public class CacheKey {

  public interface Usermail {

    /**
     * 消息主键
     */
    String KEY_USERMAIL_PKID = "usermail_pkid";
    /**
     * 单聊黑名单主键
     */
    String KEY_USERMAIL_BLACKLIST_PKID = "usermail_blacklist_pkid";
    /**
     * 单聊最新消息id
     */
    String KEY_USERMAIL_LAST_MSG_ID = "usermail_last_msgid_%s_%s";
    /**
     * 会话sequenceno
     */
    String KEY_USERMAIL_SEQNO = "usermail_seqno_";
    /**
     * 单聊回复消息主键
     */
    String KEY_USERMAIL_MSG_REPLY_PKID = "usermail_msg_reply_pkid ";
    /**
     * 单聊回复消息sequenceno
     */
    String KEY_USERMAIL_REPLY_SEQNO = "usermail_reply_seqno_%s_%s";
  }

}
