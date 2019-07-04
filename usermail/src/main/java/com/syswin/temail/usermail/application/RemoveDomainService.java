package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.common.Constants.UsermailAgentEventType;
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


  @Value("${app.temail.usermailagent.clear.domain.enabled:false}")
  private String removeEnabled;
  @Value("${app.temail.usermailagent.clear.domain.pageSize:100}")
  private Integer pageSize;
  private final UsermailRepo usermailRepo;
  private final UsermailMsgReplyRepo usermailMsgReplyRepo;
  private final UsermailBlacklistRepo usermailBlacklistRepo;
  private final UsermailBoxRepo usermailBoxRepo;
  private final UsermailMqService usermailMqService;

  public RemoveDomainService(UsermailRepo usermailRepo, UsermailMsgReplyRepo usermailMsgReplyRepo,
      UsermailBlacklistRepo usermailBlacklistRepo, UsermailBoxRepo usermailBoxRepo,
      UsermailMqService usermailMqService) {
    this.usermailRepo = usermailRepo;
    this.usermailMsgReplyRepo = usermailMsgReplyRepo;
    this.usermailBlacklistRepo = usermailBlacklistRepo;
    this.usermailBoxRepo = usermailBoxRepo;
    this.usermailMqService = usermailMqService;
  }

  /**
   * 清理指定域的所有数据
   *
   * @param domain 域
   */
  public void removeDomain(String domain) {
    Boolean enabled = Boolean.valueOf(removeEnabled);
    if (enabled) {
      usermailMqService.sendMqRemoveDomain(domain, UsermailAgentEventType.REMOVE_ALL_USERMAIL_7);
      usermailMqService.sendMqRemoveDomain(domain, UsermailAgentEventType.REMOVE_ALL_USERMAIL_MSG_REPLY_8);
      usermailMqService.sendMqRemoveDomain(domain, UsermailAgentEventType.REMOVE_ALL_USERMAIL_BLACK_LIST_9);
      usermailMqService.sendMqRemoveDomain(domain, UsermailAgentEventType.REMOVE_ALL_USERMAIL_BOX_10);
    } else {
      log.info("RemoveDomainService.removeDomain() remove domain is not enabled, removeEnabled: [{}]", removeEnabled);
    }
  }

  /**
   * 异步mq-清空指定域全部单聊消息
   *
   * @param domain 域
   */
  public void removeUsermail(String domain) {
    log.info("RemoveDomainService.removeUsermail() begin, domain: [{}], pageSize: [{}]", domain, pageSize);
    int count;
    do {
      count = usermailRepo.deleteDomain(domain, pageSize);
    } while (count > 0);
    log.info("RemoveDomainService.removeUsermail() complete, domain: [{}], pageSize: [{}]", domain, pageSize);
  }

  /**
   * 异步mq-清空指定域全部单聊回复消息
   *
   * @param domain 域
   */
  public void removeMsgReply(String domain) {
    log.info("RemoveDomainService.removeMsgReply() begin, domain: [{}], pageSize: [{}]", domain, pageSize);
    int count;
    do {
      count = usermailMsgReplyRepo.deleteDomain(domain, pageSize);
    } while (count > 0);
    log.info("RemoveDomainService.removeMsgReply() complete, domain: [{}], pageSize: [{}]", domain, pageSize);
  }

  /**
   * 异步mq-清空指定域全部单聊黑名单信息
   *
   * @param domain 域
   */
  public void removeBlack(String domain) {
    log.info("RemoveDomainService.removeBlack() begin, domain: [{}], pageSize: [{}]", domain, pageSize);
    int count;
    do {
      count = usermailBlacklistRepo.deleteDomain(domain, pageSize);
    } while (count > 0);
    log.info("RemoveDomainService.removeBlack() complete, domain: [{}], pageSize: [{}]", domain, pageSize);
  }

  /**
   * 异步mq-清空指定域全部单聊会话信息
   *
   * @param domain 域
   */
  public void removeBox(String domain) {
    log.info("RemoveDomainService.removeBox() begin, domain: [{}], pageSize: [{}]", domain, pageSize);
    int count;
    do {
      count = usermailBoxRepo.deleteDomain(domain, pageSize);
    } while (count > 0);
    log.info("RemoveDomainService.removeBox() complete, domain: [{}], pageSize: [{}]", domain, pageSize);
  }
}
