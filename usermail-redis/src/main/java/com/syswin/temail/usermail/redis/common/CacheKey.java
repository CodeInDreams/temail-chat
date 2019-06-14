package com.syswin.temail.usermail.redis.common;

public class CacheKey {

  public interface Usermail {
    /** 消息主键 */
    String KEY_USERMAIL_PKID = "usermail_pkid";
    /** 单聊黑名单主键 */
    String KEY_USERMAIL_BLACKLIST_PKID = "usermail_blacklist_pkid";
    /** 单聊最新消息id */
    String KEY_USERMAIL_LAST_MSG_ID = "usermail_last_msgid_%s_%s";
    /** 会话sequenceno */
    String KEY_USERMAIL_SEQNO = "usermail_seqno_";
    /** 单聊回复消息主键 */
    String KEY_USERMAIL_MSG_REPLY_PKID = "usermail_msg_reply_pkid ";
    /** 单聊回复消息sequenceno */
    String KEY_USERMAIL_REPLY_SEQNO = "usermail_reply_seqno_%s_%s";
  }

}
