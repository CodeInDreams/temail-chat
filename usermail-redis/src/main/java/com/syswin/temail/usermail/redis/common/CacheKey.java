package com.syswin.temail.usermail.redis.common;

public class CacheKey {

  public interface Usermail {

    // 消息主键
    String KEY_USERMAIL_PKID = "usermail_pkid";
    // 单聊黑名单主键
    String KEY_USERMAIL_BLACKLIST_PKID = "usermail_blacklist_pkid";
    // 单聊最新消息id
    String KEY_USERMAIL_LAST_MSG_ID = "usermail_last_msgid_%s_%s";
    // 会话sequenceno
    String KEY_USERMAIL_SEQNO = "usermail_seqno_";
    // 单聊回复消息主键
    String KEY_USERMAIL_MSG_REPLY_PKID = "usermail_msg_reply_pkid ";
    // 单聊回复消息sequenceno
    String KEY_USERMAIL_REPLY_SEQNO = "usermail_reply_seqno_%s_%s";
  }

  public interface Groupmail {

    // 群聊主键
    String KEY_GROUPMAIL_PKID = "groupmail_pkid";
    // 群聊成员主键
    String KEY_GROUPMAIL_MEMBER_PKID = "groupmail_member_pkid";
    // 会话sequenceno
    // 历史遗留: 这个key value 是没对应上的
    String KEY_GROUPMAIL_SEQNO = "usermail_seqno_";
    // 群消息主键
    String KEY_GROUPMAIL_MSG_PKID = "groupmail_msg_pkid";
    // 群回复消息主键
    String KEY_GROUPMAIL_MSG_REPLY_PKID = "groupmail_msg_reply_pkid";
    // 群黑名单主键
    String KEY_GROUPMAIL_BLACKLIST_PKID = "groupmail_blacklist_pkid";
    // 群回复消息sequenceno
    String KEY_GROUPMAIL_REPLY_SEQNO = "groupmail_reply_seqno_%s";
    // 群at消息主键
    String KEY_GROUPMAIL_AT_MSG_PKID = "groupmail_at_msg_pkid";
    // 群at消息序号
    String KEY_GROUPMAIL_AT_MSG_SEQNO = "groupmail_at_msg_seqno_%s";
    // 群聊at成员主键
    String KEY_GROUPMAIL_AT_MEMBER_PKID = "groupmail_at_member_pkid";
    // 群at成员序号
    String KEY_GROUPMAIL_AT_MEMBER_SEQNO = "groupmail_at_member_seqno_%s_%s";
    String KEY_GROUPMAIL_LAST_MSGID = "groupmail_last_msgid_%s";
    // 群使用序号key
    String KEY_GROUPMAIL_KEYS = "groupmail_keys_%s";


  }

  public interface Topic {

    //话题信息主键
    String KEY_TOPIC_INFO_PKID = "topic_info_pkid";
    //话题成员主键
    String KEY_TOPIC_MEMBER_PKID = "topic_member_pkid";
    //话题消息主键
    String KEY_TOPIC_MSG_PKID = "topic_msg_pkid";
    // 话题消息消息sequenceno
    String KEY_TOPIC_MSG_SEQNO = "topic_msg_seqno_%s";
    //话题序列号
    String TOPIC_INFO_SEQNO = "topic_info_seqno";
    // 话题回复最新消息msgid
    String KEY_TOPIC_LAST_MSGID = "topic_last_msgid_%s_%s";
  }

  public interface Dispatcher {

    // xpacket_eventType 唯一键
    String KEY_EVENTTYPE_PACKETID_UNIQUE = "application_%s_%s";
  }

  public interface App {

    // 协同应用配置类主键ID
    String KEY_APP_CONFIG_PK = "app_cfg_pk";
    // 协同应用主键
    String KEY_APP_PK = "app_pk";
    // 会话sequenceno
    String KEY_APP_SEQ = "app_msg_seq_";
    // 协同应用成员主键
    String KEY_APP_MEMBER_PK = "app_mem_pk";
    // 协同应用消息主键
    String KEY_APP_MSG_PK = "app_msg_pk";
    // 协同应用回复消息主键
    String KEY_APP_MSG_REPLY_PK = "app_rep_pk";
    // 协同应用黑名单主键
    String KEY_APP_BLACKLIST_PK = "app_blacklist_pk";
    // 协同应用回复消息sequenceno
    String KEY_APP_REPLY_SEQ = "app_rep_seq_%s";
    // 协同应用at消息主键
    String KEY_APP_AT_MSG_PK = "app_at_msg_pk";
    // 协同应用at消息序号
    String KEY_APP_AT_MSG_SEQ = "app_at_msg_seq_%s";
    // 协同应用at成员主键
    String KEY_APP_AT_MEMBER_PK = "app_at_mem_pk";
    // 协同应用at成员序号
    String KEY_APP_AT_MEMBER_SEQ = "app_at_mem_seq_%s_%s";
    String KEY_APP_LAST_MSGID = "app_last_msgid_%s";
    // 协同应用使用序号key
    String KEY_APP_KEYS = "app_keys_%s";
    // temail gatewayUrl and publicKey infos
    String KEY_AUTH_GATEWAY_INFO = "auth_gateway_info_%s";
  }

}
