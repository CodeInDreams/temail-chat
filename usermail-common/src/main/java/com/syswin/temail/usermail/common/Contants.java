package com.syswin.temail.usermail.common;

public class Contants {

  public enum RESULT_CODE {
    SUCCESS(200, "success"),
    ERROR_REQUEST_PARAM(400, "请求参数错误"),
    ERROR_PARAM_SIGNATURE(401, "接口参数验签失败"),
    ERROR_PUBLIC_KEY_IS_EMPTY(402, "用户公钥为空"),
    ERROR_AUTH_TEMAIL_REGISTER(409, "TeMail address is already taken"),
    ERROR_SEQNO_IS_TOO_SMALL(450, "sequenceNo 太小"),
    ERROR_FROM(451, "消息发送者不正确,未填写或者太长"),
    ERROR_TO(452, "消息接收者不正确,未填写或者太长"),
    ERROR_N0_EXIST_MEMBER(453, "用户信息不存在或不正确！"),
    ERROR_N0_ADMIN_MEMBER(454, "用户没有管理员权限！"),
    ERROR_N0_LEGAL_MEMBER(455, "用户非正常状态！"),
    ERROR_N0_LEGAL_MSGID(456, "消息ID不正确或不属于该群！"),
    ERROR_ILLEGAL_CREATE_MSG(457, "未找到前置消息信息，请确认前置消息信息是否发送正确！"),
    ERROR_ILLEGAL_MSG_ID(458, "重复MSG_ID！"),
    ERROR_ILLEGAL_STORE_TYPE(459, "storeType 不正确！"),
    ERROR_CREATE_BLACKLIST_PARAM(460, "temailAddress或blackedAddress为空"),
    ERROR_IN_BLACKLIST(461, "发送方处于接收方的黑名单中"),
    ERROR_NOT_IN_THIS_GROUP(462, "该用户不在本群"),
    //msgId重复，前端有可能收不到回执。
    ERROR_DATABASE_KEY_ID(463, "重复DATABASE_KEY_ID,"),
    // 此code APP 端做业务逻辑判断，不能修改code值
    ERROR_ILLEGAL_PARENT_MSG_ID(464, "源消息不存在"),
    ERROR_REPEAT_REQUEST(465, "重复请求"),
    //单聊根据msgId查询不到消息
    ERROR_MSGID_NOT_EXISTS(466, "查询不到消息，请检查msgId和消息所有者"),
    ERROR_MSG_DECODE(467, "消息decode错误，请检查消息格式"),
    ERROR_MSG_ENCODE(468, "消息encode错误，请检查消息格式"),
    ERROR_MSG_ZIP(469, "消息ZIP错误，请检查消息格式"),
    ERROR_IN_GROUP_BLACKLIST_NOT_JOIN(471, "用户在本群黑名单中，无法加入本群"),
    ERROR_IN_GROUP_BLACKLIST_REMOVE(472, "用户在本群黑名单中，已被移除"),
    ERROR_ADD_BLACKLIST_NOT_OWN(473, "加入黑名单错误，不能将自己加入黑名单"),


    ERROR_SERVER(500, "出现异常"),
    ERROR_GROUPMAIL_EXIST(551, "申请的群邮件已经存在！"),
    ERROR_GROUPMAIL_NO_EXIST(552, "申请的群邮件不存在或已解散！"),
    ERROR_GROUPMAIL_REGISTER(553, "群注册，AUTH认证失败！"),
    ERROR_GROUPMAIL_MEMBER_REGISTER(554, "群注册，群成员AUTH认证失败！"),
    ERROR_AUTH(555, "AUTH认证失败,"),
    ERROR_CIPHER(556, "加密失败或群信息已注册,"),
    ERROR_AUTH_GROUPMAIL_EXIST(557, "AUTH认证失败,409 群邮件已经存在！"),
    ERROR_AUTH_GROUPMAIL_ILLAGE_ARGS(558, "AUTH认证失败,400 参数不合法！"),
    ERROR_AUTH_GROUPMAIL_CAN_NOT_USE(559, "AUTH 416 temail地址不允许注册！"),
    ERROR_GROUPMAIL_NO_MASTER(560, "群中没有群主信息！"),
    ERROR_GROUPMAIL_AT_MEMBER_NOT_EXSIT(561, "该用户不在本会话组"),
    ERROR_GROUPMAIL_AT_MEMBER_NOT_CREATOR(562, "该用户不是指定消息创建者"),
    ERROR_GROUPMAIL_AT_MSG_NOT_SENDER(563, "无权限操作该msgId"),
    ERROR_GROUPMAIL_BLACKLIST_NOT_AUTH(564, "普通管理员无权限对其他管理员进行黑名单移入移除操作"),
    ERROR_GROUPMAIL_BLACKLIST_NOT_JOIN(565, "预加入黑名单用户状态错误，不可加入黑名单！"),
    ERROR_GROUPMAIL_LAST_ADMIN(566, "群管理员数量不能少于1！"),
    ERROR_FILTER_SEQIDS(567, "seqID过滤参数格式错误，无法正确解析 "),
    ERROR_GROUPMAIL_MEMBER_IS_ADMIN(568, "该群成员已是群主！"),
    ERROR_GROUPMAIL_MEMBER_IS_NOT_ADMIN(569, "该群成员不是群主！"),
    ERROR_KMS_DELETE_PUBKEY(570, "解散群，kms公钥删除失败"),
    ERROR_AUTH_DELETE_GROUPMAIL(571, "解散群，auth相关信息删除失败"),

