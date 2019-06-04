package com.syswin.temail.usermail.core;

import java.util.List;

public interface IGroupmailAdapter {

  long getGroupmailPKid();

  long getGroupmailMemberPKid();

  long getGroupmailMsgPkID();

  long getMsgSeqNo(String groupTemail);

//  void removeMsgSeqNo(String groupTemail);

  long getGroupmailMsgReplyPkID();

  long getMsgReplySeqNo(String parentMsgid, String groupTemail);

  void removeMsgsReplySeqNo(List<String> parentMsgids);

  long getGroupmailAtMsgPkID();

  long getGroupmailAtMsgSeqNo(String msgid, String groupTemail);

  void removeGroupmailAtMsgSeqNo(String msgid);

  long getGroupmailAtMemberPkID();

  long getGroupmailAtMemberSeqNo(String groupTemail, String temail);

//  void removeGroupmailAtMemberSeqNo(String groupTemail);

  long getGroupmailBlacklistPkID();

  void setGroupmailLastMsgId(String groupTemail, String msgId);

  String getGroupmailLastMsgId(String groupTemail);

  void removeGroupmailLastMsgId(String groupTemail);

  void removeCacheByGroupTemail(String groupTemail);

}
