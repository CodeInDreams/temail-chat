/*
 * MIT License
 *
 * Copyright (c) 2019 Syswin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.syswin.temail.usermail.infrastructure.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.domains.UsermailMsgReplyDO;
import com.syswin.temail.usermail.dto.QueryMsgReplyDTO;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailMsgReplyMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@Ignore
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class UsermailMsgReplyTest {

  private static Set<Long> existIds = new HashSet<>();

  @Autowired
  private UsermailMsgReplyMapper usermailMsgReplyMapper;

  @Test
  public void saveMsgReplyTest() {
    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
    usermailMsgReply.setParentMsgid("syswin-1543456947958");
    usermailMsgReply.setFrom("A2018");
    usermailMsgReply.setStatus(0);
    usermailMsgReply.setMsgid("syswin-1543572005953");
    usermailMsgReply.setId(this.generatePKid());
    usermailMsgReply.setSeqNo(0);
    usermailMsgReply.setTo("B2018");
    usermailMsgReply.setOwner("A2018");
    usermailMsgReply.setType(1);
    usermailMsgReply.setMsg("testsavemethod");
    usermailMsgReply.setSessionid("jkasjkaslkjaskl");
    usermailMsgReplyMapper.insert(usermailMsgReply);
    UsermailMsgReplyDO msgBean = selectMsgReplyByConditon();
    Assert.assertNotNull(msgBean);
  }

  @Test
  public void selectMsgReplyTest() {
    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
    usermailMsgReply.setParentMsgid("syswin-1543456947958");
    usermailMsgReply.setFrom("A2018");
    usermailMsgReply.setStatus(TemailStatus.STATUS_REVERT_1);
    usermailMsgReply.setMsgid("syswin-1543572005953");
    usermailMsgReply.setOwner("A2018");
    usermailMsgReply.setTo("B2018");
    usermailMsgReplyMapper.selectMsgReplyByCondition(usermailMsgReply);
    Assert.assertTrue(true);
  }

  @Test
  public void listMsgReplysTest() {
    QueryMsgReplyDTO dto = new QueryMsgReplyDTO();
    dto.setParentMsgid("syswin-1543456947958");
    dto.setOwner("A2018");
    dto.setFromSeqNo(0);
    dto.setPageSize(1);
    dto.setSignal("before");
    usermailMsgReplyMapper.listMsgReplys(dto);
    Assert.assertTrue(true);
  }

  @Test
  public void updateBatchMsgReplysTest() {
    List<String> msgIds = new ArrayList<>(1);
    msgIds.add("syswin-1543572005953");
    usermailMsgReplyMapper.deleteMsgReplysByMsgIds("A2018", msgIds);
    Assert.assertTrue(true);
  }

  private UsermailMsgReplyDO selectMsgReplyByConditon() {
    UsermailMsgReplyDO queryParam = new UsermailMsgReplyDO();
    queryParam.setParentMsgid("syswin-1543456947958");
    queryParam.setMsgid("syswin-1543572005953");
    queryParam.setFrom("A2018");
    queryParam.setTo("B2018");
    queryParam.setOwner("A2018");
    return usermailMsgReplyMapper.selectMsgReplyByCondition(queryParam);
  }

  @Test
  public void deleteMsgByParentIdAndOwnerTest() {
    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
    usermailMsgReply.setParentMsgid("syswin-1543456947958");
    usermailMsgReply.setFrom("A2018");
    usermailMsgReply.setStatus(0);
    usermailMsgReply.setMsgid("syswin-1543572005953");
    usermailMsgReply.setId(this.generatePKid());
    usermailMsgReply.setSeqNo(0);
    usermailMsgReply.setTo("B2018");
    usermailMsgReply.setOwner("A2018");
    usermailMsgReply.setType(1);
    usermailMsgReply.setMsg("testsavemethod");
    usermailMsgReply.setSessionid("lkjasdjlk;sadklj");
    usermailMsgReplyMapper.insert(usermailMsgReply);
    List<String> parentMsgIds = new ArrayList<>();
    parentMsgIds.add(usermailMsgReply.getParentMsgid());
    int delete = usermailMsgReplyMapper.deleteMsgReplysByParentIds(usermailMsgReply.getOwner(), parentMsgIds);
    Assert.assertTrue(delete > 0);
  }

  @Test
  public void batchDeleteBySessionIdTest() {
    int count = usermailMsgReplyMapper.deleteMsgReplysBySessionId("", "test@temail.com");
    assertThat(count).isEqualTo(0);
  }

  @Test
  public void updateDestroyAfterReadTest() {
    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
    usermailMsgReply.setParentMsgid("syswin-1543456947958");
    usermailMsgReply.setFrom("A2018");
    usermailMsgReply.setStatus(0);
    usermailMsgReply.setMsgid("test111111");
    usermailMsgReply.setOwner("A@systoontest.com");
    usermailMsgReply.setId(this.generatePKid());
    usermailMsgReply.setSeqNo(0);
    usermailMsgReply.setTo("B2018");
    usermailMsgReply.setType(1);
    usermailMsgReply.setMsg("testsavemethod");
    usermailMsgReply.setSessionid("lkjasdjlk;sadklj");
    usermailMsgReplyMapper.insert(usermailMsgReply);
    int count = usermailMsgReplyMapper
        .updateDestroyAfterRead("A@systoontest.com", "test111111", TemailStatus.STATUS_DESTROY_AFTER_READ_2,
            TemailStatus.STATUS_NORMAL_0);
    Assert.assertEquals(1, count);
    Assert.assertTrue(true);
  }

  @Test
  public void batchUpdateByParentMsgIdsTest() {
    String owner = "A2019";

    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
    String parentMsgid = "syswin-1543456947958";
    usermailMsgReply.setParentMsgid(parentMsgid);
    usermailMsgReply.setFrom("A2019");
    usermailMsgReply.setStatus(0);
    usermailMsgReply.setMsgid("syswin-154357200521211212953");
    usermailMsgReply.setId(this.generatePKid());
    usermailMsgReply.setSeqNo(0);
    usermailMsgReply.setTo("B2018");
    usermailMsgReply.setOwner(owner);
    usermailMsgReply.setType(1);
    usermailMsgReply.setMsg("testsavemethod");
    usermailMsgReply.setSessionid("lkjasdjlk;sadklj");
    usermailMsgReplyMapper.insert(usermailMsgReply);
    List<String> parentMsgids = Arrays.asList(parentMsgid);
    int status = TemailStatus.STATUS_TRASH_4;
    int count = usermailMsgReplyMapper.updateMsgReplysByParentIds(owner, parentMsgids, status);

    assertThat(count).isOne();
  }

  @Test
  public void batchDeleteByStatusTest() {
    String owner = "A2018";
    int status = TemailStatus.STATUS_NORMAL_0;

    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
    usermailMsgReply.setParentMsgid("syswin-1543456947958");
    usermailMsgReply.setFrom("A2018");
    usermailMsgReply.setStatus(status);
    usermailMsgReply.setMsgid("syswin-154357200521211212953");
    usermailMsgReply.setId(this.generatePKid());
    usermailMsgReply.setSeqNo(0);
    usermailMsgReply.setTo("B2018");
    usermailMsgReply.setOwner(owner);
    usermailMsgReply.setType(1);
    usermailMsgReply.setMsg("testsavemethod");
    usermailMsgReply.setSessionid("lkjasdjlk;sadklj");
    usermailMsgReplyMapper.insert(usermailMsgReply);

    int count = usermailMsgReplyMapper.deleteMsgReplysByStatus(owner, status);

    assertThat(count).isOne();
  }

  @Test
  public void selectLastUsermailReplyTest() {
    String owner = "A2018";
    String parentMsgid = "syswin-1543456947958";
    int status = TemailStatus.STATUS_NORMAL_0;
    int seqNo_first = 0;
    int seqNo_second = 1;

    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
    usermailMsgReply.setParentMsgid(parentMsgid);
    usermailMsgReply.setFrom("A2018");
    usermailMsgReply.setStatus(status);
    usermailMsgReply.setMsgid("syswin-154357200521211212953");
    usermailMsgReply.setId(this.generatePKid());
    usermailMsgReply.setSeqNo(seqNo_first);
    usermailMsgReply.setTo("B2018");
    usermailMsgReply.setOwner(owner);
    usermailMsgReply.setType(1);
    usermailMsgReply.setMsg("testsavemethod");
    usermailMsgReply.setSessionid("lkjasdjlk;sadklj");
    usermailMsgReplyMapper.insert(usermailMsgReply);
    usermailMsgReply.setId(this.generatePKid());
    usermailMsgReply.setMsgid("syswin-154357200521211212954");
    usermailMsgReply.setSeqNo(seqNo_second);
    usermailMsgReplyMapper.insert(usermailMsgReply);

    UsermailMsgReplyDO lastUsermailReply = usermailMsgReplyMapper.selectLastUsermailReply(parentMsgid, owner, status);

    assertThat(lastUsermailReply).isNotNull();
    assertThat(lastUsermailReply.getSeqNo()).isEqualTo(seqNo_second);

  }

  @Test
  public void updateRevertUsermailReplyTest() {
    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
    usermailMsgReply.setParentMsgid("syswin-1543456947958");
    usermailMsgReply.setFrom("A2018");
    usermailMsgReply.setStatus(0);
    usermailMsgReply.setMsgid("syswin-1543572005212112129531");
    usermailMsgReply.setId(this.generatePKid());
    usermailMsgReply.setSeqNo(0);
    usermailMsgReply.setTo("B2018");
    usermailMsgReply.setOwner("A20181");
    usermailMsgReply.setType(1);
    usermailMsgReply.setMsg("testRevert");
    usermailMsgReply.setSessionid("lkjasdjlk;sadklj1");
    usermailMsgReplyMapper.insert(usermailMsgReply);
    usermailMsgReply.setStatus(TemailStatus.STATUS_REVERT_1);
    int count = usermailMsgReplyMapper.updateRevertUsermailReply(usermailMsgReply, TemailStatus.STATUS_NORMAL_0);
    Assert.assertEquals(1, count);
    Assert.assertTrue(true);
  }

  @Test
  public void deleteMsgReplyLessThanTest() {
    UsermailMsgReplyDO usermailMsgReply1 = new UsermailMsgReplyDO();
    usermailMsgReply1.setParentMsgid("syswin-1543456947958");
    usermailMsgReply1.setFrom("A2018");
    usermailMsgReply1.setStatus(0);
    usermailMsgReply1.setMsgid("syswin-reply-201906271700-001");
    usermailMsgReply1.setId(this.generatePKid());
    usermailMsgReply1.setSeqNo(0);
    usermailMsgReply1.setTo("B2018");
    usermailMsgReply1.setOwner("A2018");
    usermailMsgReply1.setType(1);
    usermailMsgReply1.setMsg("testsavemethod");
    usermailMsgReply1.setSessionid("jkasjkaslkjaskl");
    usermailMsgReplyMapper.insert(usermailMsgReply1);
    UsermailMsgReplyDO usermailMsgReply2 = new UsermailMsgReplyDO();
    usermailMsgReply2.setParentMsgid("syswin-1543456947958");
    usermailMsgReply2.setFrom("A2018");
    usermailMsgReply2.setStatus(0);
    usermailMsgReply2.setMsgid("syswin-reply-201906271700-002");
    usermailMsgReply2.setId(this.generatePKid());
    usermailMsgReply2.setSeqNo(0);
    usermailMsgReply2.setTo("B2018");
    usermailMsgReply2.setOwner("A2018");
    usermailMsgReply2.setType(1);
    usermailMsgReply2.setMsg("testsavemethod");
    usermailMsgReply2.setSessionid("jkasjkaslkjaskl");
    usermailMsgReplyMapper.insert(usermailMsgReply2);

    LocalDate createTime = LocalDate.now().plusDays(1);
    int batchNum = 1;
    int result = usermailMsgReplyMapper.deleteMsgReplyLessThan(createTime, batchNum);

    assertThat(result).isOne();
  }

  @Test
  public void deleteDomainTest() {
    String domain = "deletedomain";
    String from = "A2018@" + domain;
    String to = "B2018@t.email";
    UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
    usermailMsgReply.setParentMsgid("syswin-1543456947958");
    usermailMsgReply.setFrom(from);
    usermailMsgReply.setStatus(0);
    usermailMsgReply.setMsgid("syswin-1543572005953");
    usermailMsgReply.setId(this.generatePKid());
    usermailMsgReply.setSeqNo(0);
    usermailMsgReply.setTo(to);
    usermailMsgReply.setOwner(from);
    usermailMsgReply.setType(1);
    usermailMsgReply.setMsg("testsavemethod");
    usermailMsgReply.setSessionid("jkasjkaslkjaskl");
    usermailMsgReplyMapper.insert(usermailMsgReply);
    usermailMsgReply.setId(this.generatePKid());
    usermailMsgReply.setMsgid(UUID.randomUUID().toString());
    usermailMsgReplyMapper.insert(usermailMsgReply);
    usermailMsgReply.setId(this.generatePKid());
    usermailMsgReply.setOwner(to);
    usermailMsgReplyMapper.insert(usermailMsgReply);
    int pageSize = 1;
    int count = usermailMsgReplyMapper.deleteDomain(domain, pageSize);

    assertThat(count).isOne();
  }

  private long generatePKid() {
    boolean isUnique;
    long id;
    do {
      id = new Random().nextInt(100);
      isUnique = existIds.add(id);
    } while (!isUnique);
    return id;
  }
}
