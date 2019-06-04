package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.common.Contants.RESULT_CODE;
import com.syswin.temail.usermail.core.exception.IllegalGMArgsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UmBlacklistProxy {

  private final UsermailBlacklistService usermailBlacklistService;

  @Autowired
  public UmBlacklistProxy(UsermailBlacklistService usermailBlacklistService) {
    this.usermailBlacklistService = usermailBlacklistService;
  }

  @Transactional
  public void checkInBlacklist(String from, String blacker) {
    int count = usermailBlacklistService.isInBlacklist(from, blacker);
    if (count != 0) {
      throw new IllegalGMArgsException(RESULT_CODE.ERROR_IN_BLACKLIST);
    }
  }
}
