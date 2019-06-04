//package com.syswin.temail.usermail.redis;
//
//import com.syswin.temail.usermail.core.IAppAdapter;
//import com.syswin.temail.usermail.redis.common.CacheKey.App;
//import com.syswin.temail.usermail.redis.queue.BlockingQueueAdapter;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.concurrent.TimeUnit;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.util.CollectionUtils;
//
//public class RedisAppAdapter implements IAppAdapter {
//
//  @Autowired
//  private RedisTemplate redisTemplate;
//  @Autowired
//  private BlockingQueueAdapter blockingQueueAdapter;
//
//  @Override
//  public long getAppConfigPKId() {
//    return redisTemplate.opsForValue().increment(App.KEY_APP_CONFIG_PK, 1);
//  }
//
//  @Override
//  public long getAppPKid() {
//    return redisTemplate.opsForValue().increment(App.KEY_APP_PK, 1L);
//  }
//
//  @Override
//  public long getAppMemberPKid() {
//    return redisTemplate.opsForValue().increment(App.KEY_APP_MEMBER_PK, 1L);
//  }
//
//  @Override
//  public long getAppMsgPkID() {
//    return redisTemplate.opsForValue().increment(App.KEY_APP_MSG_PK, 1L);
//  }
//
//  @Override
//  public long getMsgSeqNo(String appTemail) {
//    String key = App.KEY_APP_SEQ + appTemail;
//    long sequenceNo = redisTemplate.opsForValue().increment(key, 1L);
//    this.blockingQueueAdapter.addKey(appTemail, key);
//    return sequenceNo;
//  }
//
//  @Override
//  public long getAppMsgReplyPkID() {
//    return redisTemplate.opsForValue().increment(App.KEY_APP_MSG_REPLY_PK, 1L);
//  }
//
//  @Override
//  public long getMsgReplySeqNo(String parentMsgid, String appTemail) {
//    String key = String.format(App.KEY_APP_REPLY_SEQ, parentMsgid);
//    long sequenceNo = redisTemplate.opsForValue().increment(key, 1L);
//    this.blockingQueueAdapter.addKey(appTemail, key);
//    return sequenceNo;
//  }
//
//  @Override
//  public void removeMsgsReplySeqNo(List<String> parentMsgids) {
//    if (CollectionUtils.isEmpty(parentMsgids)) {
//      return;
//    }
//    Set<String> keys = new HashSet<>(parentMsgids.size());
//    for (int i = 0; i < parentMsgids.size(); i++) {
//      keys.add(String.format(App.KEY_APP_REPLY_SEQ, parentMsgids.get(i)));
//    }
//    redisTemplate.delete(keys);
//  }
//
//  @Override
//  public long getAppAtMsgPkID() {
//    return redisTemplate.opsForValue().increment(App.KEY_APP_AT_MSG_PK, 1L);
//  }
//
//  @Override
//  public long getAppAtMsgSeqNo(String msgid, String appTemail) {
//    String key = String.format(App.KEY_APP_AT_MSG_SEQ, msgid);
//    long sequenceNo = redisTemplate.opsForValue().increment(key, 1L);
//    this.blockingQueueAdapter.addKey(appTemail, key);
//    return sequenceNo;
//  }
//
//  @Override
//  public void removeAppAtMsgSeqNo(String msgid) {
//    String key = String.format(App.KEY_APP_AT_MSG_SEQ, msgid);
//    redisTemplate.delete(key);
//  }
//
//  @Override
//  public long getAppAtMemberPkID() {
//    return redisTemplate.opsForValue().increment(App.KEY_APP_AT_MEMBER_PK, 1L);
//  }
//
//  @Override
//  public long getAppAtMemberSeqNo(String appTemail, String temail) {
//    String key = String.format(App.KEY_APP_AT_MEMBER_SEQ, appTemail, temail);
//    long sequenceNo = redisTemplate.opsForValue().increment(key, 1L);
//    this.blockingQueueAdapter.addKey(appTemail, key);
//    return sequenceNo;
//  }
//
//  @Override
//  public long getAppBlacklistPkID() {
//    return redisTemplate.opsForValue().increment(App.KEY_APP_BLACKLIST_PK, 1L);
//  }
//
//  @Override
//  public void setAppLastMsgId(String appTemail, String msgId) {
//    String key = String.format(App.KEY_APP_LAST_MSGID, appTemail);
//    redisTemplate.opsForValue().set(key, msgId);
//  }
//
//  @Override
//  public String getAppLastMsgId(String appTemail) {
//    String key = String.format(App.KEY_APP_LAST_MSGID, appTemail);
//    return (String) redisTemplate.opsForValue().get(key);
//  }
//
//  @Override
//  public void removeAppLastMsgId(String appTemail) {
//    String key = String.format(App.KEY_APP_LAST_MSGID, appTemail);
//    redisTemplate.delete(key);
//  }
//
//  @Override
//  public void removeCacheByAppTemail(String appTemail) {
//    removeAppLastMsgId(appTemail);
//    clearGroupmailKeys(appTemail);
//  }
//
//  @Override
//  public Object getTemailInfoByTemail(String temail) {
//    return redisTemplate.opsForValue().get(String.format(App.KEY_AUTH_GATEWAY_INFO, temail));
//  }
//
//  @Override
//  public void setTemailInfoByTemail(String temail, Object temailInfo, Long expireTime) {
//    redisTemplate.opsForValue()
//        .set(String.format(App.KEY_AUTH_GATEWAY_INFO, temail), temailInfo, expireTime, TimeUnit.HOURS);
//  }
//
//  private void clearGroupmailKeys(String appTemail) {
//    Set<String> keySet = redisTemplate.opsForSet().members(String.format(App.KEY_APP_KEYS, appTemail));
//    if (keySet != null) {
//      keySet.forEach(key -> redisTemplate.delete(key));
//    }
//    redisTemplate.expire(String.format(App.KEY_APP_KEYS, appTemail), 1, TimeUnit.MINUTES);
//  }
//}
