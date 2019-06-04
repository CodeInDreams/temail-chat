package com.syswin.temail.usermail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;

@ApiModel(value = "temail回复信息阅后即焚")
public class ReplyDestoryDto implements java.io.Serializable {

  @ApiModelProperty(value = "消息ID")
  @NotEmpty
  private String msgId;
  @ApiModelProperty(value = "发送者")
  @NotEmpty
  private String from;
  @ApiModelProperty(value = "接收者")
  @NotEmpty
  private String to;

  public ReplyDestoryDto(String msgId, String from, String to) {
    this.msgId = msgId;
    this.from = from;
    this.to = to;
  }

  public ReplyDestoryDto() {
  }

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

  @Override
  public String toString() {
    return "ReplyDestoryDto{" +
        "msgId='" + msgId + '\'' +
        ", from='" + from + '\'' +
        ", to='" + to + '\'' +
        '}';
  }
}
