package com.syswin.temail.usermail.mongo.application;

import com.google.gson.Gson;
import com.syswin.temail.usermail.common.MongoMsgEventEnum;
import com.syswin.temail.usermail.mongo.domains.MongoUsermail;
import com.syswin.temail.usermail.mongo.infrastructure.domain.UsermailMongoMapper;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsermailMongoService {

  @Autowired
  private UsermailMongoMapper mongoMapper;
  private Gson gson = new Gson();

  public boolean parseMongoData(String dataDTO, MongoMsgEventEnum msgEventEnum) {
    switch (msgEventEnum) {
      case SEND_USERMAIL_MSG:
        MongoUsermail mongoUsermail = gson.fromJson(dataDTO, MongoUsermail.class);
        mongoUsermail.setLastReplyMsgId("");
        mongoUsermail.setReplyCount(0);
        mongoUsermail.setCreateTime(new Date());
        mongoUsermail.setUpdateTime(new Date());
        mongoMapper.insertUsermail(mongoUsermail);
    }

    return true;
  }
}
