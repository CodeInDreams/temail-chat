package com.syswin.temail.usermail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@ApiModel(value = "黑名单关系")
public class BlacklistDTO implements java.io.Serializable {

  @ApiModelProperty(value = "黑名单发起者temail地址")
  @JsonProperty(value = "from")
  @NotEmpty
  private String temailAddress;
  @ApiModelProperty(value = "被拉黑者temail地址")
  @JsonProperty(value = "to")
  @NotEmpty
  private String blackedAddress;

  public BlacklistDTO(String temailAddress, String blackedAddress) {
    this.temailAddress = temailAddress;
    this.blackedAddress = blackedAddress;
  }
}
