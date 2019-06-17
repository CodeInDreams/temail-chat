package com.syswin.temail.usermail.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.syswin.temail.usermail.domains.UsermailDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@JsonInclude(Include.NON_NULL)
@ApiModel(value = "temail收件箱信息")
public class MailboxDTO implements java.io.Serializable {

  @ApiModelProperty(value = "会话类型")
  private int sessionType = 1;
  @ApiModelProperty(value = "会话对话mail")
  private String to;
  @ApiModelProperty(value = "会话标题")
  private String title;
  @ApiModelProperty(value = "是否置顶")
  private boolean onTop = false;
  @ApiModelProperty(value = "最后一条消息")
  private UsermailDO lastMsg;
  @ApiModelProperty(value = "归档状态")
  private int archiveStatus;

  public int getSessionType() {
    return sessionType;
  }

  public void setSessionType(int sessionType) {
    this.sessionType = sessionType;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isOnTop() {
    return onTop;
  }

  public void setOnTop(boolean onTop) {
    this.onTop = onTop;
  }

  public UsermailDO getLastMsg() {
    return lastMsg;
  }

  public void setLastMsg(UsermailDO lastMsg) {
    this.lastMsg = lastMsg;
  }

  public int getArchiveStatus() {
    return archiveStatus;
  }

  public void setArchiveStatus(int archiveStatus) {
    this.archiveStatus = archiveStatus;
  }

  public MailboxDTO() {
  }

  public MailboxDTO(int sessionType, String to, String title, boolean onTop, UsermailDO lastMsg, Integer archiveStatus) {
    this.sessionType = sessionType;
    this.to = to;
    this.title = title;
    this.onTop = onTop;
    this.lastMsg = lastMsg;
    this.archiveStatus = archiveStatus;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("MailboxDTO{");
    sb.append("sessionType=").append(sessionType);
    sb.append(", to='").append(to).append('\'');
    sb.append(", title='").append(title).append('\'');
    sb.append(", onTop=").append(onTop);
    sb.append(", lastMsg=").append(lastMsg);
    sb.append(", archiveStatus=").append(archiveStatus);
    sb.append('}');
    return sb.toString();
  }
}
