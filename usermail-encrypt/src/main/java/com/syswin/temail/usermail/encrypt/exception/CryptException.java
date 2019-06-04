package com.syswin.temail.usermail.encrypt.exception;

public class CryptException extends RuntimeException {

  public CryptException() {
  }

  public CryptException(String message) {
    super(message);
  }

  public CryptException(String message, Throwable cause) {
    super(message, cause);
  }

  public CryptException(Throwable cause) {
    super(cause);
  }

  public CryptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
