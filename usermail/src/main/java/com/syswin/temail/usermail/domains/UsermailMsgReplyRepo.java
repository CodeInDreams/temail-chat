package com.syswin.temail.usermail.domains;


import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import java.util.List;

public interface UsermailMsgReplyRepo {

  int insert(UsermailMsgReply record);

  List<UsermailMsgReply> getMsgReplys(QueryMsgReplyDTO dto);

  int deleteBatchMsgReplyStatus(String owner, List<String> msgIds);

  int batchDeleteBySessionId(String sessionId, String owner);

  int deleteMsgByParentIdAndOwner(String owner, List<String> parentMsgIds);

  UsermailMsgReply getMsgReplyByCondition(UsermailMsgReply usermailMsgReply);

  int destoryAfterRead(String owner, String msgid, int status);

  int batchUpdateByParentMsgIds(String owner, List<String> parentMsgIds, int status);

  int batchDeleteByStatus(String owner, int status);

  UsermailMsgReply getLastUsermailReply(String parentMsgid, String owner, int status);

  int revertUsermailReply(UsermailMsgReply usermailMsgReply);
}