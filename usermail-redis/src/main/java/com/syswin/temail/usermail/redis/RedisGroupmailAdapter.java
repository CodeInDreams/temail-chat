package com.syswin.temail.usermail.redis;

import com.syswin.temail.usermail.core.IGroupmailAdapter;
import com.syswin.temail.usermail.redis.common.CacheKey.Groupmail;
import com.syswin.temail.usermail.redis.queue.BlockingQueueAdapter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

public class RedisGroupmailAdapter implements IGroupmailAdapter {

  @Autowired
  private RedisTemplate redisTemplate;

  @Autowired
  private BlockingQueueAdapter blockingQueueAdapter;

  @Override
  public long getGroupmailPKid() {
    return redisTemplate.opsForValue().increment(Groupmail.KEY_GROUPMAIL_PKID, 1L);
  }

  @Override
  public long getGroupmailMemberPKid() {
    return redisTemplate.opsForValue().increment(Groupmail.KEY_GROUPMAIL_MEMBER_PKID, 1L);
  }

  @Override
  public long getGroupmailMsgPkID() {
    return redisTemplate.opsForValue().increment(Groupmail.KEY_GROUPMAIL_MSG_PKID, 1L);
  }

  @Override
  public long getMsgSeqNo(String groupTemail) {
    String key = Groupmail.KEY_GROUPMAIL_SEQNO + groupTemail;
    long sequenceNo = redisTemplate.opsForValue().increment(key, 1L);
    this.blockingQueueAdapter.addKey(groupTemail, key);
    return sequenceNo;
  }

  @Override
  public long getGroupmailMsgReplyPkID() {
    return redisTemplate.opsForValue().increment(Groupmail.KEY_GROUPMAIL_MSG_REPLY_PKID, 1L);
  }

  @Override
  public long getMsgReplySeqNo(String parentMsgid, String groupTemail) {
    String key = String.format(Groupmail.KEY_GROUPMAIL_REPLY_SEQNO, parentMsgid);
    long sequenceNo = redisTemplate.opsForValue().increment(key, 1L);
    this.blockingQueueAdapter.addKey(groupTemail, key);
    return sequenceNo;
  }

  @Override
  public void removeMsgsReplySeqNo(List<String> parentMsgids) {
    if (CollectionUtils.isEmpty(parentMsgids)) {
      return;
    }
    Set<String> keys = new HashSet<>(parentMsgids.size());
    for (int i = 0; i < parentMsgids.size(); i++) {
      keys.add(String.format(Groupmail.KEY_GROUPMAIL_REPLY_SEQNO, parentMsgids.get(i)));
    }
    redisTemplate.delete(keys);
  }

  @Override
  public long getGroupmailAtMsgPkID() {
    return redisTemplate.opsForValue().increment(Groupmail.KEY_GROUPMAIL_AT_MSG_PKID, 1L);
  }

  @Override
  public long getGroupmailAtMsgSeqNo(String msgid, String groupTemail) {
    String key = String.format(Groupmail.KEY_GROUPMAIL_AT_MSG_SEQNO, msgid);
    long sequenceNo = redisTemplate.opsForValue().increment(key, 1L);
    this.blockingQueueAdapter.addKey(groupTemail, key);
    return sequenceNo;
  }

  @Override
  public void removeGroupmailAtMsgSeqNo(String msgid) {
    String key = String.format(Groupmail.KEY_GROUPMAIL_AT_MSG_SEQNO, msgid);
    redisTemplate.delete(key);
  }

  @Override
  public long getGroupmailAtMemberPkID() {
    return redisTemplate.opsForValue().increment(Groupmail.KEY_GROUPMAIL_AT_MEMBER_PKID, 1L);
  }

  @Override
  public long getGroupmailAtMemberSeqNo(String groupTemail, String temail) {
    String key = String.format(Groupmail.KEY_GROUPMAIL_AT_MEMBER_SEQNO, groupTemail, temail);
    long sequenceNo = redisTemplate.opsForValue().increment(key, 1L);
    this.blockingQueueAdapter.addKey(groupTemail, key);
    return sequenceNo;
  }

  @Override
  public long getGroupmailBlacklistPkID() {
    return redisTemplate.opsForValue().increment(Groupmail.KEY_GROUPMAIL_BLACKLIST_PKID, 1L);
  }

  @Override
  public void setGroupmailLastMsgId(String groupTemail, String msgId) {
    String key = String.format(Groupmail.KEY_GROUPMAIL_LAST_MSGID, groupTemail);
    redisTemplate.opsForValue().set(key, msgId);
  }

  @Override
  public String getGroupmailLastMsgId(String groupTemail) {
    String key = String.format(Groupmail.KEY_GROUPMAIL_LAST_MSGID, groupTemail);
    return (String) redisTemplate.opsForValue().get(key);
  }

  @Override
  public void removeGroupmailLastMsgId(String groupTemail) {
    String key = String.format(Groupmail.KEY_GROUPMAIL_LAST_MSGID, groupTemail);
    redisTemplate.delete(key);
  }

  @Override
  public void removeCacheByGroupTemail(String groupTemail) {
    removeGroupmailLastMsgId(groupTemail);
    clearGroupmailKeys(groupTemail);
  }


  private void clearGroupmailKeys(String groupTemail) {
    Set<String> keySet = redisTemplate.opsForSet().members(String.format(Groupmail.KEY_GROUPMAIL_KEYS, groupTemail));
    if (keySet != null) {
      keySet.forEach(key -> redisTemplate.delete(key));
    }
    redisTemplate.expire(String.format(Groupmail.KEY_GROUPMAIL_KEYS, groupTemail), 1, TimeUnit.MINUTES);
  }
}
