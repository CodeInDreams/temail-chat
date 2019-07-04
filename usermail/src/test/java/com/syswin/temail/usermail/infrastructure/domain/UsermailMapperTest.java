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
import com.syswin.temail.usermail.common.Constants.TemailType;
import com.syswin.temail.usermail.core.util.MsgCompressor;
import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.dto.QueryTrashDTO;
import com.syswin.temail.usermail.dto.RevertMailDTO;
import com.syswin.temail.usermail.dto.TrashMailDTO;
import com.syswin.temail.usermail.dto.UmQueryDTO;
import com.syswin.temail.usermail.infrastructure.domain.mapper.UsermailMapper;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
public class UsermailMapperTest {

  private static final String SESSIONID = "sessionid-1";
  private static Set<Long> existIds = new HashSet<>();
  private String msgid;
  private MsgCompressor msgCompressor = new MsgCompressor();

  @Autowired
  private UsermailMapper usermailMapper;

  @Before
  public void before() {
    msgid = "syswin-" + UUID.randomUUID().toString();
  }

  @Test
  public void insertUsermail() {
    UsermailDO userMail = new UsermailDO();
    userMail.setSessionid(SESSIONID);
    String from = "from@syswin.com";
    String to = "to@syswin.com";
    userMail.setId(this.generatePKid());
    userMail.setFrom(from);
    userMail.setTo(to);
    userMail.setOwner(from);
    userMail.setMsgid(msgid);
    userMail.setSeqNo(11);
    userMail.setType(TemailType.TYPE_NORMAL_0);
    userMail.setStatus(TemailStatus.STATUS_NORMAL_0);
    userMail.setMessage("");
    userMail.setAuthor(from);
    userMail.setFilter(null);
    usermailMapper.insertUsermail(userMail);
    long lastSeqno = 0;
    UmQueryDTO umQueryDto = new UmQueryDTO();
    umQueryDto.setPageSize(2);
    umQueryDto.setFromSeqNo(lastSeqno);
    umQueryDto.setSessionid(SESSIONID);
    umQueryDto.setOwner(from);
    List<UsermailDO> usermail = usermailMapper.listUsermails(umQueryDto);
    Assert.assertNotNull(usermail);
    Assert.assertTrue(usermail.size() >= 1);
  }

  @Test
  public void listLastUsermails() {
    UmQueryDTO umQueryDto = new UmQueryDTO();
    umQueryDto.setSessionid("123456789");
    umQueryDto.setOwner("to@syswin.com");
    List<UsermailDO> usermails = usermailMapper.listLastUsermails(umQueryDto);
    Assert.assertEquals(0, usermails.size());
  }

  @Test
  public void deleteMsg() {
    List<String> msgIds = new ArrayList<>();
    msgIds.add("ldfk");
    msgIds.add("syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707");
    int count = usermailMapper.deleteMsg(msgIds, "from@syswin.com");
    assertThat(count).isEqualTo(0);
  }

  @Test
  public void selectUsermailByMsgid() {
    UsermailDO usermailByMsgid = usermailMapper
        .selectByMsgidAndOwner("syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707", "from@syswin.com");
    Assert.assertNull(usermailByMsgid);
  }

  @Test
  public void updateDestroyAfterReadStatus() {
    usermailMapper
        .updateDestroyAfterReadStatus("from@syswin.com", "syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707",
            TemailStatus.STATUS_DESTROY_AFTER_READ_2);
    UsermailDO usermail = usermailMapper
        .selectByMsgidAndOwner("syswin-87532219-9c8a-41d6-976d-eaa805a145c1-1533886884707", "from@syswin.com");
    Assert.assertNull(usermail);
  }

  @Test
  public void listUsermails() {
    UsermailDO userMail = new UsermailDO();
    userMail.setSessionid(SESSIONID);
    String from = "from@syswin.com";
    String to = "to@syswin.com";
    userMail.setId(this.generatePKid());
    userMail.setFrom(from);
    userMail.setTo(to);
    userMail.setOwner(from);
    userMail.setZipMsg(msgCompressor.zip("test message".getBytes()));
    userMail.setMsgid(msgid);
    userMail.setSeqNo(11);
    userMail.setType(TemailType.TYPE_NORMAL_0);
    userMail.setStatus(TemailStatus.STATUS_NORMAL_0);
    userMail.setMessage("");
    userMail.setAuthor(from);
    userMail.setFilter(null);
    usermailMapper.insertUsermail(userMail);

    UmQueryDTO umQueryDto = new UmQueryDTO();
    umQueryDto.setSessionid(SESSIONID);
    umQueryDto.setOwner("from@syswin.com");
    umQueryDto.setPageSize(10);
    List<UsermailDO> usermails = usermailMapper.listUsermails(umQueryDto);
    assertThat(usermails.get(0).getFrom()).isEqualTo(from);
  }

