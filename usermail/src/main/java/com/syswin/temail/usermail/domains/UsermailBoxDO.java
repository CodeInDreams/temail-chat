package com.syswin.temail.usermail.domains;

import io.swagger.annotations.ApiModelProperty;
import java.sql.Timestamp;
import org.apache.ibatis.type.Alias;

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


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getMail2() {
    return mail2;
  }

  public void setMail2(String mail2) {
    this.mail2 = mail2;
  }

  public String getSessionid() {
    return sessionid;
  }

  public void setSessionid(String sessionid) {
    this.sessionid = sessionid;
  }

  public Timestamp getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Timestamp createTime) {
    this.createTime = createTime;
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public int getArchiveStatus() {
    return archiveStatus;
  }

  public void setArchiveStatus(int archiveStatus) {
    this.archiveStatus = archiveStatus;
  }

  public UsermailBoxDO() {
  }

  public UsermailBoxDO(long id, String sessionid, String mail2, String owner) {
    this.id = id;
    this.sessionid = sessionid;
    this.mail2 = mail2;
    this.owner = owner;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("UsermailBoxDO{");
    sb.append("id=").append(id);
    sb.append(", sessionid='").append(sessionid).append('\'');
    sb.append(", mail2='").append(mail2).append('\'');
    sb.append(", owner='").append(owner).append('\'');
    sb.append(", createTime=").append(createTime);
    sb.append(", archiveStatus=").append(archiveStatus);
    sb.append('}');
    return sb.toString();
  }
}
