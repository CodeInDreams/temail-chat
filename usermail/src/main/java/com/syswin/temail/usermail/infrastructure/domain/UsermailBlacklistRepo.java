package com.syswin.temail.usermail.infrastructure.domain;

import com.syswin.temail.usermail.domains.UsermailBlacklist;
import java.util.List;

public interface UsermailBlacklistRepo {

  int insert(UsermailBlacklist usermailBlacklist);

  int deleteByAddresses(UsermailBlacklist usermailBlacklist);

  UsermailBlacklist selectByAddresses(String temailAddress, String blackedAddress);

  List<UsermailBlacklist> selectByTemailAddress(String temailAddress);

  int countByAddresses(String temailAddress, String blackedAddress);
}
