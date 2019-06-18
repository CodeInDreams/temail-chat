package com.syswin.temail.usermail.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModel;
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
@ApiModel(value = "temail信息")
public class UsermailDTO implements Serializable {

  @ApiModelProperty(value = "消息ID")
  @NotEmpty
  private String msgId;

  @ApiModelProperty(value = "发送者")
  @NotEmpty
  private String from;

  @ApiModelProperty(value = "接收者")
  @NotEmpty
  private String to;

  @ApiModelProperty(value = "消息类型")
  private int type;

  @ApiModelProperty(value = "发件人加密消息")
  private String message;

  @JsonAlias("seqId")
  @ApiModelProperty(value = "会话序号")
  private long seqNo;

  public UsermailDTO(String msgId, String from, String to, int type, String message, long seqNo) {
    this.msgId = msgId;
    this.from = from;
    this.to = to;
    this.type = type;
    this.message = message;
    this.seqNo = seqNo;
  }
}
