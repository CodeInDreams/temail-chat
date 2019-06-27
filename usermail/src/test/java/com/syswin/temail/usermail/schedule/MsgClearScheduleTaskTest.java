package com.syswin.temail.usermail.schedule;

import com.syswin.temail.usermail.application.HistoryMsgClearService;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Ignore
public class MsgClearScheduleTaskTest {

  private String cron = "";
  private Integer beforeDays = 1;
  private Integer batchNum = 10;

  @InjectMocks
  private MsgClearScheduleTask task;
  @Mock
  private HistoryMsgClearService service;

  ScheduledTaskRegistrar registrar;

  @Test
  public void configureTasks() {
    task.configureTasks(registrar);
  }

}