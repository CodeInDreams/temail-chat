package com.syswin.temail.usermail.domains;

import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Alias("UsermailBox")
public class UsermailBoxDO implements java.io.Serializable {

  @ApiModelProperty(value = "PKID")
  private long id;
  @ApiModelProperty(value = "SessionID")
  private String sessionid;
  @ApiModelProperty(value = "一位聊天者")
  private String mail2;
  @ApiModelProperty(value = "会话拥有者")
  private String owner;
  @ApiModelProperty(value = "创建时间")
  private Timestamp createTime;
  @ApiModelProperty(value = "归档状态")
  private int archiveStatus;

  public UsermailBoxDO(long id, String sessionid, String mail2, String owner) {
    this.id = id;
    this.sessionid = sessionid;
    this.mail2 = mail2;
    this.owner = owner;
  }
}
