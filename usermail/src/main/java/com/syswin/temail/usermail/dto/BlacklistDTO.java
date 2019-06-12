package com.syswin.temail.usermail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;

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

  public BlacklistDTO() {
  }

  public BlacklistDTO(String temailAddress, String blackedAddress) {
    this.temailAddress = temailAddress;
    this.blackedAddress = blackedAddress;
  }

  public String getTemailAddress() {
    return temailAddress;
  }

  public void setTemailAddress(String temailAddress) {
    this.temailAddress = temailAddress;
  }

  public String getBlackedAddress() {
    return blackedAddress;
  }

  public void setBlackedAddress(String blackedAddress) {
    this.blackedAddress = blackedAddress;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("BlacklistDTO{");
    sb.append("temailAddress='").append(temailAddress).append('\'');
    sb.append(", blackedAddress='").append(blackedAddress).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
