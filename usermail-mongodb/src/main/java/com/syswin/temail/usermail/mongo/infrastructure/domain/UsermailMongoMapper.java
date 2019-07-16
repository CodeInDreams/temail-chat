package com.syswin.temail.usermail.mongo.infrastructure.domain;

import com.syswin.temail.usermail.mongo.domains.MongoUsermail;
import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class UsermailMongoMapper {


  private final MongoTemplate mongoTemplate;

  public UsermailMongoMapper(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public void sendUsermailMsg(MongoUsermail mongoUsermail) {
    mongoTemplate.insert(mongoUsermail);
  }

  public List<MongoUsermail> listUsermails(long fromSeqNo, int pageSize, String sessionid, String owner,
      String signal) {
    Query query = new Query();
    Criteria criteria = new Criteria();
    criteria.where("sessionid").is(sessionid).and("owner").is(owner);
    if (fromSeqNo == 0l) {
      if ("after".equals(signal)) {
        criteria.and("seqNo").gt(fromSeqNo);
        query.with(new Sort(Direction.ASC, "seqNo"));
      }
      if ("before".equals(signal)) {
        criteria.and("seqNo").lt(fromSeqNo);
        query.with(new Sort(Direction.DESC, "seqNo"));
      }
    } else {
      query.with(new Sort(Direction.DESC, "seqNo"));
    }
    query.addCriteria(criteria);
    query.limit(pageSize);
    List<MongoUsermail> list = mongoTemplate.find(query, MongoUsermail.class);
    return list;
  }

  public MongoUsermail selectByMsgidAndOwner(String msgid, String owner) {
    Query query = new Query();
    Criteria criteria = new Criteria();
    criteria.where("msgid").is(msgid).and("owner").is(owner);
    query.addCriteria(criteria);
    MongoUsermail mongoUsermail = mongoTemplate.findOne(query, MongoUsermail.class);
    return mongoUsermail;
  }

  public MongoUsermail listLastUsermails(String sessionid, String owner, long seqNo) {
    Query query = new Query();
    Criteria criteria = new Criteria();
    criteria.where("sessionid").is(sessionid).and("owner").is(owner);
    query.with(new Sort(Direction.DESC, "seqNo")).limit(1);
    query.addCriteria(criteria);
    MongoUsermail mongoUsermail = mongoTemplate.findOne(query, MongoUsermail.class);
    return mongoUsermail;
  }

  public List<MongoUsermail> listUsermailsByMsgid(String msgid) {
    Query query = new Query(Criteria.where("msgid").is(msgid));
    return mongoTemplate.find(query, MongoUsermail.class);
  }

  public List<MongoUsermail> listUsermailsByFromToMsgIds(String from, List<String> msgIds) {
    Query query = new Query(Criteria.where("from").is(from).and("msgid").in(msgIds));
    return mongoTemplate.find(query, MongoUsermail.class);
  }

  public List<MongoUsermail> listUsermailsByStatus(int status, String owner, String signal, Date updateTime,
      int pageSize) {
    Query query = new Query();
    Criteria criteria = new Criteria();
    criteria.and("status").is(status).and("owner").is(owner);
    if (updateTime != null) {
      if ("after".equals(signal)) {
        criteria.and("updateTime").gt(updateTime);
      }
      if ("before".equals(signal)) {
        criteria.and("updateTime").lt(updateTime);
      }
    }
    query.addCriteria(criteria);
    query.with(new Sort(Direction.DESC, "updateTime")).limit(pageSize);
    return mongoTemplate.find(query, MongoUsermail.class);
  }

}
