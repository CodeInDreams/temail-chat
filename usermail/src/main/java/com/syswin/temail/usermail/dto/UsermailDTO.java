package com.syswin.temail.usermail.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;


@ApiModel(value = "temail信息")
public class UsermailDTO implements Serializable {

  @ApiModelProperty(value = "消息ID")
  @NotEmpty
  private String msgId;

  @ApiModelProperty(value = "发送者")
  @NotEmpty
  private String from;
  @ApiModelProperty(value = "接收者")
  @NotEmpty
  private String to;

  @ApiModelProperty(value = "消息类型")
  private int type;

  @ApiModelProperty(value = "发件人加密消息")
  private String message;

  @JsonAlias("seqId")
  @ApiModelProperty(value = "会话序号")
  private long seqNo;


  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
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

  @JsonAlias("seqId")
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

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public UsermailDTO() {
  }

  public UsermailDTO(String msgId, String from, String to, int type, String message, long seqNo) {
    this.msgId = msgId;
    this.from = from;
    this.to = to;
    this.type = type;
    this.message = message;
    this.seqNo = seqNo;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("UsermailDTO{");
    sb.append("msgId='").append(msgId).append('\'');
    sb.append(", from='").append(from).append('\'');
    sb.append(", to='").append(to).append('\'');
    sb.append(", type=").append(type);
    sb.append(", message='").append(message).append('\'');
    sb.append(", seqNo=").append(seqNo);
    sb.append('}');
    return sb.toString();
  }
}
