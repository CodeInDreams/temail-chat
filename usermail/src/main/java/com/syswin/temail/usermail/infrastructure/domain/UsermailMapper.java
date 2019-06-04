package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.dto.QueryTrashDto;
import com.syswin.temail.usermail.dto.TrashMailDto;
import com.syswin.temail.usermail.dto.UmQueryDto;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailMapper {

  void saveUsermail(Usermail usermail);

  List<Usermail> getUsermail(UmQueryDto umQueryDto);

  Usermail getUsermailByMsgid(@Param("msgid") String msgid, @Param("owner") String owner);

  List<Usermail> getLastUsermail(UmQueryDto umQueryDto);

  int revertUsermail(UmQueryDto umQueryDto);

  int removeMsg(@Param("msgIds") List<String> msgIds, @Param("owner") String owner);

  void destoryAfterRead(@Param("owner") String owner, @Param("msgid") String msgid, @Param("status") int status);

  int batchDeleteBySessionId(@Param("sessionid") String sessionId, @Param("owner") String owner);

  List<Usermail> getUsermailListByMsgid(@Param("msgid") String msgid);

  List<Usermail> getUsermailByFromToMsgIds(@Param("from") String from, @Param("msgIds") List<String> msgIds);

  void updateReplyCountAndLastReplyMsgid(@Param("msgid") String msgid, @Param("owner") String owner,
      @Param("count") int count, @Param("lastReplyMsgid") String lastReplyMsgid);

  int updateStatusByMsgIds(@Param("msgIds") List<String> msgIds, @Param("owner") String owner,
      @Param("status") int status);

  int removeMsgByStatus(@Param("trashMails") List<TrashMailDto> trashMails, @Param("owner") String owner,
      @Param("status") int status);

  int updateStatusByTemail(@Param("trashMails") List<TrashMailDto> trashMails, @Param("owner") String owner,
      @Param("status") int status);

  List<Usermail> getUsermailByStatus(QueryTrashDto queryDto);

}