  @Test
  public void listUsermailsWithSeqId() {
    UsermailDO userMail = new UsermailDO();
    userMail.setSessionid(SESSIONID);
    String from = "from@syswin.com";
    String to = "to@syswin.com";
    userMail.setId(this.generatePKid());
    userMail.setFrom(from);
    userMail.setTo(to);
    userMail.setOwner(from);
    userMail.setZipMsg(msgCompressor.zip("test message".getBytes()));
    userMail.setMsgid(msgid);
    userMail.setSeqNo(11);
    userMail.setType(TemailType.TYPE_NORMAL_0);
    userMail.setStatus(TemailStatus.STATUS_NORMAL_0);
    userMail.setMessage("");
    userMail.setAuthor(from);
    userMail.setFilter(null);
    usermailMapper.insertUsermail(userMail);

    UmQueryDTO umQueryDto = new UmQueryDTO();
    umQueryDto.setSessionid(SESSIONID);
    umQueryDto.setOwner(from);
    umQueryDto.setPageSize(10);
    umQueryDto.setFromSeqNo(10);
    List<UsermailDO> usermails = usermailMapper.listUsermails(umQueryDto);
    assertThat(usermails.get(0).getFrom()).isEqualTo(from);
  }

  @Test
  public void deleteBatchBySessionId() {
    int count = usermailMapper.deleteBySessionIdAndOwner("", "alice@temail.com");
    assertThat(count).isEqualTo(0);
  }

  @Test
  public void listUsermailsByMsgid() {
    UsermailDO userMail = new UsermailDO();
    userMail.setSessionid(SESSIONID);
    long id = this.generatePKid();
    String from = id + "from@syswin.com";
    String to = id + "to@syswin.com";
    String owner = from;
    int status = TemailStatus.STATUS_NORMAL_0;
    String msgid = UUID.randomUUID().toString();
    userMail.setId(id);
    userMail.setFrom(from);
    userMail.setTo(to);
    userMail.setOwner(owner);
    userMail.setZipMsg(msgCompressor.zip("test message".getBytes()));
    userMail.setMessage("");
    userMail.setMsgid(msgid);
    userMail.setSeqNo(0);
    userMail.setType(TemailType.TYPE_NORMAL_0);
    userMail.setStatus(status);
    userMail.setAuthor(from);
    userMail.setFilter(null);
    usermailMapper.insertUsermail(userMail);
    List<UsermailDO> usermails = usermailMapper.listUsermailsByMsgid(msgid);

    assertThat(usermails).isNotEmpty();
    assertThat(usermails.size()).isOne();
  }

  @Test
  public void listUsermailsByFromToMsgIds() {
    List<String> msgIds = new ArrayList<>();
    msgIds.add("AA");
    msgIds.add("BB");
    List<UsermailDO> mail = usermailMapper.listUsermailsByFromToMsgIds("a@systoontest.com", msgIds);
    Assert.assertEquals(0, mail.size());
  }

  @Test
  public void shouldUpdateReplyCountAndLastReplyMsgid() {
    long seqNoBefor = System.currentTimeMillis();
    String msgId = UUID.randomUUID().toString();
    String lastReplyMsgid = UUID.randomUUID().toString();
    //新建消息
    UsermailDO userMail = new UsermailDO();
    userMail.setSessionid(SESSIONID);
    String from = "from@syswin.com";
    String to = "to@syswin.com";
    userMail.setFrom(from);
    userMail.setTo(to);
    userMail.setOwner(from);
    userMail.setZipMsg(msgCompressor.zip("test message".getBytes()));
    userMail.setMessage("asdasd");
    userMail.setMsgid(msgId);
    userMail.setSeqNo(seqNoBefor);
    userMail.setType(TemailType.TYPE_NORMAL_0);
    userMail.setStatus(TemailStatus.STATUS_NORMAL_0);
    userMail.setAuthor(from);
    userMail.setFilter(null);
    usermailMapper.insertUsermail(userMail);
    //更新消息seqNo
    usermailMapper.updateReplyCountAndLastReplyMsgid(msgId, from, 1, lastReplyMsgid);
    //验证最新回复消息id与消息回复总数（1）是否正常更新
    UsermailDO usermailUpdated = usermailMapper.selectByMsgidAndOwner(msgId, from);
    Assert.assertEquals(usermailUpdated.getLastReplyMsgId(), lastReplyMsgid);
    Assert.assertTrue(usermailUpdated.getReplyCount() == 1);
  }

