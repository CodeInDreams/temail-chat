package com.syswin.temail.usermail.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.syswin.temail.usermail.domains.UsermailDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
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

  public MailboxDTO(int sessionType, String to, String title, boolean onTop, UsermailDO lastMsg,
      Integer archiveStatus) {
    this.sessionType = sessionType;
    this.to = to;
    this.title = title;
    this.onTop = onTop;
    this.lastMsg = lastMsg;
    this.archiveStatus = archiveStatus;
  }
}
