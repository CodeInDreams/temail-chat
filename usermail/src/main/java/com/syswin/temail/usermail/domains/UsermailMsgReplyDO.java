package com.syswin.temail.usermail.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syswin.temail.usermail.core.json.TimestampJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
@ApiModel(value = "temail UsermailMsgReply")
@Alias("UserMailMsgReply")
public class UsermailMsgReplyDO implements java.io.Serializable {

  @JsonIgnore
  @ApiModelProperty(value = "PKID")
  private long id;
  @ApiModelProperty(value = "源消息ID")
  @JsonProperty("parentMsgId")
  private String parentMsgid;
  @ApiModelProperty(value = "回复消息ID")
  @JsonProperty("msgId")
  private String msgid;
  @ApiModelProperty(value = "消息发送人员邮箱")
  private String from;
  @ApiModelProperty(value = "消息接收人员邮箱")
  private String to;
  @JsonProperty("seqId")
  @ApiModelProperty(value = "会话序号")
  private long seqNo;
  @ApiModelProperty(value = "加密消息")
  @JsonProperty("message")
  private String msg;
  @ApiModelProperty(value = "消息状态")
  private int status;
  @ApiModelProperty(value = "消息类型")
  private int type;
  @JsonProperty("timestamp")
  @JsonSerialize(using = TimestampJsonSerializer.class)
  @ApiModelProperty(value = "创建时间")
  private Timestamp createTime;
  @JsonSerialize(using = TimestampJsonSerializer.class)
  @ApiModelProperty(value = "更新时间")
  private Timestamp updateTime;
  @ApiModelProperty(value = "消息所属人")
  private String owner;
  @JsonIgnore
  @ApiModelProperty(value = "SessionID")
  private String sessionid;
  private byte[] zipMsg;

  public UsermailMsgReplyDO(long id, String parentMsgId, String msgid, String from, String to, long seqNo,
      String msg, int status, int type, String owner, String sessionid) {
    this.id = id;
    this.parentMsgid = parentMsgId;
    this.msgid = msgid;
    this.from = from;
    this.to = to;
    this.seqNo = seqNo;
    this.msg = msg;
    this.status = status;
    this.type = type;
    this.owner = owner;
    this.sessionid = sessionid;
  }

  public UsermailMsgReplyDO(long id, String parentMsgid, String msgid, String from, String to, long seqNo, String msg,
      int status, int type, String owner, String sessionid, byte[] zipMsg) {
    this.id = id;
    this.parentMsgid = parentMsgid;
    this.msgid = msgid;
    this.from = from;
    this.to = to;
    this.seqNo = seqNo;
    this.msg = msg;
    this.status = status;
    this.type = type;
    this.owner = owner;
    this.sessionid = sessionid;
    this.zipMsg = zipMsg;
  }
}
