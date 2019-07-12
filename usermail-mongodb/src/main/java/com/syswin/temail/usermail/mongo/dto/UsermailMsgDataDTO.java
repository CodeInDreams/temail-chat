package com.syswin.temail.usermail.mongo.dto;

import com.syswin.temail.usermail.common.MongoMsgEventEnum;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;

public class UsermailMsgDataDTO implements Serializable {

  private long id;
  private String msgId;
  private String sessionId;
  private String from;
  private String to;
  private int type;
  private int status;
  private long seqNo;
  private String owner;
  //private String message;
  private Timestamp createTime;
  private String at;
  private String topic;
  private Integer replyCount;
  private Timestamp updateTime;
  private String lastReplyMsgId;
  private byte[] zipMsg;
  private String author;
  private String filter;
  private MongoMsgEventEnum msgEventEnum;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
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

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public long getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(long seqNo) {
    this.seqNo = seqNo;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Timestamp createTime) {
    this.createTime = createTime;
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

  public Integer getReplyCount() {
    return replyCount;
  }

  public void setReplyCount(Integer replyCount) {
    this.replyCount = replyCount;
  }

  public Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Timestamp updateTime) {
    this.updateTime = updateTime;
  }

  public String getLastReplyMsgId() {
    return lastReplyMsgId;
  }

  public void setLastReplyMsgId(String lastReplyMsgId) {
    this.lastReplyMsgId = lastReplyMsgId;
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

  public MongoMsgEventEnum getMsgEventEnum() {
    return msgEventEnum;
  }

  public void setMsgEventEnum(MongoMsgEventEnum msgEventEnum) {
    this.msgEventEnum = msgEventEnum;
  }

  @Override
  public String toString() {
    return "UsermailMsgDataDTO{" +
        "id=" + id +
        ", msgId='" + msgId + '\'' +
        ", sessionId='" + sessionId + '\'' +
        ", from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", type=" + type +
        ", status=" + status +
        ", seqNo=" + seqNo +
        ", owner='" + owner + '\'' +
        ", createTime=" + createTime +
        ", at='" + at + '\'' +
        ", topic='" + topic + '\'' +
        ", replyCount=" + replyCount +
        ", updateTime=" + updateTime +
        ", lastReplyMsgId='" + lastReplyMsgId + '\'' +
        ", zipMsg=" + Arrays.toString(zipMsg) +
        ", author='" + author + '\'' +
        ", filter='" + filter + '\'' +
        ", msgEventEnum=" + msgEventEnum +
        '}';
  }
}
