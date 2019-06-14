package com.syswin.temail.usermail.core.exception;


import com.syswin.temail.usermail.common.ResultCodeEnum;

public class IllegalGmArgsException extends RuntimeException {

  private ResultCodeEnum resultCode;

  public IllegalGmArgsException() {
    super();
  }

  public IllegalGmArgsException(ResultCodeEnum resultCode) {
    super(resultCode.toString());
    this.resultCode = resultCode;
  }

  public IllegalGmArgsException(String message) {
    super(message);
  }

  public IllegalGmArgsException(ResultCodeEnum resultCode, String message, Throwable cause) {
    super(message, cause);
    this.resultCode = resultCode;
  }

  public IllegalGmArgsException(ResultCodeEnum resultCode, String message) {
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

  public ResultCodeEnum getResultCode() {
    return resultCode;
  }

  public void setResultCode(ResultCodeEnum resultCode) {
    this.resultCode = resultCode;
  }
}
