package com.syswin.temail.mongo;

import com.google.gson.Gson;
import com.syswin.temail.mongo.infrastructure.domain.UsermailMongoMapper;
import com.syswin.temail.mongo.domains.MongoUsermail;

public class UsermailMongoService {

  private UsermailMongoMapper mongoMapper;
  private Gson gson = new Gson();

  public boolean parseMongoData(String mongoMessage){
    //解析json,根据event确定业务数据
    int event = 0;
    switch (event){
      case 1:
        mongoMapper.save(new MongoUsermail());
    }
    return true;
  }
}
