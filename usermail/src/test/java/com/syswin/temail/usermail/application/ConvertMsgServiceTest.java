package com.syswin.temail.usermail.application;

import static com.syswin.temail.usermail.common.ParamsKey.CassandraConstant.ID;
import static com.syswin.temail.usermail.common.ParamsKey.CassandraConstant.KEYSPACE_USERMAILAGENT;
import static com.syswin.temail.usermail.common.ParamsKey.CassandraConstant.TABLE_USERMAIL;
import static com.syswin.temail.usermail.common.ParamsKey.CassandraConstant.TABLE_USERMAIL_MSG_REPLY;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.MESSAGE;

import com.syswin.temail.usermail.cassandra.application.INosqlMsgTemplate;
import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.common.Constants.TemailType;
import com.syswin.temail.usermail.core.util.MsgCompressor;
import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.domains.UsermailMsgReplyDO;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

public class ConvertMsgServiceTest {

  private INosqlMsgTemplate nosqlMsgTemplate = Mockito.mock(INosqlMsgTemplate.class);

  private MsgCompressor msgCompressor = new MsgCompressor();

  private ConvertMsgService convertMsgService = new ConvertMsgService(msgCompressor, nosqlMsgTemplate);

  @Test
  public void convertMsgTest() {
    List<Map<String, Object>> nosqlColumnList = new ArrayList<>();
    List<Long> idList = new ArrayList<>();
    List<UsermailDO> usermails = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      UsermailDO userMail = new UsermailDO();
      userMail.setSessionid("sessionId");
      String from = "from@syswin.com";
      String to = "to@syswin.com";
      userMail.setId(i);
      userMail.setFrom(from);
      userMail.setTo(to);
      userMail.setOwner(from);
      if (i % 2 == 0) {
        userMail.setZipMsg(msgCompressor.zip("test message".getBytes()));
      } else {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ID, userMail.getId());
        map.put(MESSAGE, ByteBuffer.wrap(msgCompressor.zip("test message".getBytes())));
        nosqlColumnList.add(map);
        idList.add(userMail.getId());
      }
      userMail.setMsgid(UUID.randomUUID().toString());
      userMail.setSeqNo(11);
      userMail.setType(TemailType.TYPE_NORMAL_0);
      userMail.setStatus(
          (i % 2 == 0 && i % 4 == 0) ? TemailStatus.STATUS_DESTROY_AFTER_READ_2 : TemailStatus.STATUS_NORMAL_0);
      userMail.setMessage("");
      userMail.setAuthor(from);
      userMail.setFilter(null);
      usermails.add(userMail);
    }

    Mockito.when(nosqlMsgTemplate.listMsg(KEYSPACE_USERMAILAGENT, TABLE_USERMAIL, idList.toArray(), ID, MESSAGE))
        .thenReturn(nosqlColumnList);
    List<UsermailDO> convertMsg = convertMsgService.convertMsg(usermails);

    convertMsg.forEach(usermail -> {
      if (usermail.getStatus() == TemailStatus.STATUS_NORMAL_0) {
        Assertions.assertThat(usermail.getMessage()).isNotEmpty();
      } else if (usermail.getStatus() == TemailStatus.STATUS_DESTROY_AFTER_READ_2) {
        Assertions.assertThat(usermail.getMessage()).isEmpty();
      }
    });

  }

  @Test
  public void convertReplyMsg() {

    List<Map<String, Object>> nosqlColumnList = new ArrayList<>();
    List<Long> idList = new ArrayList<>();
    List<UsermailMsgReplyDO> msgReplys = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      UsermailMsgReplyDO usermailMsgReply = new UsermailMsgReplyDO();
      usermailMsgReply.setSessionid("sessionId");
      String from = "from@syswin.com";
      String to = "to@syswin.com";
      usermailMsgReply.setId(i);
      usermailMsgReply.setFrom(from);
      usermailMsgReply.setTo(to);
      usermailMsgReply.setOwner(from);
      if (i % 2 == 0) {
        usermailMsgReply.setZipMsg(msgCompressor.zip("test message".getBytes()));
      } else {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ID, usermailMsgReply.getId());
        map.put(MESSAGE, ByteBuffer.wrap(msgCompressor.zip("test message".getBytes())));
        nosqlColumnList.add(map);
        idList.add(usermailMsgReply.getId());
      }
      usermailMsgReply.setMsgid(UUID.randomUUID().toString());
      usermailMsgReply.setSeqNo(11);
      usermailMsgReply.setType(TemailType.TYPE_NORMAL_0);
      usermailMsgReply.setStatus(
          (i % 2 == 0 && i % 4 == 0) ? TemailStatus.STATUS_DESTROY_AFTER_READ_2 : TemailStatus.STATUS_NORMAL_0);
      usermailMsgReply.setMsg("");
      usermailMsgReply.setParentMsgid(UUID.randomUUID().toString());
      msgReplys.add(usermailMsgReply);
    }
    Mockito
        .when(nosqlMsgTemplate.listMsg(KEYSPACE_USERMAILAGENT, TABLE_USERMAIL_MSG_REPLY, idList.toArray(), ID, MESSAGE))
        .thenReturn(nosqlColumnList);
    List<UsermailMsgReplyDO> usermailMsgReplies = convertMsgService.convertReplyMsg(msgReplys);

    usermailMsgReplies.forEach(usermailMsgReply -> {
      if (usermailMsgReply.getStatus() == TemailStatus.STATUS_NORMAL_0) {
        Assertions.assertThat(usermailMsgReply.getMsg()).isNotEmpty();
      } else if (usermailMsgReply.getStatus() == TemailStatus.STATUS_DESTROY_AFTER_READ_2) {
        Assertions.assertThat(usermailMsgReply.getMsg()).isEmpty();
      }
    });
  }
}