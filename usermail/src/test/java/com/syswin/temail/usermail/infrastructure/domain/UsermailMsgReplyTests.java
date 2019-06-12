package com.syswin.temail.usermail.infrastructure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.syswin.temail.usermail.UsermailAgentApplication;
import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.domains.UsermailMsgReply;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(UsermailAgentApplication.class)
@ActiveProfiles("test")
public class UsermailMsgReplyTests {

  @Autowired
  private UsermailMsgReplyRepo usermailMsgReplyRepo;

  @Test
  public void saveMsgReply() {
    UsermailMsgReply usermailMsgReply = new UsermailMsgReply();
    usermailMsgReply.setParentMsgid("syswin-1543456947958");
    usermailMsgReply.setFrom("A2018");
    usermailMsgReply.setStatus(0);
    usermailMsgReply.setMsgid("syswin-1543572005953");
    usermailMsgReply.setId(184);
    usermailMsgReply.setSeqNo(0);
    usermailMsgReply.setTo("B2018");
    usermailMsgReply.setOwner("A2018");
    usermailMsgReply.setType(1);
    usermailMsgReply.setMsg("testsavemethod");
    usermailMsgReply.setSessionid("jkasjkaslkjaskl");
    usermailMsgReplyRepo.insert(usermailMsgReply);
    UsermailMsgReply msgBean = getMsgBean();
    Assert.assertNotNull(msgBean);
  }

  @Test
  public void getMsgReply() {
    UsermailMsgReply usermailMsgReply = new UsermailMsgReply();
    usermailMsgReply.setParentMsgid("syswin-1543456947958");
    usermailMsgReply.setFrom("A2018");
    usermailMsgReply.setStatus(TemailStatus.STATUS_REVERT_1);
    usermailMsgReply.setMsgid("syswin-1543572005953");
    usermailMsgReply.setOwner("A2018");
    usermailMsgReply.setTo("B2018");
    usermailMsgReplyRepo.getMsgReplyByCondition(usermailMsgReply);
    Assert.assertTrue(true);
  }

  @Test
  public void getMsgReplys() {
    QueryMsgReplyDTO dto = new QueryMsgReplyDTO();
    dto.setParentMsgid("syswin-1543456947958");
    dto.setOwner("A2018");
    dto.setFromSeqNo(0);
    dto.setPageSize(1);
    dto.setSignal("before");
    usermailMsgReplyRepo.getMsgReplys(dto);
    Assert.assertTrue(true);
  }

  @Test
  public void updateBatchMsgReplys() {
    List<String> msgIds = new ArrayList<>(1);
    msgIds.add("syswin-1543572005953");
    usermailMsgReplyRepo.deleteBatchMsgReplyStatus("A2018", msgIds);
    Assert.assertTrue(true);
  }

  private UsermailMsgReply getMsgBean() {
    UsermailMsgReply queryParam = new UsermailMsgReply();
    queryParam.setParentMsgid("syswin-1543456947958");
    queryParam.setMsgid("syswin-1543572005953");
    queryParam.setFrom("A2018");
    queryParam.setTo("B2018");
    queryParam.setOwner("A2018");
    return usermailMsgReplyRepo.getMsgReplyByCondition(queryParam);
  }

  @Test
  public void deleteMsgByParentIdAndOwner() {
    UsermailMsgReply usermailMsgReply = new UsermailMsgReply();
    usermailMsgReply.setParentMsgid("syswin-1543456947958");
    usermailMsgReply.setFrom("A2018");
    usermailMsgReply.setStatus(0);
    usermailMsgReply.setMsgid("syswin-1543572005953");
    usermailMsgReply.setId(1841);
    usermailMsgReply.setSeqNo(0);
    usermailMsgReply.setTo("B2018");
    usermailMsgReply.setOwner("A2018");
    usermailMsgReply.setType(1);
    usermailMsgReply.setMsg("testsavemethod");
    usermailMsgReply.setSessionid("lkjasdjlk;sadklj");
    usermailMsgReplyRepo.insert(usermailMsgReply);
    List<String> parentMsgIds = new ArrayList<>();
    parentMsgIds.add(usermailMsgReply.getParentMsgid());
    int delete = usermailMsgReplyRepo.deleteMsgByParentIdAndOwner(usermailMsgReply.getOwner(), parentMsgIds);
    Assert.assertTrue(delete > 0);
  }

  @Test
  public void batchDeleteBySessionId() throws Exception {
    int count = usermailMsgReplyRepo.batchDeleteBySessionId("", "test@temail.com");
    assertThat(count).isEqualTo(0);
  }

  @Test
  public void destoryAfterRead() {
    UsermailMsgReply usermailMsgReply = new UsermailMsgReply();
    usermailMsgReply.setParentMsgid("syswin-1543456947958");
    usermailMsgReply.setFrom("A2018");
    usermailMsgReply.setStatus(0);
    usermailMsgReply.setMsgid("test111111");
    usermailMsgReply.setOwner("A@systoontest.com");
    usermailMsgReply.setId(1841112);
    usermailMsgReply.setSeqNo(0);
    usermailMsgReply.setTo("B2018");
    usermailMsgReply.setType(1);
    usermailMsgReply.setMsg("testsavemethod");
    usermailMsgReply.setSessionid("lkjasdjlk;sadklj");
    usermailMsgReplyRepo.insert(usermailMsgReply);
    int count  = usermailMsgReplyRepo.destoryAfterRead("A@systoontest.com","test111111", TemailStatus.STATUS_DESTORY_AFTER_READ_2);
    Assert.assertEquals(1,count);
    Assert.assertTrue(true);
  }

  @Test
  public void revertTest() {
    UsermailMsgReply usermailMsgReply = new UsermailMsgReply();
    usermailMsgReply.setParentMsgid("syswin-1543456947958");
    usermailMsgReply.setFrom("A2018");
    usermailMsgReply.setStatus(0);
    usermailMsgReply.setMsgid("syswin-154357200521211212953");
    usermailMsgReply.setId(184112);
    usermailMsgReply.setSeqNo(0);
    usermailMsgReply.setTo("B2018");
    usermailMsgReply.setOwner("A2018");
    usermailMsgReply.setType(1);
    usermailMsgReply.setMsg("testsavemethod");
    usermailMsgReply.setSessionid("lkjasdjlk;sadklj");
    usermailMsgReplyRepo.insert(usermailMsgReply);
    usermailMsgReply.setStatus(TemailStatus.STATUS_REVERT_1);
    int count  = usermailMsgReplyRepo.revertUsermailReply(usermailMsgReply);
    Assert.assertEquals(1,count);
    Assert.assertTrue(true);
  }
}
