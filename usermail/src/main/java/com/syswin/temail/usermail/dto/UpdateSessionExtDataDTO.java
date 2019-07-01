package com.syswin.temail.usermail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ApiModel
@Setter
@Getter
@ToString
@NoArgsConstructor
public class UpdateSessionExtDataDTO implements Serializable {

  @NotEmpty
  @ApiModelProperty(value = "会话中自己的账号")
  private String from;
  @NotEmpty
  @ApiModelProperty(value = "会话中对方的账号")
  private String to;
  @NotEmpty
  @ApiModelProperty(value = "会话中对方的头像、昵称信息")
  private String sessionExtData;

  public UpdateSessionExtDataDTO(String from, String to, String sessionExtData) {
    this.from = from;
    this.to = to;
    this.sessionExtData = sessionExtData;
  }
}
