/*
 * MIT License
 *
 * Copyright (c) 2019 Syswin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.syswin.temail.usermail.common;

/**
 * 事件列表
 */
public interface SessionEventType {

  /**
   * 会话消息已接收 (未读+1)
   */
  int EVENT_TYPE_0 = 0;
  /**
   * 消息撤回
   */
  int EVENT_TYPE_2 = 2;
  /**
   * 阅后即焚
   */
  int EVENT_TYPE_3 = 3;
  /**
   * 会话消息已删除
   */
  int EVENT_TYPE_4 = 4;
  /**
   * 会话消息已接收 (未读+1)(阅后即焚消息)
   */
  int EVENT_TYPE_17 = 17;
  /**
   * 发送回复消息
   */
  int EVENT_TYPE_18 = 18;
  /**
   * 撤回回复消息
   */
  int EVENT_TYPE_19 = 19;
  /**
   * 删除回复消息
   */
  int EVENT_TYPE_20 = 20;
  /**
   * 阅后即焚回复消息
   */
  int EVENT_TYPE_26 = 26;
  /**
   * 单聊归档
   */
  int EVENT_TYPE_33 = 33;
  /**
   * 单聊取消归档
   */
  int EVENT_TYPE_34 = 34;
  /**
   * 单聊消息移送废纸篓
   */
  int EVENT_TYPE_35 = 35;
  /**
   * 废纸篓消息还原
   */
  int EVENT_TYPE_36 = 36;
  /**
   * 废纸篓消息删除
   */
  int EVENT_TYPE_37 = 37;
  /**
   * 跨域消息类型
   */
  int EVENT_TYPE_51 = 51;
}
