package com.syswin.temail.usermail.core;

public interface ITopicAdapter {

  long getTopicInfoPkId();

  long getTopicMemberPkId();

  long getTopicMsgPkId();

  long getTopicMsgSeqNo(String topicId);

  long getTopicSeqNo();

  String getTopicLastMsgId(String topicId, String temail);

  void setTopicLastMsgId(String topicId, String temail, String msgid);

  void removeTopicLastMsg(String topicId, String temail);
}