  @Test
  public void updateStatusByMsgIdsTest() {
    long seqNoBefor = System.currentTimeMillis();
    String msgId = UUID.randomUUID().toString();
    String lastReplyMsgid = UUID.randomUUID().toString();
    //新建消息
    UsermailDO userMail = new UsermailDO();
    userMail.setSessionid(SESSIONID);
    String from = "from@syswin.com";
    String to = "to@syswin.com";
    userMail.setId(seqNoBefor);
    userMail.setFrom(from);
    userMail.setTo(to);
    userMail.setOwner(from);
    userMail.setZipMsg(msgCompressor.zip("test message".getBytes()));
    userMail.setMessage("");
    userMail.setMsgid(msgId);
    userMail.setSeqNo(seqNoBefor);
    userMail.setType(TemailType.TYPE_NORMAL_0);
    userMail.setStatus(TemailStatus.STATUS_NORMAL_0);
    userMail.setAuthor(from);
    userMail.setFilter(null);
    usermailMapper.insertUsermail(userMail);

    List<String> msgIds = new ArrayList<>();
    msgIds.add(msgId);
    int count = usermailMapper.updateStatusByMsgIds(msgIds, from, TemailStatus.STATUS_TRASH_4);
    assertThat(count).isEqualTo(1);
  }

  @Test
  public void deleteMsgByStatus() {
    UsermailDO userMail = new UsermailDO();
    userMail.setSessionid(SESSIONID);
    long id = this.generatePKid();
    String from = id + "from@syswin.com";
    String to = id + "to@syswin.com";
    String owner = from;
    int status = TemailStatus.STATUS_NORMAL_0;
    String msgid = UUID.randomUUID().toString();
    userMail.setId(id);
    userMail.setFrom(from);
    userMail.setTo(to);
    userMail.setOwner(owner);
    userMail.setZipMsg(msgCompressor.zip("test message".getBytes()));
    userMail.setMessage("");
    userMail.setMsgid(msgid);
    userMail.setSeqNo(0);
    userMail.setType(TemailType.TYPE_NORMAL_0);
    userMail.setStatus(status);
    userMail.setAuthor(from);
    userMail.setFilter(null);
    usermailMapper.insertUsermail(userMail);

    TrashMailDTO trashMails = new TrashMailDTO();
    trashMails.setFrom(from);
    trashMails.setTo(to);
    trashMails.setMsgId(msgid);

    int count = usermailMapper.deleteMsgByStatus(Arrays.asList(trashMails), owner, status);

    assertThat(count).isOne();
  }

  @Test
  public void updateStatusByTemail() {
    // 还原废纸篓消息
    UsermailDO userMail = new UsermailDO();
    userMail.setSessionid(SESSIONID);
    long id = this.generatePKid();
    String from = id + "from@syswin.com";
    String to = id + "to@syswin.com";
    String owner = from;
    String msgid = UUID.randomUUID().toString();
    userMail.setId(id);
    userMail.setFrom(from);
    userMail.setTo(to);
    userMail.setOwner(owner);
    userMail.setZipMsg(msgCompressor.zip("test message".getBytes()));
    userMail.setMessage("");
    userMail.setMsgid(msgid);
    userMail.setSeqNo(0);
    userMail.setType(TemailType.TYPE_NORMAL_0);
    userMail.setStatus(TemailStatus.STATUS_TRASH_4);
    userMail.setAuthor(from);
    userMail.setFilter(null);
    usermailMapper.insertUsermail(userMail);

    TrashMailDTO trashMails = new TrashMailDTO();
    trashMails.setFrom(from);
    trashMails.setTo(to);
    trashMails.setMsgId(msgid);

    int count = usermailMapper
        .updateRevertMsgFromTrashStatus(Arrays.asList(trashMails), owner, TemailStatus.STATUS_NORMAL_0,
            TemailStatus.STATUS_TRASH_4);

    assertThat(count).isOne();
  }

