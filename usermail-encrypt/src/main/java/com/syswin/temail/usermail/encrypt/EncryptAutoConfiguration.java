package com.syswin.temail.usermail.encrypt;

import com.syswin.temail.kms.vault.KeyAwareVault;
import com.syswin.temail.usermail.core.encrypt.ICryptAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncryptAutoConfiguration {

  @Autowired
  private KeyAwareVault vaultKeeper;

  @Bean
  public ICryptAdapter encryptAdapter() {
    return new CryptAdapter(vaultKeeper);
  }
}
