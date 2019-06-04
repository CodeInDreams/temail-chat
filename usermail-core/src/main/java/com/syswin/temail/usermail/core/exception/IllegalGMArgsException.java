package com.syswin.temail.usermail.core.exception;


import com.syswin.temail.usermail.common.Contants.RESULT_CODE;

public class IllegalGMArgsException extends RuntimeException {

  private RESULT_CODE resultCode;

  public IllegalGMArgsException() {
    super();
  }

  public IllegalGMArgsException(RESULT_CODE resultCode) {
    super(resultCode.toString());
    this.resultCode = resultCode;
  }

  public IllegalGMArgsException(String message) {
    super(message);
  }

  public IllegalGMArgsException(RESULT_CODE resultCode, String message, Throwable cause) {
    super(message, cause);
    this.resultCode = resultCode;
  }

  public IllegalGMArgsException(RESULT_CODE resultCode, String message) {
    super(message);
    this.resultCode = resultCode;
  }

  public IllegalGMArgsException(String message, Throwable cause) {
    super(message, cause);
  }

  public IllegalGMArgsException(Throwable cause) {
    super(cause);
  }

  protected IllegalGMArgsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public RESULT_CODE getResultCode() {
    return resultCode;
  }

  public void setResultCode(RESULT_CODE resultCode) {
    this.resultCode = resultCode;
  }
}
