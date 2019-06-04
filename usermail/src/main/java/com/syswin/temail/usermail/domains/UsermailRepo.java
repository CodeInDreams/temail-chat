package com.syswin.temail.usermail.domains;

import com.syswin.temail.usermail.dto.QueryTrashDto;
import com.syswin.temail.usermail.dto.TrashMailDto;
import com.syswin.temail.usermail.dto.UmQueryDto;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailRepo {

  void saveUsermail(Usermail usermail);

  List<Usermail> getUsermail(UmQueryDto umQueryDto);

  Usermail getUsermailByMsgid(String msgid, String owner);

  List<Usermail> getLastUsermail(UmQueryDto umQueryDto);

  int revertUsermail(UmQueryDto umQueryDto);

  int removeMsg(List<String> msgIds, String owner);

  void destoryAfterRead(String owner, String msgid, int status);

  int batchDeleteBySessionId(String sessionId, String owner);

  List<Usermail> getUsermailListByMsgid(String msgid);

  List<Usermail> getUsermailByFromToMsgIds(String from, List<String> msgIds);

  void updateReplyCountAndLastReplyMsgid(String msgid, String owner, int count, String lastReplyMsgid);

  int updateStatusByMsgIds(List<String> msgIds, String owner, int status);

  int removeMsgByStatus(List<TrashMailDto> trashMails, String owner, int status);

  int updateStatusByTemail(List<TrashMailDto> trashMails, String owner, int status);

  List<Usermail> getUsermailByStatus(QueryTrashDto queryDto);

}