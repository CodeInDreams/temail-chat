package com.syswin.temail.usermail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;

@ApiModel(value = "废纸篓信息")
public class MoveTrashMailDTO implements Serializable {

  @ApiModelProperty(value = "消息会话所有者邮箱地址")
  @NotEmpty
  private String from;
  @ApiModelProperty(value = "消息会话接收者邮箱地址")
  @NotEmpty
  private String to;
  @ApiModelProperty(value = "消息ID列表")
  @NotEmpty
  private List<String> msgIds;

  public MoveTrashMailDTO() {
  }

  public MoveTrashMailDTO(String from,String to, List<String> msgIds) {
    this.from = from;
    this.to = to;
    this.msgIds = msgIds;
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

  public List<String> getMsgIds() {
    return msgIds;
  }

  public void setMsgIds(List<String> msgIds) {
    this.msgIds = msgIds;
  }

  @Override
  public String toString() {
    return "MoveTrashMailDTO{" +
        "from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", msgIds=" + msgIds +
        '}';
  }
}