    ERROR_TOPIC_NOT_EXXIST(601, "话题不存在或者话题已删除"),
    ERROR_TOPIC_MEMEBER_NOT_EXXIST(602, "用户不属于该话题"),
    ERROR_TOPIC_MESSAGE_NOT_EXXIST(603, "话题信息不存在或已经删除"),
    ERROR_TOPIC_OPERATE_DATABASE(604, "数据库操作失败"),
    ERROR_TOPIC_NOT_OWNER(605, "无权限操作该话题");
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
    // 会话消息已拉取 (未读-1)
    int EVENT_TYPE_1 = 1;
    // 消息撤回
    int EVENT_TYPE_2 = 2;
    // 阅后即焚
    int EVENT_TYPE_3 = 3;
    // 会话消息已删除
    int EVENT_TYPE_4 = 4;
    // 入群申请已收到 (未读+1)
    int EVENT_TYPE_5 = 5;
    // 入群申请已通过
    int EVENT_TYPE_6 = 6;
    // 入群申请已拒绝
    int EVENT_TYPE_7 = 7;
    // 入群邀请已收到 (未读+1)
    int EVENT_TYPE_8 = 8;
    // 入群邀请已拒绝
    int EVENT_TYPE_9 = 9;
    // 新成员已入群
    int EVENT_TYPE_10 = 10;
    // 群成员已移除(被踢)
    int EVENT_TYPE_11 = 11;
    // 群已解散
    int EVENT_TYPE_12 = 12;
    // 新建群
    int EVENT_TYPE_13 = 13;
    // 入群邀请已通过
    int EVENT_TYPE_14 = 14;
    // 主动退出群
    int EVENT_TYPE_15 = 15;
    // 群名片更新
    int EVENT_TYPE_16 = 16;
    // 会话消息已接收 (未读+1)(阅后即焚消息)
    int EVENT_TYPE_17 = 17;
    //发送回复消息
    int EVENT_TYPE_18 = 18;
    //撤回回复消息
    int EVENT_TYPE_19 = 19;
    //删除回复消息
    int EVENT_TYPE_20 = 20;
    //创建新话题消息
    int EVENT_TYPE_21 = 21;
    //发送话题回复消息
    int EVENT_TYPE_22 = 22;
    //撤回话题回复消息
    int EVENT_TYPE_23 = 23;
    //删除话题回复消息
    int EVENT_TYPE_24 = 24;
    //删除话题
    int EVENT_TYPE_25 = 25;
    //阅后即焚回复消息
    int EVENT_TYPE_26 = 26;
    //群归档
    int EVENT_TYPE_27 = 27;
    //群取消归档
    int EVENT_TYPE_28 = 28;
    //话题归档
    int EVENT_TYPE_29 = 29;
    //话题取消归档
    int EVENT_TYPE_30 = 30;
    // 群消息置顶
    int EVENT_TYPE_31 = 31;
    // 群消息取消置顶
    int EVENT_TYPE_32 = 32;
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
    // 群会话删除
    int EVENT_TYPE_38 = 38;
    // 话题会话删除
    int EVENT_TYPE_39 = 39;
    // 加入黑名单
    int EVENT_TYPE_40 = 40;
    // 移除黑名单
    int EVENT_TYPE_41 = 41;
    // 开启免打扰，通知服务已占用，标记占位避免冲突
    int EVENT_TYPE_42 = 42;
    // 关闭免打扰，通知服务已占用，标记占位避免冲突
    int EVENT_TYPE_43 = 43;
    // 发送at消息
    int EVENT_TYPE_44 = 44;
    // 删除at消息（指定人消息）
    int EVENT_TYPE_45 = 45;
    // 添加群管理员
    int EVENT_TYPE_46 = 46;
    // 移除群管理员
    int EVENT_TYPE_47 = 47;
    // 设置单聊免打扰，通知端已使用，占位
    int EVENT_TYPE_48 = 48;
    // 取消单聊免打扰，通知端已使用，占位
    int EVENT_TYPE_49 = 49;
    // 放弃群主身份群管理员
    int EVENT_TYPE_50 = 50;
    // 跨域消息类型
    int EVENT_TYPE_51 = 51;
  }

