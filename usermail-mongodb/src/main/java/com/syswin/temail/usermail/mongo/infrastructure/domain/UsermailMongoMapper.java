package com.syswin.temail.usermail.mongo.infrastructure.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;


public class UsermailMongoMapper {

  @Autowired
  MongoTemplate mongoTemplate;
}
