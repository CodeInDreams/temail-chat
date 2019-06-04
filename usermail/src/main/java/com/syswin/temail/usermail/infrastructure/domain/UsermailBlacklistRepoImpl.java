package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.UsermailBlacklist;
import com.syswin.temail.usermail.domains.UsermailBlacklistRepo;
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

  @Override
  public int insert(UsermailBlacklist usermailBlacklist) {
    return usermailBlacklistMapper.insert(usermailBlacklist);
  }

  @Override
  public int deleteByAddresses(UsermailBlacklist usermailBlacklist) {
    return usermailBlacklistMapper.deleteByAddresses(usermailBlacklist);
  }

  @Override
  public UsermailBlacklist selectByAddresses(String temailAddress, String blackedAddress) {
    return usermailBlacklistMapper.selectByAddresses(temailAddress, blackedAddress);
  }

  @Override
  public List<UsermailBlacklist> selectByTemailAddress(String temailAddress) {
    return usermailBlacklistMapper.selectByTemailAddress(temailAddress);
  }

  @Override
  public int countByAddresses(String temailAddress, String blackedAddress) {
    return usermailBlacklistMapper.countByAddresses(temailAddress, blackedAddress);
  }

}