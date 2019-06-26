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

package com.syswin.temail.usermail.redis;

import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.redis.common.CacheKey.Usermail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisUsermailAdapter implements IUsermailAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisUsermailAdapter.class);
  private final RedisTemplate redisTemplate;

  public RedisUsermailAdapter(RedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public long getPkID() {
    return redisTemplate.opsForValue().increment(Usermail.KEY_USERMAIL_PKID, 1L);
  }


  @Override
  public long getMsgSeqNo(String from, String to, String owner) {
    if (null == from || "".equals(from)) {
      throw new IllegalArgumentException("param [from] is illegal");
    }
    if (null == to || "".equals(to)) {
      throw new IllegalArgumentException("param [to] is illegal");
    }
    String key;
    if (from.compareTo(to) > 0) {
      key = String.format("%s%s_%s_%s", Usermail.KEY_USERMAIL_SEQNO, from, to, owner);
    } else {
      key = String.format("%s%s_%s_%s", Usermail.KEY_USERMAIL_SEQNO, to, from, owner);
    }
    long sequenceNo = redisTemplate.opsForValue().increment(key, 1L);
    LOGGER.debug("getMsgSeqNo-->{}={}", key, sequenceNo);
    return sequenceNo;
  }

  @Override
  public long getMsgReplyPkID() {
    return redisTemplate.opsForValue().increment(Usermail.KEY_USERMAIL_MSG_REPLY_PKID, 1L);
  }

  @Override
  public long getMsgReplySeqNo(String parentMsgid, String owner) {
    String seqNo = String.format(Usermail.KEY_USERMAIL_REPLY_SEQNO, parentMsgid, owner);
    return redisTemplate.opsForValue().increment(seqNo, 1L);
  }

  @Override
  public long getUsermailBlacklistPkID() {
    return redisTemplate.opsForValue().increment(Usermail.KEY_USERMAIL_BLACKLIST_PKID, 1L);
  }

  @Override
  public void setLastMsgId(String owner, String to, String lastMsgId) {
    String usermailKey = String.format(Usermail.KEY_USERMAIL_LAST_MSG_ID, owner, to);
    redisTemplate.opsForValue().set(usermailKey, lastMsgId);
  }

  @Override
  public String getLastMsgId(String owner, String to) {
    String usermailKey = String.format(Usermail.KEY_USERMAIL_LAST_MSG_ID, owner, to);
    return (String) redisTemplate.opsForValue().get(usermailKey);
  }

  @Override
  public void deleteLastMsgId(String owner, String to) {
    String usermailKey = String.format(Usermail.KEY_USERMAIL_LAST_MSG_ID, owner, to);
    redisTemplate.delete(usermailKey);
  }

}
