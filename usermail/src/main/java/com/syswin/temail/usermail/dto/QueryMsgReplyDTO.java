package com.syswin.temail.usermail.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Alias("QueryMsgReplyDTO")
public class QueryMsgReplyDTO implements Serializable {

  private long fromSeqNo;
  private int pageSize;
  private String msgid;
  private int status;
  private String parentMsgid;
  private String signal;
  private String owner;
}
