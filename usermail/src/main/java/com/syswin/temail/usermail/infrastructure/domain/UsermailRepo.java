package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.dto.QueryTrashDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import com.syswin.temail.usermail.dto.UmQueryDTO;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailRepo {

  void saveUsermail(Usermail usermail);

  List<Usermail> getUsermail(UmQueryDTO umQueryDto);

  Usermail getUsermailByMsgid(String msgid, String owner);

  List<Usermail> getLastUsermail(UmQueryDTO umQueryDto);

  int revertUsermail(UmQueryDTO umQueryDto);

  int removeMsg(List<String> msgIds, String owner);

  void destoryAfterRead(String owner, String msgid, int status);

  int batchDeleteBySessionId(String sessionId, String owner);

  List<Usermail> getUsermailListByMsgid(String msgid);

  List<Usermail> getUsermailByFromToMsgIds(String from, List<String> msgIds);

  void updateReplyCountAndLastReplyMsgid(String msgid, String owner, int count, String lastReplyMsgid);

  int updateStatusByMsgIds(List<String> msgIds, String owner, int status);

  int removeMsgByStatus(List<TrashMailDTO> trashMails, String owner, int status);

  int updateStatusByTemail(List<TrashMailDTO> trashMails, String owner, int status);

  List<Usermail> getUsermailByStatus(QueryTrashDTO queryDto);

}