//package com.syswin.temail.usermail.redis.queue;
//
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.TimeUnit;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class BlockingQueueAdapter {
//
//  private static final Logger LOGGER = LoggerFactory.getLogger(BlockingQueueAdapter.class);
//
//  private BlockingQueue<QueueNode> queue;
//
//  @Autowired
//  public BlockingQueueAdapter(BlockingQueue<QueueNode> queue) {
//    this.queue = queue;
//  }
//
//  public void addKey(String groupTemail, String key) {
//    try {
//      if (!queue.offer(new QueueNode(groupTemail, key), 10, TimeUnit.MILLISECONDS)) {
//        LOGGER.warn("put key to queue failed: {}, {}", groupTemail, key);
//      }
//    } catch (InterruptedException e) {
//      LOGGER.warn("blocking queue producer exception: ", e);
//    }
//  }
//}
