package com.syswin.temail.usermail.dto;

import java.util.Objects;
import org.apache.ibatis.type.Alias;

@Alias("umQuery")
public class UmQueryDTO implements java.io.Serializable {

  private static final long serialVersionUID = -1986555435518993672L;
  private long fromSeqNo;
  private int pageSize;
  private String msgid;
  private int status;
  private String sessionid;
  private String owner;
  private String signal;

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

  public String getSessionid() {
    return sessionid;
  }

  public void setSessionid(String sessionid) {
    this.sessionid = sessionid;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getSignal() {
    return signal;
  }

  public void setSignal(String signal) {
    this.signal = signal;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("UmQueryDTO{");
    sb.append("fromSeqNo=").append(fromSeqNo);
    sb.append(", pageSize=").append(pageSize);
    sb.append(", msgid='").append(msgid).append('\'');
    sb.append(", status=").append(status);
    sb.append(", sessionid='").append(sessionid).append('\'');
    sb.append(", owner='").append(owner).append('\'');
    sb.append(", signal='").append(signal).append('\'');
    sb.append('}');
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UmQueryDTO umQueryDto = (UmQueryDTO) o;
    return fromSeqNo == umQueryDto.fromSeqNo &&
        pageSize == umQueryDto.pageSize &&
        status == umQueryDto.status &&
        Objects.equals(msgid, umQueryDto.msgid) &&
        Objects.equals(sessionid, umQueryDto.sessionid) &&
        Objects.equals(owner, umQueryDto.owner) &&
        Objects.equals(signal, umQueryDto.signal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fromSeqNo, pageSize, msgid, status, sessionid, owner, signal);
  }
}
