package com.syswin.temail.usermail.core.encrypt;

public class EncryptPubKeyDto implements java.io.Serializable{

  public EncryptPubKeyDto() {
  }

  public EncryptPubKeyDto(String pubKey) {
    this.pubKey = pubKey;
  }

  private String pubKey;

  public String getPubKey() {
    return pubKey;
  }

  public void setPubKey(String pubKey) {
    this.pubKey = pubKey;
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder("EncryptPubKeyDto{");
    sb.append("pubKey='").append(pubKey).append('\'');
    sb.append('}');
    return sb.toString();
  }
}
