package com.syswin.temail.usermail.schedule;

import com.syswin.temail.usermail.application.HistoryMsgClearService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@ConditionalOnProperty(name = "app.temail.usermailagent.clear.msg.task.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "app.temail.usermailagent.clear.msg.task")
public class MsgClearScheduleTask implements SchedulingConfigurer {

  private String cron;
  private Integer beforeDays;
  private Integer batchNum;
  private final HistoryMsgClearService clearService;

  public MsgClearScheduleTask(HistoryMsgClearService clearService) {
    this.clearService = clearService;
  }

  @Override
  public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
    scheduledTaskRegistrar.addTriggerTask(() -> {
      clearService.deleteHistoryMsg(beforeDays, batchNum);
    }, new CronTrigger(cron));
  }

  public void setCron(String cron) {
    this.cron = cron;
  }

  public void setBeforeDays(Integer beforeDays) {
    this.beforeDays = beforeDays;
  }

  public void setBatchNum(Integer batchNum) {
    this.batchNum = batchNum;
  }
}
