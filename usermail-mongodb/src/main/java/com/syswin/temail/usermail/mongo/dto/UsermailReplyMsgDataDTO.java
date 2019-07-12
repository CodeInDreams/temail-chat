package com.syswin.temail.usermail.mongo.dto;

import com.syswin.temail.usermail.common.MongoMsgReplyEventEnum;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Arrays;

public class UsermailReplyMsgDataDTO implements Serializable {


  private long id;
  private String parentMsgId;
  private String msgId;
  private String from;
  private String to;
  private long seqNo;
  private int status;
  private int type;
  private Timestamp createTime;
  private Timestamp updateTime;
  private String owner;
  private String sessionId;
  private byte[] zipMsg;
  private MongoMsgReplyEventEnum msgReplyEventEnum;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getParentMsgId() {
    return parentMsgId;
  }

  public void setParentMsgId(String parentMsgId) {
    this.parentMsgId = parentMsgId;
  }

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
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

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public byte[] getZipMsg() {
    return zipMsg;
  }

  public void setZipMsg(byte[] zipMsg) {
    this.zipMsg = zipMsg;
  }

  public MongoMsgReplyEventEnum getMsgReplyEventEnum() {
    return msgReplyEventEnum;
  }

  public void setMsgReplyEventEnum(MongoMsgReplyEventEnum msgReplyEventEnum) {
    this.msgReplyEventEnum = msgReplyEventEnum;
  }

  @Override
  public String toString() {
    return "UsermailReplyMsgDataDTO{" +
        "id=" + id +
        ", parentMsgId='" + parentMsgId + '\'' +
        ", msgId='" + msgId + '\'' +
        ", from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", seqNo=" + seqNo +
        ", status=" + status +
        ", type=" + type +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", owner='" + owner + '\'' +
        ", sessionId='" + sessionId + '\'' +
        ", zipMsg=" + Arrays.toString(zipMsg) +
        ", msgReplyEventEnum=" + msgReplyEventEnum +
        '}';
  }
}
