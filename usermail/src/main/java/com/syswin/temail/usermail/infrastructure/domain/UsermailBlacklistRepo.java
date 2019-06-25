package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.UsermailBlacklistDO;
import java.util.List;

public interface UsermailBlacklistRepo {

  /**
   * 新增黑名单
   *
   * @param usermailBlacklist 黑名单信息
   * @return 插入的数量
   */
  int insertUsermailBlacklist(UsermailBlacklistDO usermailBlacklist);

  /**
   * 根据发起者和被拉黑者删除黑名单信息
   *
   * @param usermailBlacklist 黑名单信息
   * @return 删除的数量
   */
  int deleteUsermailBlacklist(UsermailBlacklistDO usermailBlacklist);

  /**
   * 根据发起者和被拉黑者查找黑名单信息
   *
   * @param temailAddress 发起者地址
   * @param blackedAddress 被拉黑地址
   * @return 黑名单信息
   */
  UsermailBlacklistDO getUsermailBlacklist(String temailAddress, String blackedAddress);

  /**
   * 查找当前地址的黑名单列表
   *
   * @param temailAddress 当前地址
   * @return 黑名单列表
   */
  List<UsermailBlacklistDO> listUsermailBlacklists(String temailAddress);

  /**
   * 判断收件人是否在发件人黑名单(0 : 不在 ; 1 : 在)
   *
   * @param temailAddress 当前地址
   * @param blackedAddress 被拉黑地址
   * @return 黑名单数量
   */
  int countByAddresses(String temailAddress, String blackedAddress);
}
