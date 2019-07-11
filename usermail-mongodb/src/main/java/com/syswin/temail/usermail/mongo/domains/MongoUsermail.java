package com.syswin.temail.usermail.mongo.domains;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="usermail")
public class MongoUsermail implements Serializable {

  private long id;
  private String msgid;
  private String sessionid;
  private String from;
  private String to;
  private int status;
  private int type;
  private String owner;
//  private String message;
  private long seqNo;
  private String at;
  private String topic;
  private Date createTime;

  private String lastReplyMsgId;

  private Integer replyCount;

  private Date updateTime;

  private byte[] zipMsg;

  private String author;

  private String filter;


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

  public String getLastReplyMsgId() {
    return lastReplyMsgId;
  }

  public void setLastReplyMsgId(String lastReplyMsgId) {
    this.lastReplyMsgId = lastReplyMsgId;
  }

  public Integer getReplyCount() {
    return replyCount;
  }

  public void setReplyCount(Integer replyCount) {
    this.replyCount = replyCount;
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

//  public String getMessage() {
//    return message;
//  }
//
//  public void setMessage(String message) {
//    this.message = message;
//  }

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

  public String getAt() {
    return at;
  }

  public void setAt(String at) {
    this.at = at;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }


  public byte[] getZipMsg() {
    return zipMsg;
  }

  public void setZipMsg(byte[] zipMsg) {
    this.zipMsg = zipMsg;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }


  @Override
  public String toString() {
    return "MongoUsermail{" +
        "id=" + id +
        ", msgid='" + msgid + '\'' +
        ", sessionid='" + sessionid + '\'' +
        ", from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", status=" + status +
        ", type=" + type +
        ", owner='" + owner + '\'' +
        ", seqNo=" + seqNo +
        ", at='" + at + '\'' +
        ", topic='" + topic + '\'' +
        ", createTime=" + createTime +
        ", lastReplyMsgId='" + lastReplyMsgId + '\'' +
        ", replyCount=" + replyCount +
        ", updateTime=" + updateTime +
        ", zipMsg=" + Arrays.toString(zipMsg) +
        ", author='" + author + '\'' +
        ", filter='" + filter + '\'' +
        '}';
  }
}
