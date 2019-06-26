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
 * 返回code
 */
public enum ResultCodeEnum {
  /* 请求成功 */
  SUCCESS(200, "success"),
  /* 请求参数错误 */
  ERROR_REQUEST_PARAM(400, "请求参数错误"),
  /* storeType 不正确！ */
  ERROR_ILLEGAL_STORE_TYPE(459, "storeType 不正确！"),
  /* 发送方处于接收方的黑名单中 */
  ERROR_IN_BLACKLIST(461, "发送方处于接收方的黑名单中"),
  /* 重复DATABASE_KEY_ID，msgId重复，前端有可能收不到回执。 */
  ERROR_DATABASE_KEY_ID(463, "重复DATABASE_KEY_ID,"),
  /* 源消息不存在，此code APP 端做业务逻辑判断，不能修改code值 */
  ERROR_ILLEGAL_PARENT_MSG_ID(464, "源消息不存在"),
  /* 消息decode错误，请检查消息格式 */
  ERROR_MSG_DECODE(467, "消息decode错误，请检查消息格式"),
  /* 消息encode错误，请检查消息格式 */
  ERROR_MSG_ENCODE(468, "消息encode错误，请检查消息格式"),
  /* 消息ZIP错误，请检查消息格式 */
  ERROR_MSG_ZIP(469, "消息ZIP错误，请检查消息格式"),
  /* 出现异常 */
  ERROR_SERVER(500, "出现异常"),
  /* seqID过滤参数格式错误，无法正确解析 */
  ERROR_FILTER_SEQIDS(567, "seqID过滤参数格式错误，无法正确解析");

  private int code;
  private String message;

  ResultCodeEnum(int code, String message) {
    this.code = code;
    this.message = message;
  }

  public int getCode() {
    return code;
  }

  void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "RESULT_CODE{" +
        "code=" + code +
        ", message='" + message + '\'' +
        '}';
  }
}
