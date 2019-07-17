package com.syswin.temail.usermail.mongo;

import com.google.gson.Gson;
import com.syswin.temail.usermail.common.MongoEventEnum;
import com.syswin.temail.usermail.common.MongoMsgEventEnum;
import com.syswin.temail.usermail.core.IMqConsumer;
import com.syswin.temail.usermail.mongo.application.UsermailMongoService;
import com.syswin.temail.usermail.mongo.dto.MongoEventDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class UsermailMongoMQConsumer implements IMqConsumer {

  private Gson gson = new Gson();
  @Autowired
  private UsermailMongoService mongoService;

  @Override
  public boolean consumer(String message) {
    MongoEventDTO mongoEventDTO = gson.fromJson(message, MongoEventDTO.class);
    MongoEventEnum mongoEvent = mongoEventDTO.getMongoEvent();
    String dataDTO = mongoEventDTO.getDataDto();
    if (mongoEvent.equals(MongoEventEnum.MONGO_USERMAIL_EVENT)) {
      MongoMsgEventEnum msgEventEnum = mongoEventDTO.getMsgEventEnum();
      mongoService.parseMongoData(dataDTO, msgEventEnum);
    } else if (mongoEvent.equals(MongoEventEnum.MONGO_USERMAIL_REPLY_EVENT)) {

    }
    return true;
  }
}
