package com.syswin.temail.usermail.domains;

import java.util.List;

public interface UsermailBlacklistRepo {

  int insert(UsermailBlacklist usermailBlacklist);

  int deleteByAddresses(UsermailBlacklist usermailBlacklist);

  UsermailBlacklist selectByAddresses(String temailAddress, String blackedAddress);

  List<UsermailBlacklist> selectByTemailAddress(String temailAddress);

  int countByAddresses(String temailAddress, String blackedAddress);
}
