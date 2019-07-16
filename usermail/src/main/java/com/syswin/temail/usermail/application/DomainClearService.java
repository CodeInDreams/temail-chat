package com.syswin.temail.usermail.application;

import com.syswin.temail.usermail.common.Constants.UsermailAgentEventType;
import com.syswin.temail.usermail.infrastructure.domain.IUsermailBlacklistDB;
import com.syswin.temail.usermail.infrastructure.domain.IUsermailMsgDB;
import com.syswin.temail.usermail.infrastructure.domain.IUsermailMsgReplyDB;
import com.syswin.temail.usermail.infrastructure.domain.IUsermailBoxDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DomainClearService {


  @Value("${app.usermailagent.clear.domain.enabled:false}")
  private String enabled;
  @Value("${app.usermailagent.clear.domain.pageSize:100}")
  private Integer pageSize;
  private final IUsermailMsgDB usermailMsgDB;
  private final IUsermailMsgReplyDB usermailMsgReplyDB;
  private final IUsermailBlacklistDB usermailBlacklistDB;
  private final IUsermailBoxDB usermailBoxDB;
  private final UsermailMqService usermailMqService;

  public DomainClearService(IUsermailMsgDB usermailMsgDB, IUsermailMsgReplyDB usermailMsgReplyDB,
      IUsermailBlacklistDB usermailBlacklistDB, IUsermailBoxDB usermailBoxDB,
      UsermailMqService usermailMqService) {
    this.usermailMsgDB = usermailMsgDB;
    this.usermailMsgReplyDB = usermailMsgReplyDB;
    this.usermailBlacklistDB = usermailBlacklistDB;
    this.usermailBoxDB = usermailBoxDB;
    this.usermailMqService = usermailMqService;
  }

  /**
   * 清理指定域的所有数据
   *
   * @param domain 域
   */
  public void clearDomainAll(String domain) {
    Boolean enabled = Boolean.valueOf(this.enabled);
    if (enabled) {
      log.info("label-DomainClearService.clearDomainAll() clear domain begin, domain: [{}]", domain);
      usermailMqService.sendMqClearDomain(domain, UsermailAgentEventType.CLEAR_ALL_USERMAIL_7);
      usermailMqService.sendMqClearDomain(domain, UsermailAgentEventType.CLEAR_ALL_USERMAIL_MSG_REPLY_8);
      usermailMqService.sendMqClearDomain(domain, UsermailAgentEventType.CLEAR_ALL_USERMAIL_BLACK_LIST_9);
      usermailMqService.sendMqClearDomain(domain, UsermailAgentEventType.CLEAR_ALL_USERMAIL_BOX_10);
    } else {
      log.info("label-DomainClearService.clearDomainAll() clear domain is not enabled, enabled: [{}]", this.enabled);
    }
  }

  /**
   * 异步mq-清空指定域全部单聊消息
   *
   * @param domain 域
   */
  public void clearUsermailAll(String domain) {
    log.info("label-DomainClearService.clearUsermailAll() begin, domain: [{}], pageSize: [{}]", domain, pageSize);
    int count;
    do {
      count = usermailMsgDB.deleteDomain(domain, pageSize);
    } while (count > 0);
    log.info("label-DomainClearService.clearUsermailAll() complete, domain: [{}], pageSize: [{}]", domain, pageSize);
  }

  /**
   * 异步mq-清空指定域全部单聊回复消息
   *
   * @param domain 域
   */
  public void clearMsgReplyAll(String domain) {
    log.info("label-DomainClearService.clearMsgReplyAll() begin, domain: [{}], pageSize: [{}]", domain, pageSize);
    int count;
    do {
      count = usermailMsgReplyDB.deleteDomain(domain, pageSize);
    } while (count > 0);
    log.info("label-DomainClearService.clearMsgReplyAll() complete, domain: [{}], pageSize: [{}]", domain, pageSize);
  }

  /**
   * 异步mq-清空指定域全部单聊黑名单信息
   *
   * @param domain 域
   */
  public void clearBlackAll(String domain) {
    log.info("label-DomainClearService.clearBlackAll() begin, domain: [{}], pageSize: [{}]", domain, pageSize);
    int count;
    do {
      count = usermailBlacklistDB.deleteDomain(domain, pageSize);
    } while (count > 0);
    log.info("label-DomainClearService.clearBlackAll() complete, domain: [{}], pageSize: [{}]", domain, pageSize);
  }

  /**
   * 异步mq-清空指定域全部单聊会话信息
   *
   * @param domain 域
   */
  public void clearBoxAll(String domain) {
    log.info("label-DomainClearService.clearBoxAll() begin, domain: [{}], pageSize: [{}]", domain, pageSize);
    int count;
    do {
      count = usermailBoxDB.deleteDomain(domain, pageSize);
    } while (count > 0);
    log.info("label-DomainClearService.clearBoxAll() complete, domain: [{}], pageSize: [{}]", domain, pageSize);
  }
}
