package com.syswin.temail.usermail.mongo.infrastructure.domain;

import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.FROM;
import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.MSGID;
import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.OWNER;
import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.SEQNO;
import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.SESSIONID;
import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.STATUS;
import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.UPDATETIME;

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

  /**
   * 根据用户会话id查询消息列表
   *
   * @param fromSeqNo 拉取位置
   * @param pageSize 拉取条数
   * @param sessionid 会话id
   * @param owner 消息所属人
   * @param signal 向前向后拉取标志
   * @return List<MongoUsermail>
   * @See MongoUsermail
   */
  public List<MongoUsermail> listUsermails(long fromSeqNo, int pageSize, String sessionid, String owner,
      String signal) {
    Query query = new Query();
    Criteria criteria = new Criteria();
    criteria.and(SESSIONID).is(sessionid).and(OWNER).is(owner);
    if (fromSeqNo > 0l) {
      if ("after".equals(signal)) {
        criteria.and(SEQNO).gt(fromSeqNo);
        query.with(new Sort(Direction.ASC, SEQNO));
      }
      if ("before".equals(signal)) {
        criteria.and(SEQNO).lt(fromSeqNo);
        query.with(new Sort(Direction.DESC, SEQNO));
      }
    } else {
      query.with(new Sort(Direction.DESC, SEQNO));
    }
    query.addCriteria(criteria);
    query.limit(pageSize);
    List<MongoUsermail> list = mongoTemplate.find(query, MongoUsermail.class);
    return list;
  }

  public MongoUsermail selectByMsgidAndOwner(String msgid, String owner) {
    Query query = new Query();
    Criteria criteria = new Criteria();
    criteria.where(MSGID).is(msgid).and(OWNER).is(owner);
    query.addCriteria(criteria);
    MongoUsermail mongoUsermail = mongoTemplate.findOne(query, MongoUsermail.class);
    return mongoUsermail;
  }

  public MongoUsermail listLastUsermails(String sessionid, String owner, long seqNo) {
    Query query = new Query();
    Criteria criteria = new Criteria();
    criteria.where(SESSIONID).is(sessionid).and(OWNER).is(owner);
    query.with(new Sort(Direction.DESC, SEQNO)).limit(1);
    query.addCriteria(criteria);
    MongoUsermail mongoUsermail = mongoTemplate.findOne(query, MongoUsermail.class);
    return mongoUsermail;
  }

  public List<MongoUsermail> listUsermailsByMsgid(String msgid) {
    Query query = new Query(Criteria.where(MSGID).is(msgid));
    return mongoTemplate.find(query, MongoUsermail.class);
  }

  public List<MongoUsermail> listUsermailsByFromToMsgIds(String from, List<String> msgIds) {
    Query query = new Query(Criteria.where(FROM).is(from).and(MSGID).in(msgIds));
    return mongoTemplate.find(query, MongoUsermail.class);
  }

  public List<MongoUsermail> listUsermailsByStatus(int status, String owner, String signal, Date updateTime,
      int pageSize) {
    Query query = new Query();
    Criteria criteria = new Criteria();
    criteria.and(STATUS).is(status).and(OWNER).is(owner);
    if (updateTime != null) {
      if ("after".equals(signal)) {
        criteria.and(UPDATETIME).gt(updateTime);
      }
      if ("before".equals(signal)) {
        criteria.and(UPDATETIME).lt(updateTime);
      }
    }
    query.addCriteria(criteria);
    query.with(new Sort(Direction.DESC, UPDATETIME)).limit(pageSize);
    return mongoTemplate.find(query, MongoUsermail.class);
  }

}
