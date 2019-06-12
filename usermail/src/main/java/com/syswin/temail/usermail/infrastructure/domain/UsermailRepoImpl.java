package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.domains.UsermailRepo;
import com.syswin.temail.usermail.dto.QueryTrashDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import com.syswin.temail.usermail.dto.UmQueryDTO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsermailRepoImpl implements UsermailRepo {

  private final UsermailMapper usermailMapper;

  @Autowired
  public UsermailRepoImpl(UsermailMapper usermailMapper) {
    this.usermailMapper = usermailMapper;
  }

  @Override
  public void saveUsermail(Usermail usermail) {
    usermailMapper.saveUsermail(usermail);
  }

  @Override
  public List<Usermail> getUsermail(UmQueryDTO umQueryDto) {
    return usermailMapper.getUsermail(umQueryDto);
  }

  @Override
  public Usermail getUsermailByMsgid(String msgid, String owner) {
    return usermailMapper.getUsermailByMsgid(msgid, owner);
  }

  @Override
  public List<Usermail> getLastUsermail(UmQueryDTO umQueryDto) {
    return usermailMapper.getLastUsermail(umQueryDto);
  }

  @Override
  public int revertUsermail(UmQueryDTO umQueryDto) {
    return usermailMapper.revertUsermail(umQueryDto);
  }

  @Override
  public int removeMsg(List<String> msgIds, String owner) {
    return usermailMapper.removeMsg(msgIds, owner);
  }

  @Override
  public void destoryAfterRead(String owner, String msgid, int status) {
    usermailMapper.destoryAfterRead(owner, msgid, status);
  }

  @Override
  public int batchDeleteBySessionId(String sessionId, String owner) {
    return usermailMapper.batchDeleteBySessionId(sessionId, owner);
  }

  @Override
  public List<Usermail> getUsermailListByMsgid(String msgid) {
    return usermailMapper.getUsermailListByMsgid(msgid);
  }

  @Override
  public List<Usermail> getUsermailByFromToMsgIds(String from, List<String> msgIds) {
    return usermailMapper.getUsermailByFromToMsgIds(from, msgIds);
  }

  @Override
  public void updateReplyCountAndLastReplyMsgid(String msgid, String owner, int count, String lastReplyMsgid) {
    usermailMapper.updateReplyCountAndLastReplyMsgid(msgid, owner, count, lastReplyMsgid);
  }

  @Override
  public int updateStatusByMsgIds(List<String> msgIds, String owner, int status) {
    return usermailMapper.updateStatusByMsgIds(msgIds, owner, status);
  }

  @Override
  public int removeMsgByStatus(List<TrashMailDTO> trashMails, String owner, int status) {
    return usermailMapper.removeMsgByStatus(trashMails, owner, status);
  }

  @Override
  public int updateStatusByTemail(List<TrashMailDTO> trashMails, String owner, int status) {
    return usermailMapper.updateStatusByTemail(trashMails, owner, status);
  }

  @Override
  public List<Usermail> getUsermailByStatus(QueryTrashDTO queryDto) {
    return usermailMapper.getUsermailByStatus(queryDto);
  }

}