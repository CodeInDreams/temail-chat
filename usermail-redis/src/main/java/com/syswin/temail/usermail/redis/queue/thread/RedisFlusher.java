package com.syswin.temail.usermail.redis.queue.thread;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.syswin.temail.usermail.redis.common.CacheKey.Groupmail;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledExecutorService;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisFlusher {

  private static final Logger LOGGER = LoggerFactory.getLogger(RedisFlusher.class);

  private ConcurrentMap<String, Set<String>> keyMap;
  private RedisTemplate redisTemplate;
  private ScheduledExecutorService scheduledExecutor;

  public RedisFlusher(ConcurrentMap<String, Set<String>> keyMap, RedisTemplate redisTemplate,
      ScheduledExecutorService scheduledExecutor) {
    this.keyMap = keyMap;
    this.redisTemplate = redisTemplate;
    this.scheduledExecutor = scheduledExecutor;
  }

  @PostConstruct
  public void start() {
    scheduledExecutor.scheduleWithFixedDelay(
        this::flush, 2, 10, SECONDS);
  }

  private void flush() {
    try {
      LOGGER.debug("redis flusher begin: {}", keyMap);
      keyMap.forEach((groupTemail, keys) -> redisTemplate.opsForSet().add(String.format(Groupmail.KEY_GROUPMAIL_KEYS, groupTemail), keys.toArray()));
      keyMap.clear();
    } catch (Exception e) {
      LOGGER.warn("redis flusher Exception: ", e);
    }
  }
}
