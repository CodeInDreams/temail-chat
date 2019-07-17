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
import java.util.Arrays;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.mongodb.core.mapping.Document;

@Entity
@Table(name = "usermail_msg_reply")
@Document(collection = "usermail_msg_reply")
public class MongoUsermailReplyMsg implements Serializable {

  @Id
  private long id;
  private String msgid;
  private String sessionid;
  private String from;
  private String to;
  private int status;
  private int type;
  private String owner;
  private long seqNo;
  private Date createTime;
  private String parentMsgid;
  private Date updateTime;
  private byte[] zipMsg;

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
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

  public String getParentMsgid() {
    return parentMsgid;
  }

  public void setParentMsgid(String parentMsgid) {
    this.parentMsgid = parentMsgid;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public long getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(long seqNo) {
    this.seqNo = seqNo;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getSessionid() {
    return sessionid;
  }

  public void setSessionid(String sessionid) {
    this.sessionid = sessionid;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public byte[] getZipMsg() {
    return zipMsg;
  }

  public void setZipMsg(byte[] zipMsg) {
    this.zipMsg = zipMsg;
  }

  @Override
  public String toString() {
    return "MongoUsermailMsgReply{" +
        "id=" + id +
        ", msgid='" + msgid + '\'' +
        ", sessionid='" + sessionid + '\'' +
        ", from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", status=" + status +
        ", type=" + type +
        ", owner='" + owner + '\'' +
        ", seqNo=" + seqNo +
        ", createTime=" + createTime +
        ", parentMsgid='" + parentMsgid + '\'' +
        ", updateTime=" + updateTime +
        ", zipMsg=" + Arrays.toString(zipMsg) +
        '}';
  }
}
