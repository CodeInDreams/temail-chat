package com.syswin.temail.usermail.mongo.dto;

import com.syswin.temail.usermail.common.MongoEventEnum;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class MongoEventDTO<T> implements Serializable {

  private MongoEventEnum mongoEvent;
  private T dataDto;

  public MongoEventEnum getMongoEvent() {
    return mongoEvent;
  }

  public void setMongoEvent(MongoEventEnum mongoEvent) {
    this.mongoEvent = mongoEvent;
  }

  public T getDataDto() {
    return dataDto;
  }

  public void setDataDto(T dataDto) {
    this.dataDto = dataDto;
  }

  @Override
  public String toString() {
    return "MongoEventDTO{" +
        "mongoEvent=" + mongoEvent +
        ", dataDto=" + dataDto +
        '}';
  }
}
