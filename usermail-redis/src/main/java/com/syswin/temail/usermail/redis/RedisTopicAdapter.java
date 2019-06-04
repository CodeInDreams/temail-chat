package com.syswin.temail.usermail.redis;

import com.syswin.temail.usermail.core.ITopicAdapter;
import com.syswin.temail.usermail.redis.common.CacheKey.Topic;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisTopicAdapter implements ITopicAdapter {

  @Autowired
  private RedisTemplate redisTemplate;

  @Override
  public long getTopicInfoPkId() {
    return redisTemplate.opsForValue().increment(Topic.KEY_TOPIC_INFO_PKID, 1L);
  }

  @Override
  public long getTopicMemberPkId() {
    return redisTemplate.opsForValue().increment(Topic.KEY_TOPIC_MEMBER_PKID, 1L);
  }

  @Override
  public long getTopicMsgPkId() {
    return redisTemplate.opsForValue().increment(Topic.KEY_TOPIC_MSG_PKID, 1L);
  }

  @Override
  public long getTopicMsgSeqNo(String topicId) {
    String key = String.format(Topic.KEY_TOPIC_MSG_SEQNO, topicId);
    return redisTemplate.opsForValue().increment(key, 1L);
  }

  @Override
  public long getTopicSeqNo() {
    return redisTemplate.opsForValue().increment(Topic.TOPIC_INFO_SEQNO, 1L);
  }

  @Override
  public String getTopicLastMsgId(String topicId, String temail) {
    String key = String.format(Topic.KEY_TOPIC_LAST_MSGID, topicId, temail);
    return (String) redisTemplate.opsForValue().get(key);
  }

  @Override
  public void setTopicLastMsgId(String topicId, String temail, String msgid) {
    String key = String.format(Topic.KEY_TOPIC_LAST_MSGID, topicId, temail);
    redisTemplate.opsForValue().set(key, msgid);
  }

  @Override
  public void removeTopicLastMsg(String topicId, String temail) {
    String pattern = String.format(Topic.KEY_TOPIC_LAST_MSGID, topicId, temail);
    Set<String> deleteKeys = redisTemplate.keys(pattern);
    redisTemplate.delete(deleteKeys);
  }
}
