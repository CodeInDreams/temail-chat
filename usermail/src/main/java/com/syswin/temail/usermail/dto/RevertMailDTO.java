package com.syswin.temail.usermail.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Alias("revertMail")
@Setter
@Getter
@ToString
@NoArgsConstructor
public class RevertMailDTO implements Serializable {

  private String owner;
  private String msgid;
  private Integer originalStatus;
  private Integer revertStatus;

  public RevertMailDTO(String owner, String msgid, Integer originalStatus, Integer revertStatus) {
    this.owner = owner;
    this.msgid = msgid;
    this.originalStatus = originalStatus;
    this.revertStatus = revertStatus;
  }
}
