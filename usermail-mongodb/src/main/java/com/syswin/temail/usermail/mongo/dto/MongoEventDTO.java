package com.syswin.temail.usermail.mongo.dto;

import com.syswin.temail.usermail.common.MongoEventEnum;
import com.syswin.temail.usermail.common.MongoMsgEventEnum;
import com.syswin.temail.usermail.common.MongoMsgReplyEventEnum;
import java.io.Serializable;
import lombok.Data;

@Data
public class MongoEventDTO implements Serializable {

  private MongoEventEnum mongoEvent;
  private String dataDto;
  private MongoMsgEventEnum msgEventEnum;
  private MongoMsgReplyEventEnum msgReplyEventEnum;

  public MongoEventDTO() {
  }

  public MongoEventDTO(MongoEventEnum mongoEvent, String dataDto,
      MongoMsgEventEnum msgEventEnum) {
    this.mongoEvent = mongoEvent;
    this.dataDto = dataDto;
    this.msgEventEnum = msgEventEnum;
  }

  public MongoEventDTO(MongoEventEnum mongoEvent, String dataDto,
      MongoMsgReplyEventEnum msgReplyEventEnum) {
    this.mongoEvent = mongoEvent;
    this.dataDto = dataDto;
    this.msgReplyEventEnum = msgReplyEventEnum;
  }
}
