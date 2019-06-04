package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.core.IUsermailAdapter;
import com.syswin.temail.usermail.domains.UsermailBlacklist;
import com.syswin.temail.usermail.domains.UsermailBlacklistRepo;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsermailBlacklistService {

  private final UsermailBlacklistRepo usermailBlacklistRepo;

  private final IUsermailAdapter iUsermailAdapter;

  @Autowired
  public UsermailBlacklistService(UsermailBlacklistRepo usermailBlacklistRepo, IUsermailAdapter iUsermailAdapter) {
    this.usermailBlacklistRepo = usermailBlacklistRepo;
    this.iUsermailAdapter = iUsermailAdapter;
  }

  @Transactional
  public int save(UsermailBlacklist usermailBlacklist) {
    usermailBlacklist.setId(iUsermailAdapter.getUsermailBlacklistPkID());
    return usermailBlacklistRepo.insert(usermailBlacklist);
  }

  @Transactional
  public int remove(UsermailBlacklist usermailBlacklist) {
    return usermailBlacklistRepo.deleteByAddresses(usermailBlacklist);
  }

  public UsermailBlacklist findByAddresses(String temailAddress, String blackedAddress) {
    return usermailBlacklistRepo.selectByAddresses(temailAddress, blackedAddress);
  }

  @Transactional
  public List<UsermailBlacklist> findByTemailAddress(String temailAddress) {
    return usermailBlacklistRepo.selectByTemailAddress(temailAddress);
  }

  @Transactional
  public int isInBlacklist(String from, String to) {
    // from to 对应黑名单要反过来查询，确认自己是否被[to]加入黑名单
    return usermailBlacklistRepo.countByAddresses(to, from);
  }
}
