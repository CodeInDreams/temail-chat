package com.syswin.temail.usermail.dto;

import com.syswin.temail.usermail.core.dto.Meta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(value = "创建temail时消息信息")
public class CreateUsermailDTO implements Serializable {

  @NotBlank(message = "msgId不能为空")
  @ApiModelProperty(value = "消息ID")
  private String msgId;

  @NotBlank(message = "from不能为空")
  @ApiModelProperty(value = "发送者")
  private String from;

  @NotBlank(message = "to不能为空")
  @ApiModelProperty(value = "接收者")
  private String to;

  @ApiModelProperty(value = "消息类型")
  private int type;

  @ApiModelProperty(value = "(int) 1 存收件人收件箱 2 存发件人收件箱")
  private int storeType;

  @ApiModelProperty(value = "加密消息")
  private String msgData;

  @ApiModelProperty(value = "元信息")
  private Meta meta;

  @ApiModelProperty(value = "附件字节大小")
  private int attachmentSize;

  private String author = "";

  private List<String> filter;

  public CreateUsermailDTO(String msgId, String from, String to, int type, int storeType, String msgData,
      int attachmentSize) {
    this.msgId = msgId;
    this.from = from;
    this.to = to;
    this.type = type;
    this.storeType = storeType;
    this.msgData = msgData;
    this.attachmentSize = attachmentSize;
  }
}
