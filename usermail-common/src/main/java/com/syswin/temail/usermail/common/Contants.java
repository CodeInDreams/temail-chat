package com.syswin.temail.usermail.common;

public class Contants {

  public enum RESULT_CODE {
    SUCCESS(200, "success"),
    ERROR_REQUEST_PARAM(400, "请求参数错误"),
    ERROR_ILLEGAL_STORE_TYPE(459, "storeType 不正确！"),
    ERROR_IN_BLACKLIST(461, "发送方处于接收方的黑名单中"),
    //msgId重复，前端有可能收不到回执。
    ERROR_DATABASE_KEY_ID(463, "重复DATABASE_KEY_ID,"),
    // 此code APP 端做业务逻辑判断，不能修改code值
    ERROR_ILLEGAL_PARENT_MSG_ID(464, "源消息不存在"),
    ERROR_MSG_DECODE(467, "消息decode错误，请检查消息格式"),
    ERROR_MSG_ENCODE(468, "消息encode错误，请检查消息格式"),
    ERROR_MSG_ZIP(469, "消息ZIP错误，请检查消息格式"),
    ERROR_SERVER(500, "出现异常"),
    ERROR_FILTER_SEQIDS(567, "seqID过滤参数格式错误，无法正确解析 ");
    private int code;
    private String message;

    RESULT_CODE(int code, String message) {
      this.code = code;
      this.message = message;
    }

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    @Override
    public String toString() {
      return "RESULT_CODE{" +
          "code=" + code +
          ", message='" + message + '\'' +
          '}';
    }
  }

  public enum ReplyCountStatus {
    INCR(1), DECR(-1);
    private int code;

    ReplyCountStatus(int code) {
      this.code = code;
    }

    public static boolean isINCR(int code) {
      return INCR.value() == code;
    }

    public static boolean isDECR(int code) {
      return DECR.value() == code;
    }

    public int value() {
      return this.code;
    }
  }

  /**
   * 事件列表
   */
  public interface SessionEventType {

    // 会话消息已接收 (未读+1)
    int EVENT_TYPE_0 = 0;
    // 消息撤回
    int EVENT_TYPE_2 = 2;
    // 阅后即焚
    int EVENT_TYPE_3 = 3;
    // 会话消息已删除
    int EVENT_TYPE_4 = 4;
    // 会话消息已接收 (未读+1)(阅后即焚消息)
    int EVENT_TYPE_17 = 17;
    //发送回复消息
    int EVENT_TYPE_18 = 18;
    //撤回回复消息
    int EVENT_TYPE_19 = 19;
    //删除回复消息
    int EVENT_TYPE_20 = 20;
    //阅后即焚回复消息
    int EVENT_TYPE_26 = 26;
    //单聊归档
    int EVENT_TYPE_33 = 33;
    // 单聊取消归档
    int EVENT_TYPE_34 = 34;
    // 单聊消息移送废纸篓
    int EVENT_TYPE_35 = 35;
    // 废纸篓消息还原
    int EVENT_TYPE_36 = 36;
    // 废纸篓消息删除
    int EVENT_TYPE_37 = 37;
    // 跨域消息类型
    int EVENT_TYPE_51 = 51;
  }

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
    String PARENT_MSGID = "parentMsgId";//回复父消息id
    String ATTACHMENT_SIZE = "attachmentSize";
    String TRASH_MSG_INFO = "trashMsgInfo";
    String REPLY_MSG_PARENT_ID = "replyMsgParentId";
    String AUTHOR = "author";
    String FILTER = "filter";
  }

  public interface UsermailAgentEventType {

    //删除废纸篓消息
    int TRASH_REMOVE_0 = 0;
    //清空废纸篓
    int TRASH_CLEAR_1 = 1;
    // 阅后即焚已焚
    int DESTROY_AFTER_READ_2 = 2;
    // 消息撤回
    int REVERT_MSG_3 = 3;
    // 回复消息撤回
    int REVERT_REPLY_MSG_4 = 4;
    // 回复消息阅后即焚
    int DESTROY_AFTER_READ_REPLY_MSG_5 = 5;
    //删除群成员相关信息
    int REMOVE_GROUP_CHAT_MEMBERS_6 = 6;
  }


  public interface HttpHeaderKey {

    String CDTP_HEADER = "CDTP-header";
    String X_PACKET_ID = "X-PACKET-ID";
  }

  public interface TemailStatus {

    // 正常状态(占位)
    int STATUS_NORMAL_0 = 0;
    // 撤回数据
    int STATUS_REVERT_1 = 1;
    // 阅后即焚
    int STATUS_DESTORY_AFTER_READ_2 = 2;
    // 删除状态
    int STATUS_DELETE_3 = 3;
    // 废纸篓
    int STATUS_TRASH_4 = 4;
  }

  public interface TemailArchiveStatus {

    // 正常状态
    int STATUS_NORMAL_0 = 0;
    // 归档状态
    int STATUS_ARCHIVE_1 = 1;
  }

  public interface TemailStoreType {

    //(int) 1 存收件人收件箱 2 存发件人收件箱
    int STORE_TYPE_TO_1 = 1;
    int STORE_TYPE_FROM_2 = 2;
  }

  public interface TemailType {

    // 普通消息(占位)
    int TYPE_NORMAL_0 = 0;
    // 阅后即焚
    int TYPE_DESTORY_AFTER_READ_1 = 1;
    // 跨域消息
    int TYPE_CROSS_DOMAIN_GROUP_EVENT_2 = 2;
  }


  public interface CassandraConstant {

    // keyspace
    String KEYSPACE_USERMAILAGENT = "usermailagent";
    // table: usermail
    String TABLE_USERMAIL = "usermail";
    // table: usermail_msg_reply
    String TABLE_USERMAIL_MSG_REPLY = "usermail_msg_reply";
    // 主键id
    String ID = "id";
    // usermail、usermial_msg_reply表 message字段
    String MESSAGE = "message";
  }

}
