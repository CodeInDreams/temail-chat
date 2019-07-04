package com.syswin.temail.usermail.application;

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


  @Value("${app.temail.usermailagent.clear.domain.pageSize:100}")
  private Integer pageSize;
  private final UsermailRepo usermailRepo;
  private final UsermailMsgReplyRepo usermailMsgReplyRepo;
  private final UsermailBlacklistRepo usermailBlacklistRepo;
  private final UsermailBoxRepo usermailBoxRepo;

  public RemoveDomainService(UsermailRepo usermailRepo, UsermailMsgReplyRepo usermailMsgReplyRepo,
      UsermailBlacklistRepo usermailBlacklistRepo, UsermailBoxRepo usermailBoxRepo) {
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
    this.removeUsermail(domain, pageSize);
    this.removeMsgReply(domain, pageSize);
    this.removeBlack(domain, pageSize);
    this.removeBox(domain, pageSize);
  }

  private void removeUsermail(String domain, int pageSize) {
    int count;
    do {
      count = usermailRepo.deleteDomain(domain, pageSize);
    } while (count > 0);
    log.info("RemoveDomainService.removeUsermail() complete, domain: [{}], pageSize: [{}]", domain, pageSize);
  }

  private void removeMsgReply(String domain, int pageSize) {
    int count;
    do {
      count = usermailMsgReplyRepo.deleteDomain(domain, pageSize);
    } while (count > 0);
    log.info("RemoveDomainService.removeMsgReply() complete, domain: [{}], pageSize: [{}]", domain, pageSize);
  }

  private void removeBlack(String domain, int pageSize) {
    int count;
    do {
      count = usermailBlacklistRepo.deleteDomain(domain, pageSize);
    } while (count > 0);
    log.info("RemoveDomainService.removeBlack() complete, domain: [{}], pageSize: [{}]", domain, pageSize);
  }

  private void removeBox(String domain, int pageSize) {
    int count;
    do {
      count = usermailBoxRepo.deleteDomain(domain, pageSize);
    } while (count > 0);
    log.info("RemoveDomainService.removeBox() complete, domain: [{}], pageSize: [{}]", domain, pageSize);
  }
}
