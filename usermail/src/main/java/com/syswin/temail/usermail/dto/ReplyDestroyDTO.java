package com.syswin.temail.usermail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(value = "temail回复信息阅后即焚")
public class ReplyDestoryDTO implements java.io.Serializable {

  @ApiModelProperty(value = "消息ID")
  @NotEmpty
  private String msgId;
  @ApiModelProperty(value = "发送者")
  @NotEmpty
  private String from;
  @ApiModelProperty(value = "接收者")
  @NotEmpty
  private String to;

  public ReplyDestoryDTO(String msgId, String from, String to) {
    this.msgId = msgId;
    this.from = from;
    this.to = to;
  }
}
