package com.syswin.temail.usermail.core.encrypt;

public class EncryptSharedKeyDto implements java.io.Serializable {

  public EncryptSharedKeyDto() {
  }

  public EncryptSharedKeyDto(String sharedKey) {
    this.sharedKey = sharedKey;
  }

  private String sharedKey;

  public String getSharedKey() {
    return sharedKey;
  }

  public void setSharedKey(String sharedKey) {
    this.sharedKey = sharedKey;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("EncryptSharedKeyDto{");
    sb.append("sharedKey='").append(sharedKey).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
