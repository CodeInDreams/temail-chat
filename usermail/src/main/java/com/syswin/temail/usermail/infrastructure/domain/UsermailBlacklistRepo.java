package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.UsermailBlacklist;
import java.util.List;

public interface UsermailBlacklistRepo {

  /**
   * 新增黑名单
   */
  int insert(UsermailBlacklist usermailBlacklist);

  /**
   * 根据发起者和被拉黑者删除黑名单信息
   */
  int deleteByAddresses(UsermailBlacklist usermailBlacklist);

  /**
   * 根据发起者和被拉黑者查找黑名单信息
   */
  UsermailBlacklist selectByAddresses(String temailAddress, String blackedAddress);

  /**
   * 查找当前地址的黑名单列表
   */
  List<UsermailBlacklist> selectByTemailAddress(String temailAddress);

  /**
   * 判断收件人是否在发件人黑名单(0:不在;1:在)
   */
  int countByAddresses(String temailAddress, String blackedAddress);
}
