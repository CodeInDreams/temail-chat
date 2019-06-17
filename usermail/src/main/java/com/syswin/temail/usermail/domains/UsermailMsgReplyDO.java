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
import org.apache.ibatis.type.Alias;

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
  @ApiModelProperty(value = "创建时间")
  private Timestamp createTime;
  @ApiModelProperty(value = "更新时间")
  private Timestamp updateTime;
  @ApiModelProperty(value = "消息所属人")
  private String owner;
  @JsonIgnore
  @ApiModelProperty(value = "SessionID")
  private String sessionid;
  private byte[] zipMsg;

  public UsermailMsgReplyDO() {
  }

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

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getMsgid() {
    return msgid;
  }

  public void setMsgid(String msgid) {
    this.msgid = msgid;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public long getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(long seqNo) {
    this.seqNo = seqNo;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getParentMsgid() {
    return parentMsgid;
  }

  public void setParentMsgid(String parentMsgid) {
    this.parentMsgid = parentMsgid;
  }

  @JsonSerialize(using = TimestampJsonSerializer.class)
  public Timestamp getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Timestamp updateTime) {
    this.updateTime = updateTime;
  }

  @JsonSerialize(using = TimestampJsonSerializer.class)
  public Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Timestamp createTime) {
    this.createTime = createTime;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getSessionid() {
    return sessionid;
  }

  public void setSessionid(String sessionid) {
    this.sessionid = sessionid;
  }

  public byte[] getZipMsg() {
    return zipMsg;
  }

  public void setZipMsg(byte[] zipMsg) {
    this.zipMsg = zipMsg;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("GroupmailMsg{");
    sb.append("id=").append(id);
    sb.append(", parentMsgid='").append(parentMsgid).append('\'');
    sb.append(", msgid='").append(msgid).append('\'');
    sb.append(", from='").append(from).append('\'');
    sb.append(", to='").append(to).append('\'');
    sb.append(", seqNo=").append(seqNo);
    sb.append(", msg='").append(msg).append('\'');
    sb.append(", status=").append(status);
    sb.append(", type=").append(type);
    sb.append(", createTime=").append(createTime);
    sb.append(", updateTime=").append(updateTime);
    sb.append(", owner=").append(owner);
    sb.append(", sessionid=").append(sessionid);
    sb.append('}');
    return sb.toString();
  }

}
