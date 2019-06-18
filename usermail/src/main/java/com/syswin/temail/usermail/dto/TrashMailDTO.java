package com.syswin.temail.usermail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
@ApiModel(value = "废纸篓信息")
public class TrashMailDTO implements Serializable {

  @NotBlank
  @ApiModelProperty(value = "消息会话所有者邮箱地址")
  private String from;

  @NotBlank
  @ApiModelProperty(value = "消息会话接收者邮箱地址")
  private String to;

  @NotBlank
  @ApiModelProperty(value = "消息ID")
  private String msgId;

  public TrashMailDTO(String from, String to, String msgId) {
    this.from = from;
    this.to = to;
    this.msgId = msgId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TrashMailDTO that = (TrashMailDTO) o;
    return Objects.equals(from, that.from) &&
        Objects.equals(to, that.to) &&
        Objects.equals(msgId, that.msgId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(from, to, msgId);
  }
}
