package com.syswin.temail.usermail.common;

public class ParamsKey {

  public interface SessionEventKey {

    String FROM = "from";
    String CDTP_HEADER = "header";
    String PACKET_ID_SUFFIX = "-SCD";
    String X_PACKET_ID = "xPacketId";
    String TO = "to";
    String OWNER = "owner";
    String SESSION_MESSAGE_TYPE = "sessionMessageType";
    String MSGID = "msgid";
    String TO_MSG = "toMsg";
    String SEQ_NO = "seqNo";
    String TIMESTAMP = "timestamp";
    String GROUP_TEMAIL = "groupTemail";
    String TEMAIL = "temail";
    String MESSAGE = "message";
    String DELETE_ALL_MSG = "deleteAllMsg";
    /** 回复父消息id */
    String PARENT_MSGID = "parentMsgId";
    String ATTACHMENT_SIZE = "attachmentSize";
    String TRASH_MSG_INFO = "trashMsgInfo";
    String REPLY_MSG_PARENT_ID = "replyMsgParentId";
    String AUTHOR = "author";
    String FILTER = "filter";
  }

  public interface HttpHeaderKey {

    String CDTP_HEADER = "CDTP-header";
    String X_PACKET_ID = "X-PACKET-ID";
  }

  public interface CassandraConstant {
    /** keyspace */
    String KEYSPACE_USERMAILAGENT = "usermailagent";
    /** table: usermail */
    String TABLE_USERMAIL = "usermail";
    /** table: usermail_msg_reply */
    String TABLE_USERMAIL_MSG_REPLY = "usermail_msg_reply";
    /** 主键id */
    String ID = "id";
    /** usermail、usermial_msg_reply表 message字段 */
    String MESSAGE = "message";
  }
}
