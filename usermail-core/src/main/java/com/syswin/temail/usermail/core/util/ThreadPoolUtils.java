package com.syswin.temail.usermail.core.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolUtils {
  public static ThreadPoolExecutor threadPool;

  /**
   * 无返回值直接执行
   * @param runnable
   */
  public  static void execute(Runnable runnable){
    getThreadPool().execute(runnable);
  }

  /**
   * 返回值直接执行
   * @param callable
   */
  public  static <T> Future<T> submit(Callable<T> callable){
    return   getThreadPool().submit(callable);
  }


  /**
   * 获取线程池
   * @return 线程池对象
   */
  public static ThreadPoolExecutor getThreadPool() {
    if (threadPool != null) {
      return threadPool;
    } else {
      synchronized (ThreadPoolUtils.class) {
        if (threadPool == null) {
          threadPool = new ThreadPoolExecutor(5, 8, 60, TimeUnit.SECONDS,
              new LinkedBlockingQueue<>(10), new ThreadPoolExecutor.CallerRunsPolicy());
        }
        return threadPool;
      }
    }
  }

}
