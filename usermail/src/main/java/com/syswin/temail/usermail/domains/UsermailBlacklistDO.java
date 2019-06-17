package com.syswin.temail.usermail.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import org.apache.ibatis.type.Alias;

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

  public UsermailBlacklistDO() {
  }

  public UsermailBlacklistDO(long id, String temailAddress, String blackedAddress) {
    this.id = id;
    this.temailAddress = temailAddress;
    this.blackedAddress = blackedAddress;
  }

  public UsermailBlacklistDO(String temailAddress, String blackedAddress) {
    this.temailAddress = temailAddress;
    this.blackedAddress = blackedAddress;
  }

  public UsermailBlacklistDO(long id, String temailAddress, String blackedAddress, int status, Timestamp createTime) {
    this.id = id;
    this.temailAddress = temailAddress;
    this.blackedAddress = blackedAddress;
    this.status = status;
    this.createTime = createTime;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Timestamp createTime) {
    this.createTime = createTime;
  }

  @Override
  public String toString() {
    return "UsermailBlacklistDO{" +
        "id=" + id +
        ", temailAddress='" + temailAddress + '\'' +
        ", blackedAddress='" + blackedAddress + '\'' +
        ", status=" + status +
        ", createTime=" + createTime +
        '}';
  }
}
