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

package com.syswin.temail.usermail.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syswin.temail.usermail.core.json.TimestampJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Setter
@Getter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@ApiModel(value = "temail Usermail")
@Alias("UserMail")
public class UsermailDO implements Serializable {

  @JsonIgnore
  @ApiModelProperty(value = "PKID")
  private long id;
  @JsonProperty("msgId")
  @ApiModelProperty(value = "消息ID")
  private String msgid;

  @JsonIgnore
  @ApiModelProperty(value = "SessionID")
  private String sessionid;

  @ApiModelProperty(value = "发送者")
  private String from;

  @ApiModelProperty(value = "接收者")
  private String to;

  @ApiModelProperty(value = "消息状态")
  private int status;

  @ApiModelProperty(value = "消息类型")
  private int type;

  @ApiModelProperty(value = "消息所属人(from/to)")
  @JsonIgnore
  private String owner;

  @ApiModelProperty(value = "加密消息内容")
  private String message;

  @JsonProperty("seqId")
  @ApiModelProperty(value = "会话序号")
  private long seqNo;

  @ApiModelProperty("at列表")
  private String at;

  @ApiModelProperty("topic")
  private String topic;

  @JsonProperty("timestamp")
  @JsonSerialize(using = TimestampJsonSerializer.class)
  @ApiModelProperty(value = "创建时间")
  private Timestamp createTime;

  @ApiModelProperty(value = "最新回复消息msgId")
  private String lastReplyMsgId;

  @ApiModelProperty(value = "回复消息总数")
  private Integer replyCount;

  @JsonSerialize(using = TimestampJsonSerializer.class)
  @ApiModelProperty(value = "创建时间")
  private Timestamp updateTime;

  private byte[] zipMsg;

  @ApiModelProperty(value = "消息创建者")
  private String author;

  @ApiModelProperty(value = "消息接收者")
  private String filter;

  public UsermailDO(long id, String msgid, String sessionid, String from, String to, int status, int type, String owner,
      String message, long seqNo) {
    this.id = id;
    this.msgid = msgid;
    this.sessionid = sessionid;
    this.from = from;
    this.to = to;
    this.status = status;
    this.type = type;
    this.owner = owner;
    this.message = message;
    this.seqNo = seqNo;
  }

  public UsermailDO(long id, String msgid, String sessionid, String from, String to, int status, int type, String owner,
      String message, long seqNo, byte[] zipMsg, String author, String filter) {
    this.id = id;
    this.msgid = msgid;
    this.sessionid = sessionid;
    this.from = from;
    this.to = to;
    this.status = status;
    this.type = type;
    this.owner = owner;
    this.message = message;
    this.seqNo = seqNo;
    this.zipMsg = zipMsg;
    this.author = author;
    this.filter = filter;
  }
}
