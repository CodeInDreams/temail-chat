package com.syswin.temail.usermail.redis;

import com.syswin.temail.usermail.core.IUsermailAdapter;
import org.junit.Assert;
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
@SpringBootTest
@Import(RedisTestApplication.class)
public class RedisMessageTests {

  @Autowired
  RedisTemplate<String, Object> redisTemplate;

  @Autowired
  IUsermailAdapter iUsermailAdapter;


  @Test
  public void testRedis() {
    System.out.println(redisTemplate);
    System.out.println(iUsermailAdapter);
//    String msgid = iUsermailAdapter.getMsgid();
//    System.out.println(msgid);
//    Assert.assertNotNull(msgid);
    Assert.assertTrue(iUsermailAdapter.getPkID() > 0);
    System.out.println(iUsermailAdapter);
    Assert.assertTrue(iUsermailAdapter.getMsgSeqNo("from","to","owner") > 0);
  }

}

@SpringBootApplication
class RedisTestApplication {

  public static void main(String[] args) {
    SpringApplication.run(RedisTestApplication.class, args);
  }
}
