package com.syswin.temail.usermail.common;

public class Constants {

  public interface UsermailAgentEventType {

    /* 删除废纸篓消息 */
    int TRASH_REMOVE_0 = 0;
    /* 清空废纸篓 */
    int TRASH_CLEAR_1 = 1;
    /* 阅后即焚已焚 */
    int DESTROY_AFTER_READ_2 = 2;
    /* 消息撤回 */
    int REVERT_MSG_3 = 3;
    /* 回复消息撤回 */
    int REVERT_REPLY_MSG_4 = 4;
    /* 回复消息阅后即焚 */
    int DESTROY_AFTER_READ_REPLY_MSG_5 = 5;
    /* 删除群成员相关信息 */
    int REMOVE_GROUP_CHAT_MEMBERS_6 = 6;
  }


  public interface TemailStatus {

    /* 正常状态(占位) */
    int STATUS_NORMAL_0 = 0;
    /* 撤回数据 */
    int STATUS_REVERT_1 = 1;
    /* 阅后即焚 */
    int STATUS_DESTORY_AFTER_READ_2 = 2;
    /* 删除状态 */
    int STATUS_DELETE_3 = 3;
    /* 废纸篓 */
    int STATUS_TRASH_4 = 4;
  }

  public interface TemailArchiveStatus {

    /* 正常状态 */
    int STATUS_NORMAL_0 = 0;
    /* 归档状态 */
    int STATUS_ARCHIVE_1 = 1;
  }

  public interface TemailStoreType {

    /* 存收件人收件箱 */
    int STORE_TYPE_TO_1 = 1;
    /* 存发件人收件箱 */
    int STORE_TYPE_FROM_2 = 2;
  }

  public interface TemailType {

    /* 普通消息(占位) */
    int TYPE_NORMAL_0 = 0;
    /* 阅后即焚 */
    int TYPE_DESTORY_AFTER_READ_1 = 1;
    /* 跨域消息 */
    int TYPE_CROSS_DOMAIN_GROUP_EVENT_2 = 2;
  }


}
