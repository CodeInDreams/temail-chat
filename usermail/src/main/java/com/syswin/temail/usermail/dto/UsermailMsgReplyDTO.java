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

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UsermailMsgReplyDTO implements Serializable {

  @ApiModelProperty(value = "发送者")
  @NotEmpty
  private String from;
  @ApiModelProperty(value = "接收者")
  @NotEmpty
  private String to;
  @ApiModelProperty(value = "加密消息")
  private String msgData;
  @ApiModelProperty(value = "源消息ID")
  @NotEmpty
  private String parentMsgId;
  @ApiModelProperty(value = "加密消息ID")
  @NotEmpty
  private String msgId;
  @ApiModelProperty(value = "消息类型")
  private int type;
  @ApiModelProperty(value = "附件大小")
  private int attachmentSize;
  @ApiModelProperty(value = "msgIds")
  private List<String> msgIds;
  @ApiModelProperty(value = "(int) 1 存收件人收件箱 2 存发件人收件箱")
  private int storeType;

  public UsermailMsgReplyDTO(String msgId, String from, String to, int type, String mssage,
      String parentMsgId, int attachmentSize, List<String> msgIds, int storeType) {
    this.msgId = msgId;
    this.from = from;
    this.to = to;
    this.type = type;
    this.msgData = mssage;
    this.parentMsgId = parentMsgId;
    this.attachmentSize = attachmentSize;
    this.msgIds = msgIds;
    this.storeType = storeType;
  }
}
