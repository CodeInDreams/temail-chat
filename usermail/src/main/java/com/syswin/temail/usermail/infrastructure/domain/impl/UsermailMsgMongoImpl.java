package com.syswin.temail.usermail.infrastructure.domain.impl;

import static com.syswin.temail.usermail.common.MongoEventEnum.MONGO_USERMAIL_EVENT;

import com.google.gson.Gson;
import com.syswin.temail.usermail.configuration.UsermailConfig;
import com.syswin.temail.usermail.core.IMqAdapter;
import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.dto.QueryTrashDTO;
import com.syswin.temail.usermail.dto.RevertMailDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import com.syswin.temail.usermail.dto.UmQueryDTO;
import com.syswin.temail.usermail.infrastructure.domain.IUsermailMsgDB;
import com.syswin.temail.usermail.mongo.dto.MongoEventDTO;
import com.syswin.temail.usermail.mongo.infrastructure.domain.UsermailMongoMapper;
import java.time.LocalDate;
import java.util.List;

public class UsermailMsgMongoImpl implements IUsermailMsgDB {

  private final IMqAdapter mqAdapter;
  private final UsermailMongoMapper usermailMongoMapper;
  private final UsermailConfig usermailConfig;
  private final Gson gson = new Gson();

  public UsermailMsgMongoImpl(IMqAdapter mqAdapter,
      UsermailMongoMapper usermailMongoMapper,
      UsermailConfig usermailConfig) {
    this.mqAdapter = mqAdapter;
    this.usermailMongoMapper = usermailMongoMapper;
    this.usermailConfig = usermailConfig;
  }

  @Override
  public void insertUsermail(UsermailDO usermail) {
    //usermail转MongoDB event
    MongoEventDTO<UsermailDO> eventDTO = new MongoEventDTO<>(MONGO_USERMAIL_EVENT, usermail);
    mqAdapter.sendMessage(usermailConfig.mongoTopic, usermail.getFrom(), gson.toJson(eventDTO));
  }

  @Override
  public List<UsermailDO> listUsermails(UmQueryDTO umQueryDto) {
    return null;
  }

  @Override
  public UsermailDO selectByMsgidAndOwner(String msgid, String owner) {
    return null;
  }

  @Override
  public List<UsermailDO> listLastUsermails(UmQueryDTO umQueryDto) {
    return null;
  }

  @Override
  public int countRevertUsermail(RevertMailDTO revertMail) {
    return 0;
  }

  @Override
  public int deleteMsg(List<String> msgIds, String owner) {
    return 0;
  }

  @Override
  public void updateDestroyAfterReadStatus(String owner, String msgid, int status) {

  }

  @Override
  public int deleteBySessionIdAndOwner(String sessionId, String owner) {
    return 0;
  }

  @Override
  public List<UsermailDO> listUsermailsByMsgid(String msgid) {
    return null;
  }

  @Override
  public List<UsermailDO> listUsermailsByFromToMsgIds(String from, List<String> msgIds) {
    return null;
  }

  @Override
  public void updateReplyCountAndLastReplyMsgid(String msgid, String owner, int count, String lastReplyMsgid) {

  }

  @Override
  public int updateStatusByMsgIds(List<String> msgIds, String owner, int status) {
    return 0;
  }

  @Override
  public int deleteMsgByStatus(List<TrashMailDTO> trashMails, String owner, int status) {
    return 0;
  }

  @Override
  public int updateRevertMsgFromTrashStatus(List<TrashMailDTO> trashMails, String owner, int status) {
    return 0;
  }

  @Override
  public List<UsermailDO> listUsermailsByStatus(QueryTrashDTO queryDto) {
    return null;
  }

  @Override
  public int deleteMsgLessThan(LocalDate createTime, int batchNum) {
    return 0;
  }

  @Override
  public int deleteDomain(String domain, int pageSize) {
    return 0;
  }
}
