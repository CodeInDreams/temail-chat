package com.syswin.temail.usermail.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UmDeleteMailDTO implements Serializable {

  @ApiModelProperty(value = "消息ID列表")
  @NotEmpty
  private List<String> msgIds;

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

  public UmDeleteMailDTO(List<String> msgIds, String from, String to, int type, String message, long seqNo) {
    this.msgIds = msgIds;
    this.from = from;
    this.to = to;
    this.type = type;
    this.message = message;
    this.seqNo = seqNo;
  }
}