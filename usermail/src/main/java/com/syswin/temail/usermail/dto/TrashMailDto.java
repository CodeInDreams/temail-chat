package com.syswin.temail.usermail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;

@ApiModel(value = "废纸篓信息")
public class TrashMailDto implements Serializable {

  @NotBlank
  @ApiModelProperty(value = "消息会话所有者邮箱地址")
  private String from;

  @NotBlank
  @ApiModelProperty(value = "消息会话接收者邮箱地址")
  private String to;

  @NotBlank
  @ApiModelProperty(value = "消息ID")
  private String msgId;

  public TrashMailDto(String from, String to, String msgId) {
    this.from = from;
    this.to = to;
    this.msgId = msgId;
  }

  public TrashMailDto() {
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

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  @Override
  public String toString() {
    return "TrashMailDto{" +
        "from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", msgId='" + msgId + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrashMailDto that = (TrashMailDto) o;
    return Objects.equals(from, that.from) &&
        Objects.equals(to, that.to) &&
        Objects.equals(msgId, that.msgId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to, msgId);
  }
}
