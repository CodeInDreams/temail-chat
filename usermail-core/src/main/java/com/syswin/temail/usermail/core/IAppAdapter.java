package com.syswin.temail.usermail.core;

import java.util.List;

public interface IAppAdapter {

  long getAppConfigPKId();

  long getAppPKid();

  long getAppMemberPKid();

  long getAppMsgPkID();

  long getMsgSeqNo(String appTemail);

  long getAppMsgReplyPkID();

  long getMsgReplySeqNo(String parentMsgid, String appTemail);

  void removeMsgsReplySeqNo(List<String> parentMsgids);

  long getAppAtMsgPkID();

  long getAppAtMsgSeqNo(String msgid, String appTemail);

  void removeAppAtMsgSeqNo(String msgid);

  long getAppAtMemberPkID();

  long getAppAtMemberSeqNo(String appTemail, String temail);

  long getAppBlacklistPkID();

  void setAppLastMsgId(String appTemail, String msgId);

  String getAppLastMsgId(String appTemail);

  void removeAppLastMsgId(String appTemail);

  void removeCacheByAppTemail(String appTemail);

  Object getTemailInfoByTemail(String temail);

  void setTemailInfoByTemail(String temail, Object temailInfo, Long expireTime);
}
