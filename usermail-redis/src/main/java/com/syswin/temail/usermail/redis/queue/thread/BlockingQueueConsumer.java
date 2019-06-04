package com.syswin.temail.usermail.redis.queue.thread;

import com.syswin.temail.usermail.redis.queue.QueueNode;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentMap;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class BlockingQueueConsumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(BlockingQueueConsumer.class);

  private BlockingQueue<QueueNode> queue;
  private ConcurrentMap<String, Set<String>> keyMap;
  private ThreadPoolTaskExecutor taskExecutor;

  public BlockingQueueConsumer(BlockingQueue<QueueNode> queue, ConcurrentMap<String, Set<String>> keyMap,
      ThreadPoolTaskExecutor taskExecutor) {
    this.queue = queue;
    this.keyMap = keyMap;
    this.taskExecutor = taskExecutor;
  }

  @PostConstruct
  public void start() {
    taskExecutor.execute(this::take);
  }


  public void take() {
    while (true) {
      try {
        LOGGER.debug("get data from queue!");
        QueueNode queueNode = queue.take();
        LOGGER.debug("queue:{}", queueNode);
        if (keyMap.containsKey(queueNode.getGroupTemail())) {
          keyMap.get(queueNode.getGroupTemail()).add(queueNode.getKey());
        } else {
          keyMap.put(queueNode.getGroupTemail(), new HashSet<>(Collections.singletonList(queueNode.getKey())));
        }
      } catch (InterruptedException e) {
        LOGGER.warn("blocking queue consumer InterruptedException: ", e);
      } catch (Exception e) {
        LOGGER.warn("blocking queue consumer Exception: ", e);
      }
    }
  }
}
