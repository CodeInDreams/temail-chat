package com.syswin.temail.usermail.core.exception;


import com.syswin.temail.usermail.common.Contants.RESULT_CODE;

public class IllegalGmArgsException extends RuntimeException {

  private RESULT_CODE resultCode;

  public IllegalGmArgsException() {
    super();
  }

  public IllegalGmArgsException(RESULT_CODE resultCode) {
    super(resultCode.toString());
    this.resultCode = resultCode;
  }

  public IllegalGmArgsException(String message) {
    super(message);
  }

  public IllegalGmArgsException(RESULT_CODE resultCode, String message, Throwable cause) {
    super(message, cause);
    this.resultCode = resultCode;
  }

  public IllegalGmArgsException(RESULT_CODE resultCode, String message) {
    super(message);
    this.resultCode = resultCode;
  }

  public IllegalGmArgsException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalGmArgsException(Throwable cause) {
    super(cause);
  }

  protected IllegalGmArgsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public RESULT_CODE getResultCode() {
    return resultCode;
  }

  public void setResultCode(RESULT_CODE resultCode) {
    this.resultCode = resultCode;
  }
}
