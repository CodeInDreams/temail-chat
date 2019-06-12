package com.syswin.temail.usermail.application;

import static com.syswin.temail.usermail.common.Constants.CassandraConstant.ID;
import static com.syswin.temail.usermail.common.Constants.CassandraConstant.KEYSPACE_USERMAILAGENT;
import static com.syswin.temail.usermail.common.Constants.CassandraConstant.TABLE_USERMAIL;
import static com.syswin.temail.usermail.common.Constants.CassandraConstant.TABLE_USERMAIL_MSG_REPLY;
import static com.syswin.temail.usermail.common.Constants.SessionEventKey.MESSAGE;

import com.syswin.temail.usermail.cassandra.application.INosqlMsgTemplate;
import com.syswin.temail.usermail.cassandra.domains.MsgRow;
import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.core.util.MsgCompressor;
import com.syswin.temail.usermail.domains.Usermail;
import com.syswin.temail.usermail.domains.UsermailMsgReply;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ConvertMsgService {

  private final MsgCompressor msgCompressor;
  private final INosqlMsgTemplate nosqlMsgTemplate;

  public ConvertMsgService(MsgCompressor msgCompressor,
      INosqlMsgTemplate nosqlMsgTemplate) {
    this.msgCompressor = msgCompressor;
    this.nosqlMsgTemplate = nosqlMsgTemplate;
  }


  public List<Usermail> convertMsg(List<Usermail> usermails) {
    if (usermails != null && !usermails.isEmpty()) {

      List idList = new ArrayList(usermails.size());

      for (int i = 0; i < usermails.size(); i++) {
        Usermail usermail = usermails.get(i);
        //兼容旧版本阅后即焚逻辑
        if (usermail.getStatus() == TemailStatus.STATUS_DESTORY_AFTER_READ_2) {
          usermail.setMessage("");
          continue;
        }
        byte[] msg = usermail.getZipMsg();
        if (msg != null && msg.length > 0) {
          usermail.setMessage(msgCompressor.unzipEncode(msg));
          usermail.setZipMsg(null);
        } else {
          idList.add(usermail.getId());
        }
      }

      if (!idList.isEmpty()) {
        List<Map<String, Object>> nosqlColumnList = nosqlMsgTemplate
            .listMsg(KEYSPACE_USERMAILAGENT, TABLE_USERMAIL, idList.toArray(), ID, MESSAGE);
        Map<Long, MsgRow> msgRowMap = new HashMap<>();
        nosqlColumnList.forEach(entry -> {
          Long id = (Long) entry.get(ID);
          ByteBuffer message = (ByteBuffer) entry.get(MESSAGE);
          msgRowMap.put(id, new MsgRow(id, message.array()));
        });
        for (int i = 0; i < usermails.size(); i++) {
          Usermail usermail = usermails.get(i);
          MsgRow msgRow = msgRowMap.get(usermail.getId());
          if (msgRow == null) {
            continue;
          }
          usermail.setMessage(msgCompressor.unzipEncode(msgRow.getMessage()));
        }
      }
    }
    return usermails;
  }

  public List<UsermailMsgReply> convertReplyMsg(List<UsermailMsgReply> replyList) {
    if (replyList != null && !replyList.isEmpty()) {
      List idList = new ArrayList(replyList.size());

      for (int i = 0; i < replyList.size(); i++) {
        UsermailMsgReply reply = replyList.get(i);
        //兼容旧版本阅后即焚逻辑
        if (reply.getStatus() == TemailStatus.STATUS_DESTORY_AFTER_READ_2) {
          reply.setMsg("");
          continue;
        }
        if (null != reply.getZipMsg() && reply.getZipMsg().length > 0) {
          reply.setMsg(msgCompressor.unzipEncode(reply.getZipMsg()));
          reply.setZipMsg(null);
        } else {
          idList.add(reply.getId());
        }
      }
      if (!idList.isEmpty()) {
        List<Map<String, Object>> nosqlColumnList = nosqlMsgTemplate
            .listMsg(KEYSPACE_USERMAILAGENT, TABLE_USERMAIL_MSG_REPLY, idList.toArray(), ID, MESSAGE);
        Map<Long, MsgRow> msgRowMap = new HashMap<>();
        nosqlColumnList.forEach(entry -> {
          Long id = (Long) entry.get(ID);
          ByteBuffer message = (ByteBuffer) entry.get(MESSAGE);
          msgRowMap.put(id, new MsgRow(id, message.array()));
        });

        for (int i = 0; i < replyList.size(); i++) {
          UsermailMsgReply msgReply = replyList.get(i);
          MsgRow msgRow = msgRowMap.get(msgReply.getId());
          if (msgRow == null) {
            continue;
          }
          msgReply.setMsg(msgCompressor.unzipEncode(msgRow.getMessage()));
        }
      }

    }
    return replyList;
  }
}