  public interface SessionEventKey {

    String FROM = "from";
    String CDTP_HEADER = "header";
    String PACKET_ID_SUFFIX = "-SCD";
    String X_PACKET_ID = "xPacketId";
    String TO = "to";
    String BODY = "body";
    String OWNER = "owner";
    String SESSION_MESSAGE_TYPE = "sessionMessageType";
    String EVENT_TYPE = "eventType";
    String MSGID = "msgid";//原通知、计费服务的msgId,兼容老版本
    String MSG_ID = "msgId";//DM事件服务直接指给前端使用的msgId
    String MSGIDS = "msgIds";
    String TO_MSG = "toMsg";
    String SEQ_NO = "seqNo";
    String SEQ_ID = "seqId";
    String TIMESTAMP = "timestamp";
    String GROUP_TEMAIL = "groupTemail";
    String APP_TEMAIL = "appTemail";
    String INVITEES = "invitees";
    String TOPIC = "topic";
    String GROUP_MEMBER = "GROUP_MEMBER";
    String GROUP_NAME = "groupName";
    String APP_NAME = "appName";
    String APP_REMARK = "appRemark";
    String GROUP_MAIL_INFO = "GROUP_MAIL_INFO";
    String TEMAIL = "temail";
    String AVATAR_URL = "avatarUrl";
    String PUBKEY = "pubKey";
    String TYPE = "type";
    String OPTYPE = "optype";
    String MESSAGE = "message";
    String STATUS = "status";
    String DATA = "data";
    String CREATETIME = "createtime";
    String GROUP_MAIL_MSG = "groupmailMsg";
    String V_CARD = "vCard";
    String GROUP_V_CARD = "groupVCard";
    String GROUP_AVATAR_URL = "groupAvatarUrl";
    String NICK_NAME = "nickName";
    String REMARK = "remark";
    String SHARED_KEY = "sharedKey";
    String NAME = "name";
    String AT = "at";
    String COUNT_STATUS = "countStatus";
    String ADMIN_NAME = "adminName";
    String ADMINS = "admins";
    String DELETE_ALL_MSG = "deleteAllMsg";
    String PARENT_MSGID = "parentMsgId";//回复父消息id
    String ATTACHMENT_SIZE = "attachmentSize";
    String TOPIC_ID = "topicId";
    String RECEIVERS = "receivers";
    String CC = "cc";
    String TITLE = "title";
    String TOPIC_SEQ_NO = "topicSeqId";
    String TRASH_MSG_INFO = "trashMsgInfo";
    String REPLY_MSG_PARENT_ID = "replyMsgParentId";
    String FROM_NAME = "fromName";
    String MEMBERS = "members";
    String AUTHOR = "author";
    String FILTER = "filter";
    String APP_CONFIGS = "appConfigs";
    String BLACK_LIST = "blackList";
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

  public interface GroupmailAgentEventType {

    // 解散群-删除群成员
    int DELETE_MEMBERS_EVENT_0 = 0;
    // 解散群-删除群消息
    int DELETE_MSGS_EVENT_1 = 1;
    // 解散群-删除群回复消息
    int DELETE_REPLY_MSGS_EVENT_2 = 2;
    // 解散群-删除群at指定人消息成员
    int DELETE_AT_MEMBERS_EVENT_3 = 3;
    // 解散群-删除群at指定人消息[源消息，回复消息]
    int DELETE_AT_MSGS_EVENT_4 = 4;
    // 解散群-删除群黑名单成员
    int DELETE_BLACKLIST_MEMBERS_EVENT_5 = 5;
    // 解散群-删除群相关的缓存
    int DELETE_GROUP_TEMAIL_CACHE_6 = 6;
    // 解散群-物理删除群信息
    int DELETE_GROUP_TEMAIL_INFO_EVENT_7 = 7;
  }

