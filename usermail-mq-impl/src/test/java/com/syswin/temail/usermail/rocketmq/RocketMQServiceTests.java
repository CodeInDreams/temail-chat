package com.syswin.temail.usermail.rocketmq;

import com.syswin.temail.usermail.core.IMqAdapter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(MQTestApplication.class)
public class RocketMQServiceTests {

  private static final String TOPIC_TEST_MESSAGE = "test-topic";
  @Autowired
  private IMqAdapter IMqAdapter;

  @Test
  public void sendMessage() {
    boolean result = IMqAdapter.sendMessage(TOPIC_TEST_MESSAGE, "push","test-message");
    Assert.assertTrue(result);
  }

}


@SpringBootApplication
class MQTestApplication {

  public static void main(String[] args) {
    SpringApplication.run(MQTestApplication.class, args);
  }
}
