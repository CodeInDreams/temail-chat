package com.syswin.temail.usermail.infrastructure.domain.impl;

import com.syswin.temail.usermail.domains.UsermailMsgReply;
import com.syswin.temail.usermail.infrastructure.domain.UsermailMsgReplyRepo;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailMsgReplyMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsermailMsgReplyRepoImpl implements UsermailMsgReplyRepo {

  private final UsermailMsgReplyMapper usermailMsgReplyMapper;

  @Autowired
  public UsermailMsgReplyRepoImpl(UsermailMsgReplyMapper usermailMsgReplyMapper) {
    this.usermailMsgReplyMapper = usermailMsgReplyMapper;
  }

  @Override
  public int insert(UsermailMsgReply record) {
    return usermailMsgReplyMapper.insert(record);
  }

  @Override
  public List<UsermailMsgReply> getMsgReplys(QueryMsgReplyDTO dto) {
    return usermailMsgReplyMapper.getMsgReplys(dto);
  }

  @Override
  public int deleteBatchMsgReplyStatus(String owner, List<String> msgIds) {
    return usermailMsgReplyMapper.deleteBatchMsgReplyStatus(owner, msgIds);
  }

  @Override
  public int batchDeleteBySessionId(String sessionId, String owner) {
    return usermailMsgReplyMapper.batchDeleteBySessionId(sessionId, owner);
  }

  @Override
  public int deleteMsgByParentIdAndOwner(String owner, List<String> parentMsgIds) {
    return usermailMsgReplyMapper.deleteMsgByParentIdAndOwner(owner, parentMsgIds);
  }

  @Override
  public UsermailMsgReply getMsgReplyByCondition(UsermailMsgReply usermailMsgReply) {
    return usermailMsgReplyMapper.getMsgReplyByCondition(usermailMsgReply);
  }

  @Override
  public int destoryAfterRead(String owner, String msgid, int status) {
    return usermailMsgReplyMapper.destoryAfterRead(owner, msgid, status);
  }

  @Override
  public int batchUpdateByParentMsgIds(String owner, List<String> parentMsgIds, int status) {
    return usermailMsgReplyMapper.batchUpdateByParentMsgIds(owner, parentMsgIds, status);
  }

  @Override
  public int batchDeleteByStatus(String owner, int status) {
    return usermailMsgReplyMapper.batchDeleteByStatus(owner, status);
  }

  @Override
  public UsermailMsgReply getLastUsermailReply(String parentMsgid, String owner, int status) {
    return usermailMsgReplyMapper.getLastUsermailReply(parentMsgid, owner, status);
  }

  @Override
  public int revertUsermailReply(UsermailMsgReply usermailMsgReply) {
    return usermailMsgReplyMapper.revertUsermailReply(usermailMsgReply);
  }
}