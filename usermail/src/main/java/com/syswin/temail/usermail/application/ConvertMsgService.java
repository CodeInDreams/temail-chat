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

package com.syswin.temail.usermail.application;

import static com.syswin.temail.usermail.common.ParamsKey.CassandraConstant.ID;
import static com.syswin.temail.usermail.common.ParamsKey.CassandraConstant.KEYSPACE_USERMAILAGENT;
import static com.syswin.temail.usermail.common.ParamsKey.CassandraConstant.TABLE_USERMAIL;
import static com.syswin.temail.usermail.common.ParamsKey.CassandraConstant.TABLE_USERMAIL_MSG_REPLY;
import static com.syswin.temail.usermail.common.ParamsKey.SessionEventKey.MESSAGE;

import com.syswin.temail.usermail.cassandra.application.INosqlMsgTemplate;
import com.syswin.temail.usermail.cassandra.domains.MsgRow;
import com.syswin.temail.usermail.common.Constants.TemailStatus;
import com.syswin.temail.usermail.core.util.MsgCompressor;
import com.syswin.temail.usermail.domains.UsermailDO;
import com.syswin.temail.usermail.domains.UsermailMsgReplyDO;
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


  /**
   * 将在mysql中查询到的zipmsg解压后set到单聊消息记录，为null的话去Cassandra查询 并将其解压后的消息set到单聊消息记录
   *
   * @param usermails 消息不完整的单聊消息列表
   * @return 补全的单聊消息列表
   */
  public List<UsermailDO> convertMsg(List<UsermailDO> usermails) {
    if (usermails != null && !usermails.isEmpty()) {

      List idList = new ArrayList(usermails.size());

      for (int i = 0; i < usermails.size(); i++) {
        UsermailDO usermail = usermails.get(i);
        // 兼容旧版本阅后即焚逻辑
        if (usermail.getStatus() == TemailStatus.STATUS_DESTROY_AFTER_READ_2) {
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
          UsermailDO usermail = usermails.get(i);
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

  /**
   * 将在mysql中查询到的zipmsg解压后set到单聊回复消息记录，为null的话去Cassandra查询 并将其解压后set到单聊回复消息记录
   *
   * @param replyList 消息不完整的单聊回复消息列表
   * @return 补全的单聊回复消息列表
   */
  public List<UsermailMsgReplyDO> convertReplyMsg(List<UsermailMsgReplyDO> replyList) {
    if (replyList != null && !replyList.isEmpty()) {
      List idList = new ArrayList(replyList.size());

      for (int i = 0; i < replyList.size(); i++) {
        UsermailMsgReplyDO reply = replyList.get(i);
        // 兼容旧版本阅后即焚逻辑
        if (reply.getStatus() == TemailStatus.STATUS_DESTROY_AFTER_READ_2) {
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
          UsermailMsgReplyDO msgReply = replyList.get(i);
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
