/*
 * MIT License
 *
 * Copyright (c) 2019 Syswin
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
