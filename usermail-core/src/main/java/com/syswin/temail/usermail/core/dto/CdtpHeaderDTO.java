package com.syswin.temail.usermail.core.dto;

import java.io.Serializable;
import java.util.Objects;

public class CdtpHeaderDTO implements Serializable {

  private String xPacketId;
  private String cdtpHeader;

  public CdtpHeaderDTO(String cdtpHeader, String xPacketId) {
    this.cdtpHeader = cdtpHeader;
    this.xPacketId = xPacketId;
  }

  private static class SingletonHolder {

    private final static CdtpHeaderDTO instance = new CdtpHeaderDTO("", "");
  }

  public static CdtpHeaderDTO getInstance() {
    return SingletonHolder.instance;
  }


  public String getxPacketId() {
    return xPacketId;
  }

  public void setxPacketId(String xPacketId) {
    this.xPacketId = xPacketId;
  }

  public String getCdtpHeader() {
    return cdtpHeader;
  }

  public void setCdtpHeader(String cdtpHeader) {
    this.cdtpHeader = cdtpHeader;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CdtpHeaderDTO that = (CdtpHeaderDTO) o;
    return Objects.equals(xPacketId, that.xPacketId) &&
        Objects.equals(cdtpHeader, that.cdtpHeader);
  }

  @Override
  public int hashCode() {
    return Objects.hash(xPacketId, cdtpHeader);
  }

  @Override
  public String toString() {
    return "CdtpHeaderDTO{" +
        "xPacketId='" + xPacketId + '\'' +
        ", cdtpHeader='" + cdtpHeader + '\'' +
        '}';
  }
}
