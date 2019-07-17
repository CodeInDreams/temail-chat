/*
 * MIT License
 *
 * Copyright (c) 2019 Syswin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.syswin.temail.usermail.mongo.infrastructure.domain;

import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.CREATETIME;
import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.MSGID;
import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.OWNER;
import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.PARENTMSGID;
import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.SEQNO;
import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.SESSIONID;
import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.STATUS;
import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.UPDATETIME;
import static com.syswin.temail.usermail.common.ParamsKey.MongoCollectionFields.ZIPMSG;

import com.syswin.temail.usermail.mongo.domains.MongoUsermailReplyMsg;
import com.syswin.temail.usermail.mongo.dto.MongoQueryMsgReplyDTO;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class UsermailReplyMongoMapper {

  @Autowired
  MongoTemplate mongoTemplate;

  public static final String COLLECTION_NAME = "usermail_msg_reply";

  public void insert(MongoUsermailReplyMsg usermailReplyMsg) {
    mongoTemplate.save(usermailReplyMsg);
  }

  public List<MongoUsermailReplyMsg> listMsgReplys(MongoQueryMsgReplyDTO msgReplyDTO) {

    Criteria criteria = Criteria.where(PARENTMSGID).is(msgReplyDTO.getParentMsgid()).and(OWNER)
        .is(msgReplyDTO.getOwner());
    Query query = new Query();
    long fromSeqNo = msgReplyDTO.getFromSeqNo();
    String signal = msgReplyDTO.getSignal();
    int pageSize = msgReplyDTO.getPageSize();
    if (fromSeqNo != 0) {
      if ("before".equals(signal)) {
        query.addCriteria(criteria.and(SEQNO).lt(fromSeqNo)).with(new Sort(Direction.DESC, SEQNO)).limit(pageSize);
      } else if ("after".equals(signal)) {
        query.addCriteria(criteria.and(SEQNO).gt(fromSeqNo)).with(Sort.by(SEQNO)).limit(pageSize);
      }
    } else {
      query.addCriteria(criteria).with(new Sort(Direction.DESC, SEQNO)).limit(pageSize);
    }
    return mongoTemplate.find(query, MongoUsermailReplyMsg.class, COLLECTION_NAME);
  }

  public void deleteMsgReplysByMsgIds(String owner, List<String> msgIds) {
    Query query = Query.query(Criteria.where(OWNER).is(owner).and(MSGID).in(msgIds));
    mongoTemplate.remove(query, COLLECTION_NAME);
  }

  public void deleteMsgReplysBySessionId(String sessionId, String owner) {
    Query query = Query.query(Criteria.where(OWNER).is(owner).and(SESSIONID).is(sessionId));
    mongoTemplate.remove(query, COLLECTION_NAME);
  }

  public void deleteMsgReplysByParentIds(String owner, List<String> parentMsgIds) {
    Query query = Query.query(Criteria.where(OWNER).is(owner).and(PARENTMSGID).in(parentMsgIds));
    mongoTemplate.remove(query, COLLECTION_NAME);
  }

  public MongoUsermailReplyMsg selectMsgReplyByCondition(MongoUsermailReplyMsg usermailMsgReply) {
    Criteria criteria = Criteria.where(OWNER).is(usermailMsgReply.getOwner()).and(MSGID)
        .is(usermailMsgReply.getMsgid());
    Query query = Query.query(criteria);
    return mongoTemplate.findOne(query, MongoUsermailReplyMsg.class, COLLECTION_NAME);
  }

  public void updateDestroyAfterRead(String owner, String msgid, int status, int originalStatus) {
    Query query = Query
        .query(Criteria.where(STATUS).is(originalStatus).and(MSGID).is(msgid).and(OWNER).is(owner));
    Update update = new Update();
    update.set(STATUS, status);
    update.set(ZIPMSG, null);
    update.set(UPDATETIME, System.currentTimeMillis());
    mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
  }

  public void updateMsgReplysByParentIds(String owner, List<String> parentMsgIds, int status) {
    Query query = Query.query(Criteria.where(OWNER).is(owner).and(PARENTMSGID).in(parentMsgIds));
    Update update = new Update();
    update.set(STATUS, status);
    //这里获取的是系统的时间是没有问题的吧
    update.set(UPDATETIME, System.currentTimeMillis());
    mongoTemplate.updateMulti(query, update, COLLECTION_NAME);
  }

  public void deleteMsgReplysByStatus(String owner, int status) {
    Query query = Query.query(Criteria.where(OWNER).is(owner).and(STATUS).is(status));
    mongoTemplate.remove(query, COLLECTION_NAME);
  }

  public MongoUsermailReplyMsg selectLastUsermailReply(String parentMsgid, String owner, int status) {
    Query query = Query.query(
        Criteria.where(PARENTMSGID).is(parentMsgid).and(OWNER).is(owner).and(STATUS).is(status)).with(
        new Sort(Direction.DESC, SEQNO)).limit(1);
    return mongoTemplate.findOne(query, MongoUsermailReplyMsg.class, COLLECTION_NAME);
  }

  public void updateRevertUsermailReply(MongoUsermailReplyMsg usermailMsgReply, int originalStatus) {
    Criteria criteria = Criteria.where(STATUS).is(originalStatus).and(MSGID).is(usermailMsgReply.getMsgid())
        .and(OWNER).is(usermailMsgReply.getOwner());
    Update update = new Update();
    update.set(STATUS, usermailMsgReply.getStatus());
    update.set(ZIPMSG, null);
    update.set(UPDATETIME, System.currentTimeMillis());
    Query query = Query.query(criteria);
    mongoTemplate.updateFirst(query, update, COLLECTION_NAME);
  }

  public void deleteMsgReplyLessThan(LocalDate createTime, int batchNum) {
    Criteria criteria = Criteria.where(CREATETIME).lt(createTime);
    Query query = Query.query(criteria).limit(batchNum);
    mongoTemplate.remove(query, MongoUsermailReplyMsg.class);
  }

  public void deleteDomain(String domain, int pageSize) {
    Criteria criteria = Criteria.where(OWNER).regex("@" + domain);
    Query query = Query.query(criteria).limit(pageSize);
    mongoTemplate.remove(query, COLLECTION_NAME);
  }
}