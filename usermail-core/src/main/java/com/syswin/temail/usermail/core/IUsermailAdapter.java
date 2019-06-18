package com.syswin.temail.usermail.core;

public interface IUsermailAdapter {

  /**
   * 获取主键id
   *
   * @return PkID
   */
  long getPkID();

  /**
   * 获取单聊消息序列号
   *
   * @param from 发送者
   * @param to 接收者
   * @param owner 消息所属人
   * @return 序列号
   */
  long getMsgSeqNo(String from, String to, String owner);

  /**
   * 获取回复消息PkId
   *
   * @return PkID
   */
  long getMsgReplyPkID();

  /**
   * 获取回复消息序列号
   *
   * @param parentMsgid 父消息id
   * @param owner 消息所属人
   */
  long getMsgReplySeqNo(String parentMsgid, String owner);

  /**
   * 获取黑名单PkID
   *
   * @return PkID
   */
  long getUsermailBlacklistPkID();

  /**
   * 为收件箱中每条单聊消息设置最新回复消息id
   *
   * @param owner 消息所属人
   * @param to 接收者
   * @param lastMsgId 最新回复消息id
   */
  void setLastMsgId(String owner, String to, String lastMsgId);

  /**
   * 获取单聊消息最新回复消息id
   *
   * @param owner 消息所属人
   * @param to 收件人
   * @return 最新消息id
   */
  String getLastMsgId(String owner, String to);

  /**
   * 删除单聊消息最新回复消息id
   *
   * @param owner 消息所属人
   * @param to 收件人
   */
  void deleteLastMsgId(String owner, String to);
}
