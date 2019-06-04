package com.syswin.temail.usermail.core;

public interface IUsermailAdapter {

  long getPkID();

  long getMsgSeqNo(String from, String to, String owner);

  long getMsgReplyPkID();

  long getMsgReplySeqNo(String parentMsgid, String owner);

  long getUsermailBlacklistPkID();

  void setLastMsgId(String owner, String to, String lastMsgId);

  String getLastMsgId(String owner, String to);

  void deleteLastMsgId(String owner, String to);
}