  public interface HttpHeaderKey {

    String CDTP_HEADER = "CDTP-header";
    String X_PACKET_ID = "X-PACKET-ID";
    String TE_MAIL = "TE-MAIL";
    String PUBLIC_KEY = "PUBLIC-KEY";
    String UNSIGNED_BYTES = "UNSIGNED-BYTES";
    String SIGNATURE = "SIGNATURE";
    String ALGORITHM = "ALGORITHM";
    String IS_AUTH_SIGN = "IS-AUTH-SIGN";
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

  public interface GroupmailType {

    // 好友群(default)
    int TYPE_FRIEND_0 = 0;
    // 开放群
    int TYPE_OPEN_1 = 1;
    // 协作群（管理型群）
    int TYPE_HELP_2 = 2;
    // 企业开放群
    int TYPE_ENT_OPEN_3 = 3;
    //俱乐部
    int TYPE_CLUB_4 = 4;
  }

  // 二级群类型
  public interface GroupmailSecondaryType {

    // 讨论群
    int TYPE_DISCUSS_0 = 0;
    // 新闻群
    int TYPE_NEWS_1 = 1;
    // 文件群
    int TYPE_FILE_2 = 2;
  }

  public interface GroupmailMemberType {

    // 普通用户
    int TYPE_NORMAL_0 = 0;
    // 管理员(群主)
    int TYPE_ADMIN_1 = 1;
  }

  public interface GroupmailAtMsgType {

    // 指定消息
    int AT_MSG_TYPE_0 = 0;
    // 回复指定消息
    int AT_REPLY_TYPE_1 = 1;
  }


  public interface GroupmailAtMemberType {

    // 发起人
    int TYPE_SENDER_0 = 0;
    // 接收人
    int TYPE_RECEIVER_1 = 1;
  }

  public interface GroupmailStoreEventType {

    // 创建群
    int STORE_EVENT_TYPE_0 = 0;
    // 解散群
    int STORE_EVENT_TYPE_1 = 1;
    // 添加群成员
    int STORE_EVENT_TYPE_2 = 2;
    // 删除群成员
    int STORE_EVENT_TYPE_3 = 3;
    // 添加群消息
    int STORE_EVENT_TYPE_4 = 4;
    // 删除或撤回群消息
    int STORE_EVENT_TYPE_5 = 5;
    // 修改群成员信息
    int STORE_EVENT_TYPE_6 = 6;
    // 修改群名片
    int STORE_EVENT_TYPE_7 = 7;
    //回复消息数修改
    int STORE_EVENT_TYPE_8 = 8;
  }

  public interface GroupmailInfoStatus {

    // 正常状态
    int STATUS_NORMAL_0 = 0;
    // 销毁状态
    int STATUS_DESTORY_1 = 1;
  }

  public interface GroupmailMemberStatus {

    // 拉取群信息：不拉取群成员信息
    int STATUS_NOT_RETRIVE = -2;
    // 拉取全部：不区分状态
    int STATUS_ALL = -1;
    // 正常状态
    int STATUS_NORMAL_0 = 0;
    // 移除状态
    int STATUS_DELETE_1 = 1;
    // 邀请中状态
    int STATUS_INVITING_2 = 2;
    // 申请状态
    int STATUS_JOINING_3 = 3;
    // 退出状态
    int STATUS_QUIT_4 = 4;
    // 申请审核未通过状态
    int STATUS_JOIN_AUDIT_FAILED_5 = 5;
    // 拒绝邀请入群状态
    int STATUS_INVITE_REJECT_6 = 6;
  }

  public interface GroupmailAtMsgStatus {

    // 正常状态
    int STATUS_NORMAL_0 = 0;
    // 撤回状态
    int STATUS_REVERT_1 = 1;
  }

  public interface GroupmailManagerStatus {

    //移除群管理员
    int STATUS_MANAGER_REMOVE_0 = 0;
    //添加群管理员
    int STATUS_MANAGER_ADD_1 = 1;
  }

  public interface GroupmailArchiveStatus {

