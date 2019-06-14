package com.syswin.temail.usermail.common;

/**
 * 回复消息的数量
 */
public enum ReplyCountEnum {
  /* 加1 */
  INCR(1),
  /* 减1 */
  DECR(-1);
  private int code;

  ReplyCountEnum(int code) {
    this.code = code;
  }

  public static boolean isINCR(int code) {
    return INCR.value() == code;
  }

  public static boolean isDECR(int code) {
    return DECR.value() == code;
  }

  public int value() {
    return this.code;
  }
}
