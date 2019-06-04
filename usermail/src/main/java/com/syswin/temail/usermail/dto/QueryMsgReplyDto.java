package com.syswin.temail.usermail.dto;

import java.io.Serializable;
import org.apache.ibatis.type.Alias;

@Alias("QueryMsgReplyDto")
public class QueryMsgReplyDto implements Serializable {

  private long fromSeqNo;
  private int pageSize;
  private String msgid;
  private int status;
  private String parentMsgid;
  private String signal;
  private String owner;

  public long getFromSeqNo() {
    return fromSeqNo;
  }

  public void setFromSeqNo(long fromSeqNo) {
    this.fromSeqNo = fromSeqNo;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public String getMsgid() {
    return msgid;
  }

  public void setMsgid(String msgid) {
    this.msgid = msgid;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getParentMsgid() {
    return parentMsgid;
  }

  public void setParentMsgid(String parentMsgid) {
    this.parentMsgid = parentMsgid;
  }

  public String getSignal() {
    return signal;
  }

  public void setSignal(String signal) {
    this.signal = signal;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  @Override
  public String toString() {
    return "QueryMsgReplyDto{" +
        "fromSeqNo=" + fromSeqNo +
        ", pageSize=" + pageSize +
        ", msgid='" + msgid + '\'' +
        ", status=" + status +
        ", parentMsgid='" + parentMsgid + '\'' +
        ", signal='" + signal + '\'' +
        ", owner='" + owner + '\'' +
        '}';
  }
}
