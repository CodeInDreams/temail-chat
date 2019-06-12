package com.syswin.temail.usermail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@ApiModel(value = "废纸篓信息列表")
public class TrashMailsDTO implements Serializable {

  @NotBlank
  @ApiModelProperty(value = "消息会话所有者邮箱地址")
  private String from;

  @Valid
  @NotEmpty
  @ApiModelProperty(value = "废纸篓消息列表")
  private List<TrashMailDTO> trashMails;

  public TrashMailsDTO() {
  }

  public TrashMailsDTO(@NotEmpty String from, List<TrashMailDTO> trashMails) {
    this.from = from;
    this.trashMails = trashMails;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public List<TrashMailDTO> getTrashMails() {
    return trashMails;
  }

  public void setTrashMails(List<TrashMailDTO> trashMails) {
    this.trashMails = trashMails;
  }

  @Override
  public String toString() {
    return "TrashMailsDTO{" +
        "from='" + from + '\'' +
        ", trashMails=" + trashMails +
        '}';
  }
}
