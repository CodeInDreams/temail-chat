package com.syswin.temail.usermail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Objects;
import javax.validation.constraints.NotBlank;

@ApiModel(value = "群事件信息")
public class GroupChatEventDTO {

  @NotBlank(message = "from不能为空")
  @ApiModelProperty(value = "发起人temail")
  private String from;

  @NotBlank(message = "to不能为空")
  @ApiModelProperty(value = "群成员")
  private String to;

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
    return "GroupChatEventDTO{" +
        "from='" + from + '\'' +
        ", to='" + to + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }
    GroupChatEventDTO that = (GroupChatEventDTO) o;
    return Objects.equals(this.from, that.from) &&
        Objects.equals(this.to, that.to);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.from, this.to);
  }
}
