package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.common.ResultCodeEnum;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
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

  /**
   * @param from 发件人
   * @param blacker 收件人
   * @description 检查发件人是否在收件人黑名单中
   */
  @Transactional
  public void checkInBlacklist(String from, String blacker) {
    int count = usermailBlacklistService.isInBlacklist(from, blacker);
    if (count != 0) {
      throw new IllegalGmArgsException(ResultCodeEnum.ERROR_IN_BLACKLIST);
    }
  }
}
