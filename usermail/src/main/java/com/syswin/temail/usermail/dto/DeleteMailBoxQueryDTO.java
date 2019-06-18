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

  public DeleteMailBoxQueryDTO(String from, String to, boolean deleteAllMsg) {
    this.from = from;
    this.to = to;
    this.deleteAllMsg = deleteAllMsg;
  }
}
