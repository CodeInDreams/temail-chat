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
