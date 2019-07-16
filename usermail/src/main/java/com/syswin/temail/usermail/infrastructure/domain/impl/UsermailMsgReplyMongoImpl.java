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

package com.syswin.temail.usermail.infrastructure.domain.impl;

import com.google.gson.Gson;
import com.syswin.temail.usermail.common.MongoEventEnum;
import com.syswin.temail.usermail.common.MongoMsgReplyEventEnum;
import com.syswin.temail.usermail.configuration.UsermailConfig;
import com.syswin.temail.usermail.core.IMqAdapter;
import com.syswin.temail.usermail.domains.UsermailMsgReplyDO;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import com.syswin.temail.usermail.infrastructure.domain.IUsermailMsgReplyDB;
import com.syswin.temail.usermail.mongo.dto.MongoEventDTO;
import com.syswin.temail.usermail.mongo.dto.UsermailReplyMsgDataDTO;
import com.syswin.temail.usermail.mongo.infrastructure.domain.UsermailReplyMongoMapper;
import java.time.LocalDate;
import java.util.List;

/**
 * @program: temail-chat
 * @description:
 * @author: xiao
 * @create: 2019-07-12 14:08
 **/

public class UsermailMsgReplyMongoImpl implements IUsermailMsgReplyDB {

  private final IMqAdapter mqAdapter;
  private final UsermailReplyMongoMapper replyMongoMapper;
  private final UsermailConfig usermailConfig;
  private final Gson gson = new Gson();


  public UsermailMsgReplyMongoImpl(IMqAdapter mqAdapter,
      UsermailReplyMongoMapper replyMongoMapper, UsermailConfig usermailConfig) {
    this.mqAdapter = mqAdapter;
    this.replyMongoMapper = replyMongoMapper;
    this.usermailConfig = usermailConfig;
  }

  @Override
  public int insert(UsermailMsgReplyDO usermailMsgReply) {
    UsermailReplyMsgDataDTO usermailReplyMsgDataDTO = new UsermailReplyMsgDataDTO();
    usermailReplyMsgDataDTO.setFrom(usermailMsgReply.getFrom());
    //....
    MongoEventDTO mongoEventDTO = new MongoEventDTO(MongoEventEnum.MONGO_USERMAIL_REPLY_EVENT, usermailReplyMsgDataDTO,
        MongoMsgReplyEventEnum.SEND_USERMAIL_REPLY_MSG);
    mqAdapter.sendMessage(usermailConfig.mongoTopic, usermailMsgReply.getFrom(), gson.toJson(mongoEventDTO));
    return 0;
  }

  @Override
  public List<UsermailMsgReplyDO> listMsgReplys(QueryMsgReplyDTO dto) {
    return null;
  }

  @Override
  public int deleteMsgReplysByMsgIds(String owner, List<String> msgIds) {
    return 0;
  }

  @Override
  public int deleteMsgReplysBySessionId(String sessionId, String owner) {
    return 0;
  }

  @Override
  public int deleteMsgReplysByParentIds(String owner, List<String> parentMsgIds) {
    return 0;
  }

  @Override
  public UsermailMsgReplyDO selectMsgReplyByCondition(UsermailMsgReplyDO usermailMsgReply) {
    return null;
  }

  @Override
  public int updateDestroyAfterRead(String owner, String msgid, int status) {
    return 0;
  }

  @Override
  public int updateMsgReplysByParentIds(String owner, List<String> parentMsgIds, int status) {
    return 0;
  }

  @Override
  public int deleteMsgReplysByStatus(String owner, int status) {
    return 0;
  }

  @Override
  public UsermailMsgReplyDO selectLastUsermailReply(String parentMsgid, String owner, int status) {
    return null;
  }

  @Override
  public int updateRevertUsermailReply(UsermailMsgReplyDO usermailMsgReply) {
    return 0;
  }

  @Override
  public int deleteMsgReplyLessThan(LocalDate createTime, int batchNum) {
    return 0;
  }

  @Override
  public int deleteDomain(String domain, int pageSize) {
    return 0;
  }
}