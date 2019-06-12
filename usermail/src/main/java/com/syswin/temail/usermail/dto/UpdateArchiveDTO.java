package com.syswin.temail.usermail.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;

public class UpdateArchiveDTO implements Serializable {

  @ApiModelProperty(value = "发件人(要归档会话的消息人)")
  @NotEmpty
  private String from;
  @ApiModelProperty(value = "收件人")
  @NotEmpty
  private String to;
  @ApiModelProperty(value = "归档状态")
  private int archiveStatus;


  public UpdateArchiveDTO() {
  }

  public UpdateArchiveDTO(String from, String to, int archiveStatus) {
    this.from = from;
    this.to = to;
    this.archiveStatus = archiveStatus;
  }

  public int getArchiveStatus() {
    return archiveStatus;
  }

  public void setArchiveStatus(int archiveStatus) {
    this.archiveStatus = archiveStatus;
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
    return "UpdateArchiveDTO{" +
        "from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", archiveStatus='" + archiveStatus + '\'' +
        '}';
  }
}
