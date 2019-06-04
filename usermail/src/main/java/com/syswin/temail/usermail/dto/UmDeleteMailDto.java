package com.syswin.temail.usermail.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;

public class UmDeleteMailDto implements Serializable {
    @ApiModelProperty(value = "消息ID列表")
    @NotEmpty
    private List<String> msgIds;

    @ApiModelProperty(value = "发送者")
    @NotEmpty
    private String from;
    @ApiModelProperty(value = "接收者")
    @NotEmpty
    private String to;

    @ApiModelProperty(value = "消息类型")
    private int type;

    @ApiModelProperty(value = "发件人加密消息")
    private String message;

    @JsonAlias("seqId")
    @ApiModelProperty(value = "会话序号")
    private long seqNo;

    public List<String> getMsgIds() {
        return msgIds;
    }

    public void setMsgIds(List<String> msgIds) {
        this.msgIds = msgIds;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(long seqNo) {
        this.seqNo = seqNo;
    }

    public UmDeleteMailDto(List<String> msgIds, String from, String to, int type, String message, long seqNo) {
        this.msgIds = msgIds;
        this.from = from;
        this.to = to;
        this.type = type;
        this.message = message;
        this.seqNo = seqNo;
    }

    public UmDeleteMailDto() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UsermailDto{");
        sb.append("msgId='").append(msgIds).append('\'');
        sb.append(", from='").append(from).append('\'');
        sb.append(", to='").append(to).append('\'');
        sb.append(", type=").append(type);
        sb.append(", message='").append(message).append('\'');
        sb.append(", seqNo=").append(seqNo);
        sb.append('}');
        return sb.toString();
    }
}