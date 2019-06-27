package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.core.exception.UserMailException;
import com.syswin.temail.usermail.infrastructure.domain.UsermailBlacklistRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailBoxRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailMsgReplyRepo;
import com.syswin.temail.usermail.infrastructure.domain.UsermailRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RemoveDomainService {


  @Value("${app.temail.usermailagent.clear.domain.pageSize}")
  private Integer pageSize;
  private final UsermailRepo usermailRepo;
  private final UsermailMsgReplyRepo usermailMsgReplyRepo;
  private final UsermailBlacklistRepo usermailBlacklistRepo;
  private final UsermailBoxRepo usermailBoxRepo;

  public RemoveDomainService(UsermailRepo usermailRepo, UsermailMsgReplyRepo usermailMsgReplyRepo,
      UsermailBlacklistRepo usermailBlacklistRepo,
      UsermailBoxRepo usermailBoxRepo) {
    this.usermailRepo = usermailRepo;
    this.usermailMsgReplyRepo = usermailMsgReplyRepo;
    this.usermailBlacklistRepo = usermailBlacklistRepo;
    this.usermailBoxRepo = usermailBoxRepo;
  }

  /**
   * 清理指定域的所有数据
   *
   * @param domain 域
   */
  public void removeDomain(String domain) {
    try {
      usermailRepo.removeDomain(domain, pageSize);
      usermailMsgReplyRepo.removeDomain(domain, pageSize);
      usermailBlacklistRepo.removeDomain(domain, pageSize);
      usermailBoxRepo.removeDomain(domain, pageSize);
    } catch (InterruptedException e) {
      log.error("RemoveDomainService.removeDomain() encounter exception, paran:[{}], ", domain, e);
      Thread.currentThread().interrupt();
      throw new UserMailException(e);
    }

  }

}
