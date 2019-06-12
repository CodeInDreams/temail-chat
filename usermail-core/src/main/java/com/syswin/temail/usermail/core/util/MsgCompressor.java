package com.syswin.temail.usermail.core.util;


import com.syswin.temail.usermail.common.Contants.RESULT_CODE;
import com.syswin.temail.usermail.core.exception.IllegalGmArgsException;
import java.io.IOException;
import java.util.Base64;

public class MsgCompressor {

  private GzipUtils gzip = new GzipUtils();

  public byte[] zip(byte[] data) {
    try {
      return gzip.zip(data);
    } catch (IOException e) {
      throw new IllegalGmArgsException(RESULT_CODE.ERROR_MSG_ZIP);
    }
  }

  public byte[] zipWithDecode(final String data) {
    byte[] zip = new byte[0];
    try {
      zip = gzip.zip(Base64.getUrlDecoder().decode(data.getBytes("utf-8")));
    } catch (IOException e) {
      throw new IllegalGmArgsException(RESULT_CODE.ERROR_MSG_DECODE, data);
    }
    return zip;
  }

  public String unzipEncode(final byte[] data) {
    String s = null;
    try {
      s = new String(Base64.getUrlEncoder().encode(gzip.unzip(data)), "utf-8");
    } catch (IOException e) {
      throw new IllegalGmArgsException(RESULT_CODE.ERROR_MSG_ENCODE);
    }
    return s;
  }

  public byte[] zip(final String data) {
    byte[] zip = new byte[0];
    try {
      zip = gzip.zip(data.getBytes("utf-8"));
    } catch (IOException e) {
      throw new IllegalGmArgsException(RESULT_CODE.ERROR_MSG_DECODE, data);
    }
    return zip;
  }

  public String unzip(final byte[] data) {
    String s = null;
    try {
      s = new String(gzip.unzip(data), "utf-8");
    } catch (IOException e) {
      throw new IllegalGmArgsException(RESULT_CODE.ERROR_MSG_ENCODE);
    }
    return s;
  }
}
