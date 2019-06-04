package com.syswin.temail.usermail.core.encrypt;

import com.syswin.temail.ps.common.entity.DataEncryptType;

public interface ICryptAdapter {

  EncryptPubKeyDto encryptPubKey(String temail);

  EncryptSharedKeyDto encryptSharedKey(String temail);

  EncryptPubKeyDto acquirePubKey(String temail);

  boolean verify(String publicKey, String unsigned, String signature);

  String encryptMsg(String key, String msg);

  String decryptMsg(String key, String msg);

  String encryptByTemail(String temail, String plainText);

  String decryptByTemail(String temail, String plainText);

  byte[] encryptByPubKey(String pubKey, String plainText, DataEncryptType dataEncryptType);

}
