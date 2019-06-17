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
import org.apache.ibatis.type.Alias;

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
  @ApiModelProperty(value = "创建时间")
  private Timestamp createTime;

  @ApiModelProperty(value = "最新回复消息msgId")
  private String lastReplyMsgId;

  @ApiModelProperty(value = "回复消息总数")
  private Integer replyCount;

  @ApiModelProperty(value = "创建时间")
  private Timestamp updateTime;

  private byte[] zipMsg;

  @ApiModelProperty(value = "消息创建者")
  private String author;

  @ApiModelProperty(value = "消息接收者")
  private String filter;

  public String getLastReplyMsgId() {
    return lastReplyMsgId;
  }

  public void setLastReplyMsgId(String lastReplyMsgId) {
    this.lastReplyMsgId = lastReplyMsgId;
  }

  public Integer getReplyCount() {
    return replyCount;
  }

  public void setReplyCount(Integer replyCount) {
    this.replyCount = replyCount;
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

  @JsonSerialize(using = TimestampJsonSerializer.class)
  public Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Timestamp createTime) {
    this.createTime = createTime;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public long getSeqNo() {
    return seqNo;
  }

  public void setSeqNo(long seqNo) {
    this.seqNo = seqNo;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getSessionid() {
    return sessionid;
  }

  public void setSessionid(String sessionid) {
    this.sessionid = sessionid;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getAt() {
    return at;
  }

  public void setAt(String at) {
    this.at = at;
  }

  public String getTopic() {
    return topic;
  }

  public void setTopic(String topic) {
    this.topic = topic;
  }

  public Timestamp getUpdateTime() {
    return updateTime;
  }

  @JsonSerialize(using = TimestampJsonSerializer.class)
  public void setUpdateTime(Timestamp updateTime) {
    this.updateTime = updateTime;
  }

  public byte[] getZipMsg() {
    return zipMsg;
  }

  public void setZipMsg(byte[] zipMsg) {
    this.zipMsg = zipMsg;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getFilter() {
    return filter;
  }

  public void setFilter(String filter) {
    this.filter = filter;
  }

  public UsermailDO() {
  }

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
    this. author = author;
    this.filter = filter;
  }

  @Override
  public String toString() {
    return "UsermailDO{" +
        "id=" + id +
        ", msgid='" + msgid + '\'' +
        ", sessionid='" + sessionid + '\'' +
        ", from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", status=" + status +
        ", type=" + type +
        ", owner='" + owner + '\'' +
        ", message='" + message + '\'' +
        ", seqNo=" + seqNo +
        ", at='" + at + '\'' +
        ", topic='" + topic + '\'' +
        ", createTime=" + createTime +
        ", updateTime=" + updateTime +
        ", lastReplyMsgId='" + lastReplyMsgId + '\'' +
        ", replyCount=" + replyCount +
        '}';
  }
}