    // 拉取全部：不区分状态
    int STATUS_ALL = -1;
    // 正常状态
    int STATUS_NORMAL_0 = 0;
    // 归档状态
    int STATUS_ARCHIVE_1 = 1;
    // 会话删除状态
    int STATUS_DELETE_2 = 2;
  }

  public interface GroupmailMsgStickStatus {

    // 群消息正常
    int STATUS_STICK_0 = 0;
    // 群消息置顶
    int STATUS_STICK_1 = 1;
  }

  /**
   * 协同应用用户加入方式
   */
  public interface AppJoinType {

    // 自由加入
    int JOIN_TYPE_1 = 1;
    // 群主/成员邀请加入
    int JOIN_TYPE_2 = 2;
    // 群主邀请加入
    int JOIN_TYPE_3 = 3;
    // 身份验证通过后，群主审核加入
    int JOIN_TYPE_4 = 4;
  }

  /**
   * 协同应用配置开关状态
   */
  public interface AppConfigStatus {

    // 打开
    int APP_CONFIG_ALLOW = 1;
    // 关闭
    int APP_CONFIG_CLOSED = 0;
  }

  public interface TopicMsgType {

    //创建话题消息
    int TYPE_NORMAL_0 = 0;

    //回复话题消息
    int TYPE_REPLY_1 = 1;
  }

  public interface TopicMemberType {

    //创建话题创建者
    int TYPE_OWNER_0 = 0;
    //话题接收者
    int TYPE_RECEIVER_1 = 1;
    //话题抄送者
    int TYPE_CC_2 = 2;
  }

  public interface TopicStatus {

    // 正常状态(占位)
    int STATUS_NORMAL_0 = 0;
    // 撤回消息
    int STATUS_REVERT_1 = 1;
    // 删除状态
    int STATUS_DELETE_2 = 2;
  }

  public interface TopicMemberStatus {

    int STATUS_NORMAL_0 = 0;
    //话题成员已退出
    int STATUS_QUIT_1 = 1;
  }

  public interface TopicMsgStatus {

    // 正常状态(占位)
    int STATUS_NORMAL_0 = 0;
    // 撤回消息
    int STATUS_REVERT_1 = 1;
    // 删除状态
    int STATUS_DELETE_2 = 2;
  }

  public interface TopicArchiveStatus {

    // 正常状态
    int STATUS_NORMAL_0 = 0;
    // 归档状态
    int STATUS_ARCHIVE_1 = 1;
    // 删除状态(隐藏)
    int STATUS_DELETE_2 = 2;
  }

  public interface TopicAgentEventType {

    // 删除话题-删除话题成员
    int DELETE_MEMBERS_EVENT_0 = 0;
    // 删除话题-删除消息
    int DELETE_MSGS_EVENT_1 = 1;
    // 删除话题-删除最新消息
    int DELETE_LAST_MSGS_EVENT_2 = 2;
    // 删除话题-删除话题
    int DELETE_TOPIC_EVENT_3 = 3;
  }

  /**
   * 跨域事件列表
   */
  public interface GroupChatCrossEventType {

    //发送单聊消息
    int EVENT_MSG_SEND = 1;

    //撤回单聊消息
    int EVENT_MSG_REVERT = 2;

    //同步单聊入群事件
    int EVENT_IN_GROUP = 3;

    //同步单聊出群事件
    int EVENT_OUT_GROUP = 4;

    //同步到dm服务通知事件
    int EVENT_SYNC_DM_GROUP = 5;
  }

  public interface AppMqEventTypeParams {

    //发送消息类型 K
    String APP_EVENT_TYPE_KEY = "type";
    //创建群聊 K
    String APP_TEMAIL_TYPE_KEY = "groupTemail";
    // 创建群聊 群主temail
    String APP_ADMIN_KEY = "from";
    //创建群 公钥 K
    String APP_PUBKEY_CREATE = "pubKey";
    //创建群聊 V
    int APP_TEMAIL_CREATE_TYPE = 1;
    //解散协同应用
    int APP_TEMAIL_DISBAND_TYPE = 2;
  }

  public interface AppDMType {

