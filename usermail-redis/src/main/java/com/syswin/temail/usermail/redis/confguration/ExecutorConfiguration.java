//package com.syswin.temail.usermail.redis.confguration;
//
//import com.syswin.temail.usermail.redis.queue.QueueNode;
//import java.util.Set;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ConcurrentMap;
//import java.util.concurrent.Executors;
//import java.util.concurrent.LinkedBlockingDeque;
//import java.util.concurrent.ScheduledExecutorService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//@Configuration
//public class ExecutorConfiguration {
//
//  @Bean
//  public ThreadPoolTaskExecutor taskExecutor() {
//    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//    executor.setCorePoolSize(2);
//    return executor;
//  }
//
//  @Bean
//  public ScheduledExecutorService scheduledExecutor() {
//    return Executors.newSingleThreadScheduledExecutor();
//  }
//
//  @Bean
//  public ConcurrentMap<String, Set<String>> keyMap() {
//    return new ConcurrentHashMap<>();
//  }
//
//  @Bean
//  public BlockingQueue<QueueNode> queue() {
//    return new LinkedBlockingDeque<>();
//  }
//}
//
