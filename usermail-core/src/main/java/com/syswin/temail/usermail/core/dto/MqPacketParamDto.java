package com.syswin.temail.usermail.core.dto;

import com.syswin.temail.ps.common.entity.DataEncryptType;
import java.io.Serializable;
import java.util.UUID;

public class MqPacketParamDto implements Serializable {

  private String sender;
  private String senderPK;
  private String receiver;
  private String receiverPK;
  private String targetAddress;
  private String xPacketId;
  private short commandSpace;
  private short command;
  private DataEncryptType dataEncryptType;
  private String extraData;
  private Object data;

  public MqPacketParamDto(String sender, String senderPK, String receiver, String receiverPK,
      String targetAddress, String xPacketId, short commandSpace, short command, DataEncryptType dataEncryptType,
      String extraData, Object data) {
    this.sender = sender;
    this.senderPK = senderPK;
    this.receiver = receiver;
    this.receiverPK = receiverPK;
    this.targetAddress = targetAddress;
    this.xPacketId = xPacketId;
    this.commandSpace = commandSpace;
    this.command = command;
    this.dataEncryptType = dataEncryptType;
    this.extraData = extraData;
    this.data = data;
  }

  public MqPacketParamDto(String sender, String receiver, String xPacketId, short commandSpace, short command,
      DataEncryptType dataEncryptType, String extraData, Object data) {
    this.sender = sender;
    this.receiver = receiver;
    this.xPacketId = xPacketId;
    this.commandSpace = commandSpace;
    this.command = command;
    this.dataEncryptType = dataEncryptType;
    this.extraData = extraData;
    this.data = data;
  }

  public MqPacketParamDto() {
    this.xPacketId = UUID.randomUUID().toString();
  }

  public DataEncryptType getDataEncryptType() {
    return dataEncryptType;
  }

  public void setDataEncryptType(DataEncryptType dataEncryptType) {
    this.dataEncryptType = dataEncryptType;
  }

  public String getSender() {
    return sender;
  }

  public void setSender(String sender) {
    this.sender = sender;
  }

  public String getSenderPK() {
    return senderPK;
  }

  public void setSenderPK(String senderPK) {
    this.senderPK = senderPK;
  }

  public String getReceiver() {
    return receiver;
  }

  public void setReceiver(String receiver) {
    this.receiver = receiver;
  }

  public String getReceiverPK() {
    return receiverPK;
  }

  public void setReceiverPK(String receiverPK) {
    this.receiverPK = receiverPK;
  }

  public String getTargetAddress() {
    return targetAddress;
  }

  public void setTargetAddress(String targetAddress) {
    this.targetAddress = targetAddress;
  }

  public String getxPacketId() {
    return xPacketId;
  }

  public void setxPacketId(String xPacketId) {
    this.xPacketId = xPacketId;
  }

  public short getCommandSpace() {
    return commandSpace;
  }

  public void setCommandSpace(short commandSpace) {
    this.commandSpace = commandSpace;
  }

  public short getCommand() {
    return command;
  }

  public void setCommand(short command) {
    this.command = command;
  }

  public String getExtraData() {
    return extraData;
  }

  public void setExtraData(String extraData) {
    this.extraData = extraData;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  @Override
  public String toString() {
    return "MqPacketParamDto{" +
        "sender='" + sender + '\'' +
        ", senderPK='" + senderPK + '\'' +
        ", receiver='" + receiver + '\'' +
        ", receiverPK='" + receiverPK + '\'' +
        ", targetAddress='" + targetAddress + '\'' +
        ", xPacketId='" + xPacketId + '\'' +
        ", commandSpace=" + commandSpace +
        ", command=" + command +
        ", dataEncryptType=" + dataEncryptType +
        ", extraData='" + extraData + '\'' +
        ", data=" + data +
        '}';
  }
}
