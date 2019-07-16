package com.syswin.temail.usermail.mongo.dto;

import com.syswin.temail.usermail.common.MongoEventEnum;
import com.syswin.temail.usermail.common.MongoMsgEventEnum;
import com.syswin.temail.usermail.common.MongoMsgReplyEventEnum;
import java.io.Serializable;
import lombok.Data;

@Data
public class MongoEventDTO<T> implements Serializable {

  private MongoEventEnum mongoEvent;
  private T dataDto;
  private MongoMsgEventEnum msgEventEnum;
  private MongoMsgReplyEventEnum msgReplyEventEnum;

  public MongoEventDTO() {
  }

  public MongoEventDTO(MongoEventEnum mongoEvent, T dataDto,
      MongoMsgEventEnum msgEventEnum) {
    this.mongoEvent = mongoEvent;
    this.dataDto = dataDto;
    this.msgEventEnum = msgEventEnum;
  }

  public MongoEventDTO(MongoEventEnum mongoEvent, T dataDto,
      MongoMsgReplyEventEnum msgReplyEventEnum) {
    this.mongoEvent = mongoEvent;
    this.dataDto = dataDto;
    this.msgReplyEventEnum = msgReplyEventEnum;
  }
}
