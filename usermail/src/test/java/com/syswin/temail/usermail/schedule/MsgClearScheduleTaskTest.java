package com.syswin.temail.usermail.schedule;

import com.syswin.temail.usermail.application.HistoryMsgClearService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@RunWith(MockitoJUnitRunner.class)
public class MsgClearScheduleTaskTest {

  private String cron = "0/1 * * * * ?";
  private Integer beforeDays = 1;
  private Integer batchNum = 10;

  @InjectMocks
  private MsgClearScheduleTask task;
  @Autowired
  private HistoryMsgClearService service;

  ScheduledTaskRegistrar registrar = new ScheduledTaskRegistrar();

  @Before
  public void setUp() {
    task.setCron(cron);
    task.setBeforeDays(beforeDays);
    task.setBatchNum(batchNum);
  }

  @Test
  public void configureTasks() {
    task.configureTasks(registrar);
  }

}