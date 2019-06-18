package com.syswin.temail.usermail.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(value = "废纸篓信息列表")
public class TrashMailsDTO implements Serializable {

  @NotBlank
  @ApiModelProperty(value = "消息会话所有者邮箱地址")
  private String from;

  @Valid
  @NotEmpty
  @ApiModelProperty(value = "废纸篓消息列表")
  private List<TrashMailDTO> trashMails;

  public TrashMailsDTO(@NotEmpty String from, List<TrashMailDTO> trashMails) {
    this.from = from;
    this.trashMails = trashMails;
  }
}
