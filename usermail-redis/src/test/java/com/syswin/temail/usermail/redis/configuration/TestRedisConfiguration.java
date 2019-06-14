package com.syswin.temail.usermail.redis.configuration;

import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.redis.RedisUsermailAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class TestRedisConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public IUsermailAdapter usermailAdapter(RedisTemplate redisTemplate) {
    return new RedisUsermailAdapter(redisTemplate);
  }

}
