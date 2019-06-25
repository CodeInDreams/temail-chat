package com.syswin.temail.usermail.infrastructure.domain.impl;

import com.syswin.temail.usermail.domains.UsermailBlacklistDO;
import com.syswin.temail.usermail.infrastructure.domain.UsermailBlacklistRepo;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailBlacklistMapper;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UsermailBlacklistRepoImpl implements UsermailBlacklistRepo {

  private final UsermailBlacklistMapper usermailBlacklistMapper;

  @Autowired
  public UsermailBlacklistRepoImpl(UsermailBlacklistMapper usermailBlacklistMapper) {
    this.usermailBlacklistMapper = usermailBlacklistMapper;
  }

  /**
   * 新增黑名单
   *
   * @param usermailBlacklist 黑名单信息
   * @return 插入的数量
   */
  @Override
  public int insertUsermailBlacklist(UsermailBlacklistDO usermailBlacklist) {
    return usermailBlacklistMapper.insertUsermailBlacklist(usermailBlacklist);
  }

  /**
   * 根据发起者和被拉黑者删除黑名单信息
   *
   * @param usermailBlacklist 黑名单信息
   * @return 删除的数量
   */
  @Override
  public int deleteUsermailBlacklist(UsermailBlacklistDO usermailBlacklist) {
    return usermailBlacklistMapper.deleteUsermailBlacklist(usermailBlacklist);
  }

  /**
   * 根据发起者和被拉黑者查找黑名单信息
   *
   * @param temailAddress 发起者地址
   * @param blackedAddress 被拉黑地址
   * @return 黑名单信息
   */
  @Override
  public UsermailBlacklistDO getUsermailBlacklist(String temailAddress, String blackedAddress) {
    return usermailBlacklistMapper.getUsermailBlacklist(temailAddress, blackedAddress);
  }

  /**
   * 查找当前地址的黑名单列表
   *
   * @param temailAddress 当前地址
   * @return 黑名单列表
   */
  @Override
  public List<UsermailBlacklistDO> listUsermailBlacklists(String temailAddress) {
    return usermailBlacklistMapper.listUsermailBlacklists(temailAddress);
  }

  /**
   * 判断收件人是否在发件人黑名单(0 : 不在 ; 1 : 在)
   *
   * @param temailAddress 当前地址
   * @param blackedAddress 被拉黑地址
   * @return 黑名单数量
   */
  @Override
  public int countByAddresses(String temailAddress, String blackedAddress) {
    return usermailBlacklistMapper.countByAddresses(temailAddress, blackedAddress);
  }

}