package com.syswin.temail.usermail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;

@ApiModel(value = "删除会话信息参数")
public class DeleteMailBoxQueryDTO {

  @ApiModelProperty(value = "发件人(要删除会话的消息人)")
  @NotEmpty
  private String from;
  @ApiModelProperty(value = "收件人")
  @NotEmpty
  private String to;
  @ApiModelProperty(value = "是否删除全部消息")
  private boolean deleteAllMsg;

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

  public boolean isDeleteAllMsg() {
    return deleteAllMsg;
  }

  public void setDeleteAllMsg(boolean deleteAllMsg) {
    this.deleteAllMsg = deleteAllMsg;
  }

  public DeleteMailBoxQueryDTO() {
  }

  public DeleteMailBoxQueryDTO(String from, String to, boolean deleteAllMsg) {
    this.from = from;
    this.to = to;
    this.deleteAllMsg = deleteAllMsg;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("DeleteMailBoxQueryDTO{");
    sb.append("from='").append(from).append('\'');
    sb.append(", to='").append(to).append('\'');
    sb.append(", deleteAllMsg=").append(deleteAllMsg);
    sb.append('}');
    return sb.toString();
  }
}
