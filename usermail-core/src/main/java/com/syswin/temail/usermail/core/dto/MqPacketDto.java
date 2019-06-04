package com.syswin.temail.usermail.core.dto;

public class MqPacketDto {

  private String from;
  private String to;
  private String xPacketId;
  private String packet;

  public MqPacketDto(String from, String to, String xPacketId, String packet) {
    this.from = from;
    this.to = to;
    this.xPacketId = xPacketId;
    this.packet = packet;
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

  public String getxPacketId() {
    return xPacketId;
  }

  public void setxPacketId(String xPacketId) {
    this.xPacketId = xPacketId;
  }

  public String getPacket() {
    return packet;
  }

  public void setPacket(String packet) {
    this.packet = packet;
  }

  @Override
  public String toString() {
    return "MqPacketDto{" +
        "from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", xPacketId='" + xPacketId + '\'' +
        ", packet='" + packet + '\'' +
        '}';
  }
}
