//package com.syswin.temail.usermail.redis;
//
//import static com.syswin.temail.usermail.redis.common.CacheKey.Dispatcher.KEY_EVENTTYPE_PACKETID_UNIQUE;
//
//import com.syswin.temail.usermail.core.IRedisDispatcherAdapter;
//import java.util.concurrent.TimeUnit;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisTemplate;
//
//
//public class RedisDispatcherAdapter implements IRedisDispatcherAdapter {
//
//  private static final Logger LOGGER = LoggerFactory.getLogger(RedisDispatcherAdapter.class);
//
//  @Autowired
//  private RedisTemplate<String, Object> redisTemplate;
//
//  @Override
//  public boolean checkPacketIdUnique(String xpacketId, Integer eventType) {
//    String key = String.format(KEY_EVENTTYPE_PACKETID_UNIQUE, eventType, xpacketId);
//    Boolean result = redisTemplate.opsForValue().setIfAbsent(key, "value");
//    if (result == null) {
//      LOGGER.warn("RedisDispatcherAdapter-repeat-packetId is [{}]", xpacketId);
//      return false;
//    }
//    redisTemplate.expire(key, 5, TimeUnit.MINUTES);
//    return result;
//  }
//}
