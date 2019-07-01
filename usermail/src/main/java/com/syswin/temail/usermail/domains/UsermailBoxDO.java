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
  @ApiModelProperty(value = "会话中对方的昵称和头像")
  private String sessionExtData;

  public static UsermailBoxDO Create(long id, String sessionid, String mail2, String owner, String sessionExtData) {
    return new UsermailBoxDO(id, sessionid, mail2, owner, sessionExtData);
  }

  public UsermailBoxDO(long id, String sessionid, String mail2, String owner, String sessionExtData) {
    this.id = id;
    this.sessionid = sessionid;
    this.mail2 = mail2;
    this.owner = owner;
    this.sessionExtData = sessionExtData;
  }

  public UsermailBoxDO(long id, String sessionid, String mail2, String owner) {
    this.id = id;
    this.sessionid = sessionid;
    this.mail2 = mail2;
    this.owner = owner;
  }
}