  @Test
  public void listUsermailsByStatus() {
    UsermailDO userMail = new UsermailDO();
    userMail.setSessionid(SESSIONID);
    long id = this.generatePKid();
    String from = id + "from@syswin.com";
    String to = id + "to@syswin.com";
    String owner = from;
    String msgid = UUID.randomUUID().toString();
    int status = TemailStatus.STATUS_NORMAL_0;
    userMail.setId(id);
    userMail.setFrom(from);
    userMail.setTo(to);
    userMail.setOwner(owner);
    userMail.setZipMsg(msgCompressor.zip("test message".getBytes()));
    userMail.setMessage("");
    userMail.setMsgid(msgid);
    userMail.setSeqNo(0);
    userMail.setType(TemailType.TYPE_NORMAL_0);
    userMail.setStatus(status);
    userMail.setAuthor(from);
    userMail.setFilter(null);
    usermailMapper.insertUsermail(userMail);

    QueryTrashDTO queryTrashDTO = new QueryTrashDTO();
    queryTrashDTO.setOwner(from);
    queryTrashDTO.setStatus(status);
    queryTrashDTO.setPageSize(2);

    List<UsermailDO> usermailByStatus = usermailMapper.listUsermailsByStatus(queryTrashDTO);

    assertThat(usermailByStatus).isNotEmpty();
    assertThat(usermailByStatus.size()).isOne();
  }

  @Test
  public void countRevertUsermail() {
    UsermailDO userMail = new UsermailDO();
    userMail.setSessionid(SESSIONID);
    String from = "from@syswin.com";
    String to = "to@syswin.com";
    userMail.setId(123451516);
    userMail.setFrom(from);
    userMail.setTo(to);
    userMail.setOwner(from);
    userMail.setZipMsg(msgCompressor.zip("test message".getBytes()));
    userMail.setMessage("");
    userMail.setMsgid("3a2s1asd1c1a5s");
    userMail.setSeqNo(12);
    userMail.setType(TemailType.TYPE_NORMAL_0);
    userMail.setStatus(TemailStatus.STATUS_NORMAL_0);
    userMail.setAuthor(from);
    userMail.setFilter(null);
    usermailMapper.insertUsermail(userMail);
    int count = usermailMapper.countRevertUsermail(
        new RevertMailDTO(from, "3a2s1asd1c1a5s", TemailStatus.STATUS_NORMAL_0, TemailStatus.STATUS_REVERT_1));
    Assert.assertEquals(1, count);
  }

  @Test
  public void deleteUseMsgLessThanTest() {

    UsermailDO userMail1 = new UsermailDO();
    userMail1.setSessionid(SESSIONID);
    String from = "from@syswin.com";
    String to = "to@syswin.com";
    userMail1.setId(this.generatePKid());
    userMail1.setFrom(from);
    userMail1.setTo(to);
    userMail1.setOwner(from);
    userMail1.setZipMsg(msgCompressor.zip("test message".getBytes()));
    userMail1.setMsgid(msgid);
    userMail1.setSeqNo(1);
    userMail1.setType(TemailType.TYPE_NORMAL_0);
    userMail1.setStatus(TemailStatus.STATUS_NORMAL_0);
    userMail1.setMessage("");
    userMail1.setAuthor(from);
    userMail1.setFilter(null);
    usermailMapper.insertUsermail(userMail1);
    UsermailDO userMail2 = new UsermailDO();
    userMail2.setSessionid(SESSIONID);
    userMail2.setId(this.generatePKid());
    userMail2.setFrom(from);
    userMail2.setTo(to);
    userMail2.setOwner(from);
    userMail2.setZipMsg(msgCompressor.zip("test message".getBytes()));
    userMail2.setMsgid(msgid);
    userMail2.setSeqNo(2);
    userMail2.setType(TemailType.TYPE_NORMAL_0);
    userMail2.setStatus(TemailStatus.STATUS_NORMAL_0);
    userMail2.setMessage("");
    userMail2.setAuthor(from);
    userMail2.setFilter(null);
    usermailMapper.insertUsermail(userMail2);

    LocalDate createTime = LocalDate.now().plusDays(1);
    int batchNum = 1;
    int count = usermailMapper.deleteUseMsgLessThan(createTime, batchNum);

    assertThat(count).isEqualTo(1);

  }

  @Test
  public void deleteDomainTest() {
    UsermailDO mail = new UsermailDO();
    mail.setSessionid(SESSIONID);
    String from = "from@deletedomain";
    String to = "to@syswin.com";
    mail.setId(this.generatePKid());
    mail.setFrom(from);
    mail.setTo(to);
    mail.setOwner(from);
    mail.setMsgid(msgid);
    mail.setSeqNo(11);
    mail.setType(TemailType.TYPE_NORMAL_0);
    mail.setStatus(TemailStatus.STATUS_NORMAL_0);
    mail.setMessage("");
    mail.setAuthor(from);
    mail.setFilter(null);
    usermailMapper.insertUsermail(mail);
    mail.setId(this.generatePKid());
    mail.setMsgid(msgid + "002");
    usermailMapper.insertUsermail(mail);
    String domain = "deletedomain";
    int pageSize = 1;
    int count = usermailMapper.deleteDomain(domain, pageSize);

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
