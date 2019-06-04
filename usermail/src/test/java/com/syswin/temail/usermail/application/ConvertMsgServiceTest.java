package com.syswin.temail.usermail.application;

import static com.syswin.temail.usermail.common.Contants.CassandraConstant.ID;
import static com.syswin.temail.usermail.common.Contants.CassandraConstant.KEYSPACE_USERMAILAGENT;
import static com.syswin.temail.usermail.common.Contants.CassandraConstant.TABLE_USERMAIL;
import static com.syswin.temail.usermail.common.Contants.CassandraConstant.TABLE_USERMAIL_MSG_REPLY;
import static com.syswin.temail.usermail.common.Contants.SessionEventKey.MESSAGE;

import com.syswin.temail.usermail.cassandra.application.INosqlMsgTemplate;
import com.syswin.temail.usermail.common.Contants.TemailStatus;
import com.syswin.temail.usermail.common.Contants.TemailType;
import com.syswin.temail.usermail.core.util.MsgCompressor;
import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.domains.UsermailMsgReply;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConvertMsgServiceTest {

  private INosqlMsgTemplate nosqlMsgTemplate = Mockito.mock(INosqlMsgTemplate.class);

  private MsgCompressor msgCompressor = new MsgCompressor();

  private ConvertMsgService convertMsgService = new ConvertMsgService(msgCompressor, nosqlMsgTemplate);

  @Test
  public void convertMsgTest() {
    List<Map<String, Object>> nosqlColumnList = new ArrayList<>();
    List idList = new ArrayList();
    List<Usermail> usermails = new ArrayList<>();
    for(int i=0;i<10;i++){
      Usermail userMail = new Usermail();
      userMail.setSessionid("sessionId");
      String from = "from@syswin.com";
      String to = "to@syswin.com";
      userMail.setId(i);
      userMail.setFrom(from);
      userMail.setTo(to);
      userMail.setOwner(from);
      if( i%2 == 0 ) {
        userMail.setZipMsg(msgCompressor.zip("test message".getBytes()));
      } else {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ID,userMail.getId());
        map.put(MESSAGE,ByteBuffer.wrap(msgCompressor.zip("test message".getBytes())));
        nosqlColumnList.add(map);
        idList.add(userMail.getId());
      }
      userMail.setMsgid(UUID.randomUUID().toString());
      userMail.setSeqNo(11);
      userMail.setType(TemailType.TYPE_NORMAL_0);
      userMail.setStatus(TemailStatus.STATUS_NORMAL_0);
      userMail.setMessage("");
      userMail.setAuthor(from);
      userMail.setFilter(null);
      usermails.add(userMail);
    }

    Mockito.when(nosqlMsgTemplate.listMsg(KEYSPACE_USERMAILAGENT, TABLE_USERMAIL, idList.toArray(), ID, MESSAGE)).thenReturn(nosqlColumnList);
    List<Usermail> convertMsg = convertMsgService.convertMsg(usermails);

    convertMsg.forEach(usermail -> {
      if (StringUtils.isNotEmpty(usermail.getMsgid())) {
        Assertions.assertThat(usermail.getMessage()).isNotEmpty();
      }
    });

  }

  @Test
  public void convertReplyMsg() {

    List<Map<String, Object>> nosqlColumnList = new ArrayList<>();
    List idList = new ArrayList();
    List<UsermailMsgReply> msgReplys = new ArrayList<>();
    for(int i=0;i<10;i++){
      UsermailMsgReply usermailMsgReply = new UsermailMsgReply();
      usermailMsgReply.setSessionid("sessionId");
      String from = "from@syswin.com";
      String to = "to@syswin.com";
      usermailMsgReply.setId(i);
      usermailMsgReply.setFrom(from);
      usermailMsgReply.setTo(to);
      usermailMsgReply.setOwner(from);
      if( i%2 == 0 ) {
        usermailMsgReply.setZipMsg(msgCompressor.zip("test message".getBytes()));
      } else {
        HashMap<String, Object> map = new HashMap<>();
        map.put(ID,usermailMsgReply.getId());
        map.put(MESSAGE,ByteBuffer.wrap(msgCompressor.zip("test message".getBytes())));
        nosqlColumnList.add(map);
        idList.add(usermailMsgReply.getId());
      }
      usermailMsgReply.setMsgid(UUID.randomUUID().toString());
      usermailMsgReply.setSeqNo(11);
      usermailMsgReply.setType(TemailType.TYPE_NORMAL_0);
      usermailMsgReply.setStatus(TemailStatus.STATUS_NORMAL_0);
      usermailMsgReply.setMsg("");
      usermailMsgReply.setParentMsgid(UUID.randomUUID().toString());
      msgReplys.add(usermailMsgReply);
    }
    Mockito.when(nosqlMsgTemplate.listMsg(KEYSPACE_USERMAILAGENT, TABLE_USERMAIL_MSG_REPLY, idList.toArray(), ID, MESSAGE)).thenReturn(nosqlColumnList);
    List<UsermailMsgReply> usermailMsgReplies = convertMsgService.convertReplyMsg(msgReplys);

    usermailMsgReplies.forEach(usermailMsgReply -> {
      Assertions.assertThat(usermailMsgReply.getMsg()).isNotEmpty();
    });
  }
}