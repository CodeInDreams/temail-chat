package com.syswin.temail.usermail.infrastructure.domain;


import com.syswin.temail.usermail.domains.UsermailMsgReply;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsermailMsgReplyMapper {

  int insert(UsermailMsgReply record);

  List<UsermailMsgReply> getMsgReplys(QueryMsgReplyDTO dto);

  int deleteBatchMsgReplyStatus(@Param("owner") String owner, @Param("msgIds") List<String> msgIds);

  int batchDeleteBySessionId(@Param("sessionid") String sessionId, @Param("owner") String owner);

  int deleteMsgByParentIdAndOwner(@Param("owner") String owner, @Param("parentMsgIds") List<String> parentMsgIds);

  UsermailMsgReply getMsgReplyByCondition(UsermailMsgReply usermailMsgReply);

  int destoryAfterRead(@Param("owner") String owner, @Param("msgid") String msgid, @Param("status") int status);

  int batchUpdateByParentMsgIds(@Param("owner") String owner, @Param("parentMsgIds") List<String> parentMsgIds,
      @Param("status") int status);

  int batchDeleteByStatus(@Param("owner") String owner, @Param("status") int status);

  UsermailMsgReply getLastUsermailReply(@Param("parentMsgid") String parentMsgid, @Param("owner") String owner,
      @Param("status") int status);

  int revertUsermailReply(UsermailMsgReply usermailMsgReply);
}