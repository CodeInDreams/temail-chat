package com.syswin.temail.usermail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@ApiModel(value = "废纸篓信息列表")
public class TrashMailsDto implements Serializable {

  @NotBlank
  @ApiModelProperty(value = "消息会话所有者邮箱地址")
  private String from;

  @Valid
  @NotEmpty
  @ApiModelProperty(value = "废纸篓消息列表")
  private List<TrashMailDto> trashMails;

  public TrashMailsDto() {
  }

  public TrashMailsDto(@NotEmpty String from, List<TrashMailDto> trashMails) {
    this.from = from;
    this.trashMails = trashMails;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public List<TrashMailDto> getTrashMails() {
    return trashMails;
  }

  public void setTrashMails(List<TrashMailDto> trashMails) {
    this.trashMails = trashMails;
  }

  @Override
  public String toString() {
    return "TrashMailsDto{" +
        "from='" + from + '\'' +
        ", trashMails=" + trashMails +
        '}';
  }
}
