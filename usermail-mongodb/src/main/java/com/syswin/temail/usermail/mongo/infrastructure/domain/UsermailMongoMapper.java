package com.syswin.temail.usermail.mongo.infrastructure.domain;

import com.syswin.temail.usermail.mongo.domains.MongoUsermail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsermailMongoMapper {

  @Autowired
  MongoTemplate mongoTemplate;

  public void sendUsermailMsg(MongoUsermail mongoUsermail) {
    mongoTemplate.insert(mongoUsermail);
  }
}