    String TYPE_RB001 = "RB001";//邀请入群事件（被邀请人）
    String TYPE_RB002 = "RB002";//申请状态邀请入群事件（被邀请人）
    String TYPE_RB003 = "RB003";//回复同意邀请入群事件（被邀请人）
    String TYPE_RB004 = "RB004";//同步回复同意邀请入群事件（已入群成员）
    String TYPE_RB005 = "RB005";//拒绝邀请入群事件（被邀请人）
    String TYPE_RB006 = "RB006";//同步拒绝邀请入群事件（管理员）
    String TYPE_RB007 = "RB007";//申请入群事件（群管理员）
    String TYPE_RB008 = "RB008";//邀请状态申请入群事件（申请人）
    String TYPE_RB009 = "RB009";//申请加入入群事件（申请人）
    String TYPE_RB00A = "RB00A";//同步申请加入入群事件（已入群成员
    String TYPE_RB00B = "RB00B";//拒绝入群申请事件（申请人）   
    String TYPE_RB00C = "RB00C";//同步拒绝入群申请事件（管理员）
    String TYPE_RB00D = "RB00D";//退群事件（退群人）
    String TYPE_RB00E = "RB00E";//同步退群事件（其他群成员）
    String TYPE_RB00F = "RB00F";//移除群成员事件（被移除人）
    String TYPE_RB010 = "RB010";//同步移除群成员事件（其他群成员）
    String TYPE_RB011 = "RB011";//解散群事件（所有群成员）
    String TYPE_RB012 = "RB012";//修改群成员昵称（所有群成员）
    String TYPE_RB013 = "RB013";//修改群昵称（所有群成员）
    String TYPE_RB014 = "RB014";//会话归档事件（归档人）
    String TYPE_RB015 = "RB015";//取消会话归档事件（归档人）
    String TYPE_RB016 = "RB016";//删除会话事件（归档人）
    String TYPE_RB017 = "RB017";//加入黑名单事件（管理员）
    String TYPE_RB018 = "RB018";//移除黑名单事件（管理员）
    String TYPE_RB019 = "RB019";//消息置顶事件（所有群成员）
    String TYPE_RB01A = "RB01A";//取消消息指定事件（所有群成员）
    String TYPE_RB01B = "RB01B";//添加管理员事件（所有群成员）
    String TYPE_RB01C = "RB01C";//移除管理员事件（所有群成员）
    String TYPE_RB01D = "RB01D";//放弃管理员身份事件（所有群成员）
    String TYPE_RB01E = "RB01E";//发送消息事件（所有群成员）
    String TYPE_RB01F = "RB01F";//撤回消息事件（所有群成员）
    String TYPE_RB020 = "RB020";//删除消息事件（所有群成员）
    String TYPE_RB021 = "RB021";//发送回复消息事件（所有群成员）
    String TYPE_RB022 = "RB022";//撤回回复消息事件（所有群成员）
    String TYPE_RB023 = "RB023";//删除回复消息事件（所有群成员）
    String TYPE_RB024 = "RB024";//发送指定人消息事件（消息指定者）
    String TYPE_RB025 = "RB025";//删除指定人消息事件（消息指定者）
    String TYPE_RB026 = "RB026";//发送指定人消息事件（消息指定者）
    String TYPE_RB027 = "RB027";//撤回回复消息事件（消息指定者）
    String TYPE_RB028 = "RB028";//删除指定人消息事件（消息指定者）
    String TYPE_RB029 = "RB029";//更新协同应用配置信息
    String TYPE_RB02A = "RB02A";//协同应用创建成功事件（运营后台已经占用）
    String TYPE_RB02B = "RB02B";//邀请状态申请入群事件变为申请中状态（申请人）
  }

  /**
   * 指令空间
   */
  public interface CommandSpace {

    short SINGLE_COMMANDSPACE_0001 = 0x1;
  }

  /**
   * 单聊消息指令
   */
  public interface SingleCommand {

    // dm服务通知指令
    short DM_COMMAND_3000 = 0x3000;
  }

  public interface EventFlowType {

    int EVENT_FLOW_DM_TYPE = 52;

  }

  /**
   * 单聊消息指令
   */
  public interface OperateDbResponseType {

    short SUCCESS = 1;
    short FAILURE = 0;
  }

  /**
   * 协同应用参数
   */
  public interface AppParams {

    //二维码过期时间
    Long QRCODE_TIME_DUE = 7 * 86400000L;

    //获取协同应用成员分页大小
    int APP_MEMBER_PAGESIZE = 100;

    //获取协同应用成员第一个页码
    int APP_MEMBER_PAGENUM = 1;
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
