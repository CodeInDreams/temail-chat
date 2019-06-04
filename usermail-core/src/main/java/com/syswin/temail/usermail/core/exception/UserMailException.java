package com.syswin.temail.usermail.core.exception;

public class UserMailException extends RuntimeException {

  public UserMailException() {
    super();
  }

  public UserMailException(String message) {
    super(message);
  }

  public UserMailException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserMailException(Throwable cause) {
    super(cause);
  }

  protected UserMailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
