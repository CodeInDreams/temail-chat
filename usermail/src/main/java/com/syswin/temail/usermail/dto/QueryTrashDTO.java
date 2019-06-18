package com.syswin.temail.usermail.dto;

import java.sql.Timestamp;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@NoArgsConstructor
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
