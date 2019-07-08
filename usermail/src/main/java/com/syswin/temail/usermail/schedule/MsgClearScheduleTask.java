package com.syswin.temail.usermail.schedule;

import com.syswin.temail.usermail.application.HistoryMsgClearService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Slf4j
@EnableScheduling
@Component
@ConditionalOnProperty(name = "app.usermailagent.clear.msg.task.enabled", havingValue = "true")
@ConfigurationProperties(prefix = "app.usermailagent.clear.msg.task")
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
      try {
        clearService.deleteHistoryMsg(beforeDays, batchNum);
      } catch (Exception e) {
        log.error("label-MsgClearScheduleTask-exception: ", e);
      }
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
