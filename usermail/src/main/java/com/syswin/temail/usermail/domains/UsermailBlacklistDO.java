package com.syswin.temail.usermail.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Setter
@Getter
@ToString
@NoArgsConstructor
@ApiModel(value = "黑名单")
@JsonInclude(Include.NON_NULL)
@Alias("UsermailBlacklist")
public class UsermailBlacklistDO implements java.io.Serializable {

  @JsonIgnore
  private long id;
  @ApiModelProperty(value = "黑名单发起者Temail")
  private String temailAddress;
  @ApiModelProperty(value = "黑名单用户Temail")
  private String blackedAddress;
  @ApiModelProperty(value = "状态")
  private int status;
  @JsonIgnore
  private Timestamp createTime;

  public UsermailBlacklistDO(long id, String temailAddress, String blackedAddress) {
    this.id = id;
    this.temailAddress = temailAddress;
    this.blackedAddress = blackedAddress;
  }

  public UsermailBlacklistDO(String temailAddress, String blackedAddress) {
    this.temailAddress = temailAddress;
    this.blackedAddress = blackedAddress;
  }
}
