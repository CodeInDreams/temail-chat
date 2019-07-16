package com.syswin.temail.usermail.mongo;

import com.google.gson.Gson;
import com.syswin.temail.usermail.common.MongoMsgEventEnum;
import com.syswin.temail.usermail.mongo.domains.MongoUsermail;
import com.syswin.temail.usermail.mongo.dto.MongoEventDTO;
import com.syswin.temail.usermail.mongo.infrastructure.domain.UsermailMongoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsermailMongoService {

  @Autowired
  private UsermailMongoMapper mongoMapper;
  private Gson gson = new Gson();

  public boolean parseMongoData(String mongoMessage, MongoMsgEventEnum msgEventEnum) {
    switch (msgEventEnum) {
      case SEND_USERMAIL_MSG:
        MongoEventDTO<MongoUsermail> mongoEventDTO = gson.fromJson(mongoMessage, MongoEventDTO.class);
        mongoMapper.sendUsermailMsg(mongoEventDTO.getDataDto());

    }

    return true;
  }
}
