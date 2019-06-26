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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.syswin.temail.usermail.redis.common.CacheKey.Usermail;
import com.syswin.temail.usermail.redis.confguration.RedisConfiguration;
import com.syswin.temail.usermail.redis.configuration.TestRedisConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = {RedisConfiguration.class, TestRedisConfiguration.class})
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class RedisUsermailAdapterTest {

  @MockBean
  private RedisTemplate redisTemplate;
  @MockBean
  private ValueOperations valueOperations;
  @Autowired
  private RedisUsermailAdapter redisUsermailAdapter;

  @Before
  public void setUp() {
    when(redisTemplate.opsForValue()).thenReturn(valueOperations);
  }

  @Test
  public void testGetPkID() {
    redisUsermailAdapter.getPkID();
    verify(valueOperations).increment(Usermail.KEY_USERMAIL_PKID, 1L);
  }

  @Test
  public void testGetMsgReplyPkID() {
    redisUsermailAdapter.getMsgReplyPkID();
    verify(valueOperations).increment(Usermail.KEY_USERMAIL_MSG_REPLY_PKID, 1L);
  }

  @Test
  public void testGetUsermailBlacklistPkID() {
    redisUsermailAdapter.getUsermailBlacklistPkID();
    verify(valueOperations).increment(Usermail.KEY_USERMAIL_BLACKLIST_PKID, 1L);
  }

  @Test
  public void testSetLastMsgId() {
    String owner = "redis-owner";
    String to = "redis-to";
    String lastMsgId = "123";
    String usermailKey = String.format(Usermail.KEY_USERMAIL_LAST_MSG_ID, owner, to);
    redisUsermailAdapter.setLastMsgId(owner, to, lastMsgId);
    verify(valueOperations).set(usermailKey, lastMsgId);
  }

  @Test
  public void testGetLastMsgId() {
    String owner = "redis-owner";
    String to = "redis-to";
    String lastMsgId = "123";
    String usermailKey = String.format(Usermail.KEY_USERMAIL_LAST_MSG_ID, owner, to);
    when(valueOperations.get(usermailKey)).thenReturn(lastMsgId);
    String result = redisUsermailAdapter.getLastMsgId(owner, to);
    Assert.assertTrue(result.equals(lastMsgId));
  }

  @Test
  public void testDeleteLastMsgId() {
    String owner = "redis-owner";
    String to = "redis-to";
    String usermailKey = String.format(Usermail.KEY_USERMAIL_LAST_MSG_ID, owner, to);
    redisUsermailAdapter.deleteLastMsgId(owner, to);
    verify(redisTemplate).delete(usermailKey);
  }

  @Test
  public void testGetMsgReplySeqNo() {
    String parentMsgid = "parentMsgid-123";
    String owner = "owner";
    String seqNo = String.format(Usermail.KEY_USERMAIL_REPLY_SEQNO, parentMsgid, owner);
    redisUsermailAdapter.getMsgReplySeqNo(parentMsgid, owner);
    verify(valueOperations).increment(seqNo, 1l);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetMsgSeqNo_toNull() {
    redisUsermailAdapter.getMsgSeqNo("from", null, "owner");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetMsgSeqNo_fromNull() {
    redisUsermailAdapter.getMsgSeqNo(null, "to", "owner");
  }

  @Test
  public void testGetMsgSeqNo_from() {
    String from = "1_form";
    String to = "0_to";
    String owner = "owner";
    String key = String.format("%s%s_%s_%s", Usermail.KEY_USERMAIL_SEQNO, from, to, owner);
    when(valueOperations.increment(key, 1l)).thenReturn(2l);
    long sequenceNo = redisUsermailAdapter.getMsgSeqNo(from, to, owner);
    Assert.assertTrue(sequenceNo == 2l);
  }

  @Test
  public void testGetMsgSeqNo_to() {
    String from = "0_form";
    String to = "1_to";
    String owner = "owner";
    String key = String.format("%s%s_%s_%s", Usermail.KEY_USERMAIL_SEQNO, to, from, owner);
    when(valueOperations.increment(key, 1l)).thenReturn(2l);
    long sequenceNo = redisUsermailAdapter.getMsgSeqNo(from, to, owner);
    Assert.assertTrue(sequenceNo == 2l);
  }


}

