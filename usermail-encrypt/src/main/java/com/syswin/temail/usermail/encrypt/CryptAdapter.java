package com.syswin.temail.usermail.encrypt;

import static com.syswin.temail.kms.vault.CipherAlgorithm.ECDSA;

import com.syswin.temail.kms.vault.KeyAwareAsymmetricCipher;
import com.syswin.temail.kms.vault.KeyAwareVault;
import com.syswin.temail.kms.vault.PublicKeyCipher;
import com.syswin.temail.kms.vault.aes.SymmetricCipher;
import com.syswin.temail.ps.common.entity.DataEncryptType;
import com.syswin.temail.ps.common.packet.KeyAwarePacketEncryptor;
import com.syswin.temail.usermail.common.Contants.RESULT_CODE;
import com.syswin.temail.usermail.core.encrypt.EncryptPubKeyDto;
import com.syswin.temail.usermail.core.encrypt.EncryptSharedKeyDto;
import com.syswin.temail.usermail.core.encrypt.ICryptAdapter;
import com.syswin.temail.usermail.core.exception.IllegalGMArgsException;
import org.springframework.beans.factory.annotation.Autowired;


public class CryptAdapter implements ICryptAdapter {

  private final KeyAwareVault vaultKeeper;

  @Autowired
  public CryptAdapter(KeyAwareVault vault) {
    vaultKeeper = vault;
  }

  @Override
  public EncryptPubKeyDto encryptPubKey(String temail) {
    try {
      KeyAwareAsymmetricCipher asymmetricCipher = vaultKeeper.asymmetricCipher(ECDSA);
      return new EncryptPubKeyDto(asymmetricCipher.register(temail));
    } catch (Exception e) {
      throw new IllegalGMArgsException(RESULT_CODE.ERROR_CIPHER, e.getMessage());
    }
  }

  @Override
  public EncryptSharedKeyDto encryptSharedKey(String temail) {
    try {
      SymmetricCipher symmetricCipher = vaultKeeper.symmetricCipher();
      return new EncryptSharedKeyDto(symmetricCipher.getKey(temail));
    } catch (Exception e) {
      throw new IllegalGMArgsException(RESULT_CODE.ERROR_CIPHER, e.getMessage());
    }
  }

  @Override
  public EncryptPubKeyDto acquirePubKey(String temail) {
    try {
      KeyAwareAsymmetricCipher asymmetricCipher = vaultKeeper.asymmetricCipher(ECDSA);
      return new EncryptPubKeyDto(asymmetricCipher.publicKey(temail).orElse(""));
    } catch (Exception e) {
      throw new IllegalGMArgsException(RESULT_CODE.ERROR_CIPHER, e.getMessage());
    }
  }

  @Override
  public boolean verify(String publicKey, String unsigned, String signature) {
    try {
      PublicKeyCipher asymmetricCipher = vaultKeeper.publicKeyCipher(ECDSA);
      return asymmetricCipher.verify(publicKey, unsigned, signature);
    } catch (Exception e) {
      throw new IllegalGMArgsException(RESULT_CODE.ERROR_CIPHER, e.getMessage());
    }
  }

  @Override
  public String encryptMsg(String key, String msg) {
    try {
      SymmetricCipher symmetricCipher = vaultKeeper.symmetricCipher();
      String secretKey = symmetricCipher.getKey(key);
      return symmetricCipher.encrypt(secretKey, msg);
    } catch (Exception e) {
      throw new IllegalGMArgsException(RESULT_CODE.ERROR_CIPHER, e.getMessage());
    }
  }

  @Override
  public String decryptMsg(String key, String msg) {
    try {
      SymmetricCipher symmetricCipher = vaultKeeper.symmetricCipher();
      String secretKey = symmetricCipher.getKey(key);
      return symmetricCipher.decrypt(secretKey, msg);
    } catch (Exception e) {
      throw new IllegalGMArgsException(RESULT_CODE.ERROR_CIPHER, e.getMessage());
    }
  }

  @Override
  public String encryptByTemail(String temail, String plainText) {
    try {
      KeyAwareAsymmetricCipher asymmetricCipher = vaultKeeper.asymmetricCipher(ECDSA);
      return asymmetricCipher.encrypt(temail, plainText);
    } catch (Exception e) {
      throw new IllegalGMArgsException(RESULT_CODE.ERROR_CIPHER, e.getMessage());
    }
  }

  @Override
  public String decryptByTemail(String temail, String plainText) {
    try {
      KeyAwareAsymmetricCipher asymmetricCipher = vaultKeeper.asymmetricCipher(ECDSA);
      return asymmetricCipher.decrypt(temail, plainText);
    } catch (Exception e) {
      throw new IllegalGMArgsException(RESULT_CODE.ERROR_CIPHER, e.getMessage());
    }
  }

  @Override
  public byte[] encryptByPubKey(String pubKey, String plainText, DataEncryptType dataEncryptType) {
    try {
      KeyAwarePacketEncryptor keyAwarePacketEncryptor = new KeyAwarePacketEncryptor(vaultKeeper);
      byte[] encryptData = keyAwarePacketEncryptor
          .encryptWithPubKey(pubKey, plainText.getBytes(), dataEncryptType);
      return encryptData;
    } catch (Exception e) {
      throw new IllegalGMArgsException(RESULT_CODE.ERROR_CIPHER, e.getMessage());
    }
  }
}
