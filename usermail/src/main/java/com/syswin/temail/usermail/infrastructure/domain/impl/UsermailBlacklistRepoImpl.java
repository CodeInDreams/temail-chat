package com.syswin.temail.usermail.infrastructure.domain.impl;

import com.syswin.temail.usermail.domains.UsermailBlacklist;
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
   * @param usermailBlacklist 黑名单信息
   * @description 新增黑名单
   */
  @Override
  public int insert(UsermailBlacklist usermailBlacklist) {
    return usermailBlacklistMapper.insert(usermailBlacklist);
  }

  /**
   * @param usermailBlacklist 黑名单信息
   * @description 根据发起者和被拉黑者删除黑名单信息
   */
  @Override
  public int deleteByAddresses(UsermailBlacklist usermailBlacklist) {
    return usermailBlacklistMapper.deleteByAddresses(usermailBlacklist);
  }

  /**
   * @param temailAddress 发起者地址
   * @param blackedAddress 被拉黑地址
   * @description 根据发起者和被拉黑者查找黑名单信息
   */
  @Override
  public UsermailBlacklist selectByAddresses(String temailAddress, String blackedAddress) {
    return usermailBlacklistMapper.selectByAddresses(temailAddress, blackedAddress);
  }

  /**
   * @param temailAddress 当前地址
   * @description 查找当前地址的黑名单列表
   */
  @Override
  public List<UsermailBlacklist> selectByTemailAddress(String temailAddress) {
    return usermailBlacklistMapper.selectByTemailAddress(temailAddress);
  }

  /**
   * @param temailAddress 当前地址
   * @param blackedAddress 被拉黑地址
   * @description 判断收件人是否在发件人黑名单(0 : 不在 ; 1 : 在)
   */
  @Override
  public int countByAddresses(String temailAddress, String blackedAddress) {
    return usermailBlacklistMapper.countByAddresses(temailAddress, blackedAddress);
  }

}