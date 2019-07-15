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

package com.syswin.temail.usermail.mongo.domains;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usermail_msg_reply")
public class MongoUsermailReplyMsg implements Serializable {

  private long id;
  private String parentMsgid;
  private String msgid;
  private String from;
  private String to;
  private long seqNo;
  private String msg;
  private int status;
  private int type;
  private Timestamp createTime;
  private Timestamp updateTime;
  private String owner;
  private String sessionid;
  private byte[] zipMsg;

  public MongoUsermailReplyMsg() {
  }

  public MongoUsermailReplyMsg(long id, String parentMsgid, String msgid, String from, String to, long seqNo,
      String msg, int status, int type, String owner, String sessionid, byte[] zipMsg) {
    this.id = id;
    this.parentMsgid = parentMsgid;
    this.msgid = msgid;
    this.from = from;
    this.to = to;
    this.seqNo = seqNo;
    this.msg = msg;
    this.status = status;
    this.type = type;
    this.owner = owner;
    this.sessionid = sessionid;
    this.zipMsg = zipMsg;
  }

  public MongoUsermailReplyMsg(long id, String parentMsgid, String msgid, String from, String to, long seqNo,
      String msg, int status, int type, String owner, String sessionid) {
    this.id = id;
    this.parentMsgid = parentMsgid;
    this.msgid = msgid;
    this.from = from;
    this.to = to;
    this.seqNo = seqNo;
    this.msg = msg;
    this.status = status;
    this.type = type;
    this.owner = owner;
    this.sessionid = sessionid;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getParentMsgid() {
    return parentMsgid;
  }

  public void setParentMsgid(String parentMsgid) {
    this.parentMsgid = parentMsgid;
  }

  public String getMsgid() {
    return msgid;
  }

  public void setMsgid(String msgid) {
    this.msgid = msgid;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public long getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(long seqNo) {
    this.seqNo = seqNo;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Timestamp createTime) {
    this.createTime = createTime;
  }

  public Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Timestamp updateTime) {
    this.updateTime = updateTime;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getSessionid() {
    return sessionid;
  }

  public void setSessionid(String sessionid) {
    this.sessionid = sessionid;
  }

  public byte[] getZipMsg() {
    return zipMsg;
  }

  public void setZipMsg(byte[] zipMsg) {
    this.zipMsg = zipMsg;
  }

  @Override
  public String toString() {
    return "MongoUsermailReplyMsg{" +
        "id=" + id +
        ", parentMsgid='" + parentMsgid + '\'' +
        ", msgid='" + msgid + '\'' +
        ", from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", seqNo=" + seqNo +
        ", msg='" + msg + '\'' +
        ", status=" + status +
        ", type=" + type +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", owner='" + owner + '\'' +
        ", sessionid='" + sessionid + '\'' +
        ", zipMsg=" + Arrays.toString(zipMsg) +
        '}';
  }
}
