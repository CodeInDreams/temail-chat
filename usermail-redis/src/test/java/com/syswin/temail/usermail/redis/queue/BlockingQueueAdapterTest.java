package com.syswin.temail.usermail.redis.queue;

import com.syswin.temail.usermail.redis.RedisGroupmailAdapter;
import com.syswin.temail.usermail.redis.common.CacheKey.Groupmail;
import java.util.Set;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
    "logging.level.com.syswin.temail.usermail.redis=debug",
})
@Import(BlockingQueueAdapterTestApplication.class)
@Ignore
public class BlockingQueueAdapterTest {

  @Autowired
  private BlockingQueueAdapter blockingQueueAdapter;

  @Autowired
  private RedisGroupmailAdapter redisGroupmailAdapter;

  @Autowired
  private RedisTemplate redisTemplate;

  @Test
  public void test() throws Exception {
    String group = "ggggg";
    for (int i = 0; i < 10; i++) {
      blockingQueueAdapter.addKey(group, String.valueOf(i));
    }

    Thread.sleep(13 * 1000);
    Set keys = redisTemplate.opsForSet().members(String.format(Groupmail.KEY_GROUPMAIL_KEYS, group));
    System.out.println("before clear: " + keys);
    Assert.assertTrue(keys.size() == 10);

    redisGroupmailAdapter.removeCacheByGroupTemail(group);

    Thread.sleep(70 * 1000);
    keys = redisTemplate.opsForSet().members(String.format(Groupmail.KEY_GROUPMAIL_KEYS, group));
    System.out.println("after clear: " + keys);
    Assert.assertTrue(keys == null || keys.isEmpty());
  }

  @Test
  public void testSet() {
    String group = "ggggg";
    redisTemplate.delete(group);
    redisTemplate.opsForSet().add(group, "1", "2", "3", "4");
    System.out.println(redisTemplate.opsForSet().members(group));
    redisTemplate.delete(group);
  }
}

@SpringBootApplication(scanBasePackages = "com.syswin.temail.usermail.redis")
class BlockingQueueAdapterTestApplication {

  public static void main(String[] args) {
    SpringApplication.run(BlockingQueueAdapterTestApplication.class, args);
  }
}