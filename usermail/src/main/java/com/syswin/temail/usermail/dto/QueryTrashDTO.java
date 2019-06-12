package com.syswin.temail.usermail.dto;

import java.sql.Timestamp;
import java.util.Objects;
import org.apache.ibatis.type.Alias;

@Alias("QueryTrashDTO")
public class QueryTrashDTO implements java.io.Serializable {

  private Timestamp updateTime;
  private int pageSize;
  private int status;
  private String signal;
  private String owner;

  public QueryTrashDTO(Timestamp updateTime, int pageSize, int status, String signal, String owner) {
    this.updateTime = updateTime;
    this.pageSize = pageSize;
    this.status = status;
    this.signal = signal;
    this.owner = owner;
  }

  public QueryTrashDTO() {
  }

  public Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Timestamp updateTime) {
    this.updateTime = updateTime;
  }

  public int getPageSize() {
    return pageSize;
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
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
    return "QueryTrashDTO{" +
        "updateTime=" + updateTime +
        ", pageSize=" + pageSize +
        ", status=" + status +
        ", signal='" + signal + '\'' +
        ", owner='" + owner + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    QueryTrashDTO that = (QueryTrashDTO) o;
    return pageSize == that.pageSize &&
        status == that.status &&
        updateTime.equals(that.updateTime) &&
        signal.equals(that.signal) &&
        owner.equals(that.owner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(updateTime, pageSize, status, signal, owner);
  }
}
