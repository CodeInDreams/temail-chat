package com.syswin.temail.usermail.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UpdateArchiveDTO implements Serializable {

  @ApiModelProperty(value = "发件人(要归档会话的消息人)")
  @NotEmpty
  private String from;
  @ApiModelProperty(value = "收件人")
  @NotEmpty
  private String to;
  @ApiModelProperty(value = "归档状态")
  private int archiveStatus;

  public UpdateArchiveDTO(String from, String to, int archiveStatus) {
    this.from = from;
    this.to = to;
    this.archiveStatus = archiveStatus;
  }
}
