package com.syswin.temail.usermail.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;

public class UsermailMsgReplyDto implements Serializable {

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

  public UsermailMsgReplyDto(String msgId, String from, String to, int type, String mssage,
      String parentMsgId, int attachmentSize,List<String> msgIds,int storeType) {
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

  public UsermailMsgReplyDto() {
  }


  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getMsgData() {
    return msgData;
  }

  public void setMsgData(String msgData) {
    this.msgData = msgData;
  }

  public String getParentMsgId() {
    return parentMsgId;
  }

  public void setParentMsgId(String parentMsgId) {
    this.parentMsgId = parentMsgId;
  }

  public String getMsgId() {
    return msgId;
  }

  public void setMsgId(String msgId) {
    this.msgId = msgId;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public int getAttachmentSize() {
    return attachmentSize;
  }

  public void setAttachmentSize(int attachmentSize) {
    this.attachmentSize = attachmentSize;
  }

  public List<String> getMsgIds() {
    return msgIds;
  }

  public void setMsgIds(List<String> msgIds) {
    this.msgIds = msgIds;
  }

  public int getStoreType() {
    return storeType;
  }

  public void setStoreType(int storeType) {
    this.storeType = storeType;
  }

  @Override
  public String toString() {
    return "UsermailMsgReplyDto{" +
        "from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", msgData='" + msgData + '\'' +
        ", parentMsgId='" + parentMsgId + '\'' +
        ", msgId='" + msgId + '\'' +
        ", type=" + type +
        ", attachmentSize=" + attachmentSize +
        ", storeType=" + storeType +
        ", msgIds=" + msgIds +
        '}';
  }
}
