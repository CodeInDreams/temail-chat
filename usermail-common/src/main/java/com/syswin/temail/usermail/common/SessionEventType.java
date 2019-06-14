package com.syswin.temail.usermail.common;

/**
 * 事件列表
 */
public interface SessionEventType {

  /** 会话消息已接收 (未读+1) */
  int EVENT_TYPE_0 = 0;
  /** 消息撤回 */
  int EVENT_TYPE_2 = 2;
  /** 阅后即焚 */
  int EVENT_TYPE_3 = 3;
  /** 会话消息已删除 */
  int EVENT_TYPE_4 = 4;
  /** 会话消息已接收 (未读+1)(阅后即焚消息) */
  int EVENT_TYPE_17 = 17;
  /** 发送回复消息 */
  int EVENT_TYPE_18 = 18;
  /** 撤回回复消息 */
  int EVENT_TYPE_19 = 19;
  /** 删除回复消息 */
  int EVENT_TYPE_20 = 20;
  /** 阅后即焚回复消息 */
  int EVENT_TYPE_26 = 26;
  /** 单聊归档 */
  int EVENT_TYPE_33 = 33;
  /** 单聊取消归档 */
  int EVENT_TYPE_34 = 34;
  /** 单聊消息移送废纸篓 */
  int EVENT_TYPE_35 = 35;
  /** 废纸篓消息还原 */
  int EVENT_TYPE_36 = 36;
  /** 废纸篓消息删除 */
  int EVENT_TYPE_37 = 37;
  /** 跨域消息类型 */
  int EVENT_TYPE_51 = 51;
}
