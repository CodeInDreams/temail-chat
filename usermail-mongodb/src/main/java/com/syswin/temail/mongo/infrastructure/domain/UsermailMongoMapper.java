package com.syswin.temail.mongo.infrastructure.domain;

import com.syswin.temail.mongo.domains.MongoUsermail;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsermailMongoMapper extends MongoRepository<MongoUsermail, Long> {

}
