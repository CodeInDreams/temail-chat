package com.syswin.temail.usermail.dto;

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

  public UmQueryDTO(String sessionid, String owner) {
    this.sessionid = sessionid;
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
